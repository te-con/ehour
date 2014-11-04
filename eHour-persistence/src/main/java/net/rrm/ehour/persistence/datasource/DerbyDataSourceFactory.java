package net.rrm.ehour.persistence.datasource;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author thies
 */
public class DerbyDataSourceFactory {
    /**
     * Create datasource and validate database
     */
    public static DataSource createDataSource(String databaseName) throws IOException {
        EmbeddedConnectionPoolDataSource dataSource = new EmbeddedConnectionPoolDataSource();
        dataSource.setDatabaseName(databaseName);

        return dataSource;
    }
}
