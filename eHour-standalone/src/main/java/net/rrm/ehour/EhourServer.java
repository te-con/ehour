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

package net.rrm.ehour;

import net.rrm.ehour.appconfig.EhourHomeUtil;
import net.rrm.ehour.persistence.datasource.DerbyDataSourceFactory;
import net.rrm.ehour.util.IoUtil;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Start jetty
 */

public class EhourServer {

    private DataSource dataSource;

    public void start(String configurationFilename) throws Exception {
        ServerPropertiesConfigurator configuration = new ServerPropertiesConfigurator();

        ServerConfig config = configuration.configureFromProperties(replaceSystemEnv(configurationFilename));
        startServer(config);
    }

    private String replaceSystemEnv(String filename) {
        String ehourHome = EhourHomeUtil.getEhourHome();

        if (StringUtils.isBlank(ehourHome)) {
            throw new IllegalArgumentException("EHOUR_HOME is not defined as a environment variable or system property.");
        }

        return filename.replace("${EHOUR_HOME}", ehourHome);
    }

    private void startServer(ServerConfig config) throws Exception {
        Server server = new Server(config.getPort());

        File jettyXmlFile = EhourHomeUtil.getFileInConfDir(config.getDefaultConfigFileName());

        FileInputStream stream = null;

        try {
            stream = new FileInputStream(jettyXmlFile);

            XmlConfiguration configuration = new XmlConfiguration(stream);
            configuration.configure(server);

            registerJndiDS(config);

            server.start();

            if (!isInTestMode()) {
                server.join();
            }
        } finally {
            IoUtil.close(stream);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private boolean isInTestMode() {
        return Boolean.parseBoolean(System.getProperty("EHOUR_TEST", "false"));
    }

    private void registerJndiDS(ServerConfig config) throws IOException, NamingException {
        dataSource = createDataSource(config);
        new Resource("jdbc/eHourDS", dataSource);
    }

    private DataSource createDataSource(ServerConfig config) throws IOException {
        DataSource dataSource;

        if ("derby".equalsIgnoreCase(config.getDataBase())) {
            dataSource = new DerbyDataSourceFactory().createDataSource(isInTestMode() ? "memory:ehourDb" : "ehourDb");
        } else {
            BasicDataSource dbcpDataSource = new BasicDataSource();

            dbcpDataSource.setDriverClassName(config.getDataBaseDriver());
            dbcpDataSource.setUrl(config.getDataBaseURL());
            dbcpDataSource.setUsername(config.getDataBaseUsername());
            dbcpDataSource.setPassword(config.getDataBasePassword());

            dbcpDataSource.setMaxActive(100);
            dbcpDataSource.setMaxIdle(30);
            dbcpDataSource.setValidationQuery("SELECT 1");
            dbcpDataSource.setTestOnBorrow(true);
            dbcpDataSource.setTestWhileIdle(true);
            dbcpDataSource.setPoolPreparedStatements(true);

            dataSource = dbcpDataSource;
        }
        return dataSource;
    }
}
