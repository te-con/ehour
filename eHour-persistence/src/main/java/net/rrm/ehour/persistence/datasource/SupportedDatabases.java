package net.rrm.ehour.persistence.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public enum SupportedDatabases {
    DERBY {
        @Override
        public DataSource createDatasource(DatasourceConfiguration config) throws IOException {
            return DerbyDataSourceFactory.createDataSource(isInTestMode() ? "memory:ehourDb;create=true" : "ehourDb");
        }

        private boolean isInTestMode() {
            return Boolean.parseBoolean(System.getProperty("EHOUR_TEST", "false"));
        }
    },
    MYSQL {
        @Override
        public DataSource createDatasource(DatasourceConfiguration config) throws IOException, PropertyVetoException, URISyntaxException {
            return createHikariDatasource(config, "mysql");
        }
    },
    POSTGRESQL {
        @Override
        public DataSource createDatasource(DatasourceConfiguration config) throws IOException, PropertyVetoException, URISyntaxException {
            return createHikariDatasource(config, "postgresql");
        }
    };

    private static final Logger LOGGER = Logger.getLogger(SupportedDatabases.class);

    public abstract DataSource createDatasource(DatasourceConfiguration config) throws IOException, PropertyVetoException, URISyntaxException;

    private static DataSource createHikariDatasource(DatasourceConfiguration config, String db) throws IOException, PropertyVetoException, URISyntaxException {
        HikariConfig hikariConfig = new HikariConfig();

        String username = config.username;
        String password = config.password;

        if (StringUtils.isBlank(config.username) && StringUtils.isBlank(password)) {
            URI dbUri = new URI(config.url);

            String userInfo = dbUri.getUserInfo();

            if (StringUtils.isNotBlank(userInfo)) {
                String[] splitted = userInfo.split(":");
                username = splitted[0];
                password = splitted[1];
            }

            String dbUrl = String.format("jdbc:%s://%s:%d%s", db, dbUri.getHost(), dbUri.getPort(), dbUri.getPath());

            LOGGER.info("Only a DB URL was provided, stripped of username and password and connecting to " + dbUrl);

            verify(username, password);
            verify("ehour.database.url", dbUrl);

            hikariConfig.setJdbcUrl(dbUrl);
        } else {
            verify(username, password);
            verify("ehour.database.url", config.url);
            hikariConfig.setJdbcUrl(config.url);
        }

        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        hikariConfig.addDataSourceProperty("autoCommit", "false");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");

        return new HikariDataSource(hikariConfig);
    }

    private static void verify(String username, String password) {
        verifyItem("ehour.database.username", username);
        verifyItem("ehour.database.password", password);
    }

    private static void verifyItem(String name, String value) {
        if (StringUtils.isBlank(value)) {
            throw new ConfigurationException(String.format("%s is required but no value provided", name));
        }
    }
}
