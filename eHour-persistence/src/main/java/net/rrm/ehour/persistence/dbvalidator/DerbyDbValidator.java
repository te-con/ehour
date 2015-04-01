/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.persistence.dbvalidator;

import net.rrm.ehour.config.ConfigurationItem;
import org.apache.commons.beanutils.DynaBean;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DataReader;
import org.apache.ddlutils.io.DatabaseDataIO;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Derby database accessor methods
 */
public class DerbyDbValidator {
    private static final String DDL_FILE = "derby/ddl/ddl-ehour-%s.xml";
    private static final String DML_FILE = "derby/ddl/dml-ehour-%s.xml";
    private static final String DML_DIFF_FILE = "derby/ddl/dml-ehour-%s-diff.xml";

    public enum DdlType {NONE, CREATE_TABLE, ALTER_TABLE}

    private static final Logger LOGGER = Logger.getLogger(DerbyDbValidator.class);

    private EmbeddedDataSource dataSource;
    private String requiredDbVersion;


    public DerbyDbValidator(String requiredDbVersion, DataSource dataSource) {
        this.requiredDbVersion = requiredDbVersion;
        this.dataSource = (EmbeddedDataSource) dataSource;
    }

    public DdlType checkDatabaseState() {
        boolean databaseInState;
        String currentVersion;
        DdlType ddlType;

        LOGGER.info("Verifying datamodel version. Minimum version: " + requiredDbVersion);

        Connection connection = null;

        try {
            dataSource.setCreateDatabase("create");

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            currentVersion = getCurrentVersion(connection);

            databaseInState = (currentVersion != null) && currentVersion.equalsIgnoreCase(requiredDbVersion);

            if (databaseInState) {
                ddlType = DdlType.NONE;
                LOGGER.info("Datamodel is the required version.");
            } else {
                LOGGER.info("Datamodel of version " + currentVersion + " found. Upgrading to " + requiredDbVersion);

                ddlType = DdlType.ALTER_TABLE;
            }
        } catch (SQLException e) {
            ddlType = DdlType.CREATE_TABLE;
            LOGGER.info("Could not determine datamodel's version, recreating..");
        } finally {
            dataSource.setCreateDatabase("");

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
//                LOGGER.error("Failed to close connection", e);
            }
        }

        if (ddlType != DdlType.NONE) {
            try {
                createOrAlterDatamodel(dataSource, ddlType);
            } catch (Exception e) {
                LOGGER.error("Failed to create or upgrade datamodel", e);
            }
        }

        return ddlType;
    }

    /**
     * Create datamodel and fill with initial data
     */
    private void createOrAlterDatamodel(DataSource dataSource, DdlType ddlType) throws DdlUtilsException, IOException {
        Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);

        Resource resource = new ClassPathResource(getDdlFilename());

        DatabaseIO reader = new DatabaseIO();
        reader.setValidateXml(false);
        reader.setUseInternalDtd(true);

        Database ddlModel = reader.read(new InputStreamReader(resource.getInputStream(), "UTF-8"));

        if (ddlType == DdlType.CREATE_TABLE) {
            platform.createTables(ddlModel, false, false);
            insertInitialData(platform, ddlModel);
        } else {
            platform.alterTables(ddlModel, false);
            insertDiffData(platform, ddlModel);
            updateVersion(platform, ddlModel);
        }
    }

    /**
     * Insert data
     */
    private void insertInitialData(Platform platform, Database model) throws DdlUtilsException, IOException {
        insertData(platform, model, getDmlFilename());
        LOGGER.info("Data inserted");
    }


    /**
     * Insert diff data (for existing db's)
     */
    private void insertDiffData(Platform platform, Database model) throws DdlUtilsException, IOException {
        insertData(platform, model, getDmlDiffFilename());
        LOGGER.info("Data updated");
    }

    private void insertData(Platform platform, Database model, String filename) throws IOException {
        Resource resource = new ClassPathResource(filename);

        if (!resource.exists()) {
            return;
        }

        DatabaseDataIO dataIO = new DatabaseDataIO();

        DataReader dataReader = dataIO.getConfiguredDataReader(platform, model);

        dataReader.getSink().start();

        dataIO.writeDataToDatabase(dataReader, new InputStreamReader(resource.getInputStream(), "UTF-8"));
    }

    /**
     * Get current version of database state
     */
    private String getCurrentVersion(Connection connection) throws SQLException {
        String version = null;

        try (PreparedStatement statement = connection.prepareStatement("SELECT config_value FROM CONFIGURATION WHERE config_key = ?")) {
            statement.setString(1, ConfigurationItem.VERSION.getDbField());

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    version = results.getString("config_value");
                }
            }
        }

        return version;
    }

    private void updateVersion(Platform platform, Database database) {
        DynaBean configuration = database.createDynaBeanFor("CONFIGURATION", false);
        configuration.set("config_key", "version");
        platform.delete(database, configuration);

        configuration.set("config_value", requiredDbVersion);

        platform.insert(database, configuration);
    }

    private String getDdlFilename() {
        return String.format(DDL_FILE, requiredDbVersion);
    }

    private String getDmlFilename() {
        return String.format(DML_FILE, requiredDbVersion);
    }

    private String getDmlDiffFilename() {
        return String.format(DML_DIFF_FILE, requiredDbVersion);
    }

}
