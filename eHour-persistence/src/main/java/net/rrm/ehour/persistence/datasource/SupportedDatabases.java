package net.rrm.ehour.persistence.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
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
            if (DatasourceConfiguration.CP_HIKARI.equalsIgnoreCase(config.connectionPool)) {
                return createHikariDatasource(config, "mysql");
            } else {
                return createC3p0Datasource(config, "mysql", "com.mysql.jdbc.Driver");
            }

        }
    },
    POSTGRESQL {
        @Override
        public DataSource createDatasource(DatasourceConfiguration config) throws IOException, PropertyVetoException, URISyntaxException {
            if (DatasourceConfiguration.CP_HIKARI.equalsIgnoreCase(config.connectionPool)) {
                return createHikariDatasource(config, "postgresql");
            } else {
                return createC3p0Datasource(config, "postgresql", "org.postgresql.Driver");
            }
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

    private static DataSource createC3p0Datasource(DatasourceConfiguration config, String db, String defaultDriverClass) throws IOException, PropertyVetoException, URISyntaxException {
        ComboPooledDataSource ds = new ComboPooledDataSource();

        String username = config.username;
        String password = config.password;

        // try to extract username/password from the db URL when they're not provided
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

            ds.setJdbcUrl(dbUrl);
        } else {
            verify(username, password);
            verify("ehour.database.url", config.url);
            ds.setJdbcUrl(config.url);
        }

        ds.setUser(username);
        ds.setPassword(password);

        ds.setNumHelperThreads(10);

        String driverClass = StringUtils.isNotBlank(config.driver) ? config.driver : defaultDriverClass;
        verify("ehour.database.driver", driverClass);
        LOGGER.info("Using driver class: " + driverClass);

        ds.setDriverClass(driverClass);
        ds.setInitialPoolSize(10);
        ds.setAcquireIncrement(2);

        ds.setIdleConnectionTestPeriod(30);
        ds.setTestConnectionOnCheckout(false);
        ds.setTestConnectionOnCheckin(true);

        ds.setMaxPoolSize(100);
        ds.setMaxStatements(0);
        ds.setMinPoolSize(10);
        ds.setCheckoutTimeout(config.checkoutTimeout);

        LOGGER.info(config.toString());
        return ds;
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
