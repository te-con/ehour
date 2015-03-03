package net.rrm.ehour.persistence.appconfig;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DerbyConnectionProvider implements ConnectionProvider {
    public static EmbeddedConnectionPoolDataSource dataSource;

    public DerbyConnectionProvider() throws IOException {
        String databaseName = isInTestMode() ? "memory:ehourDb;create=true" : "ehourDb";
        dataSource = new EmbeddedConnectionPoolDataSource();
        dataSource.setDatabaseName(databaseName);
        System.out.println("creating");
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        return connection;
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }

    private boolean isInTestMode() {
        return Boolean.parseBoolean(System.getProperty("EHOUR_TEST", "false"));
    }
}
