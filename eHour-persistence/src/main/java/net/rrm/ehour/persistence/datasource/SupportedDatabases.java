package net.rrm.ehour.persistence.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
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
            return createC3p0Datasource(config, "mysql", "com.mysql.jdbc.Driver");
        }
    },
    POSTGRESQL {
        @Override
        public DataSource createDatasource(DatasourceConfiguration config) throws IOException, PropertyVetoException, URISyntaxException {
            return createC3p0Datasource(config, "postgresql", "org.postgresql.Driver");
        }
    };

    private static final Logger LOGGER = Logger.getLogger(SupportedDatabases.class);

    public abstract DataSource createDatasource(DatasourceConfiguration config) throws IOException, PropertyVetoException, URISyntaxException;

    private static DataSource createC3p0Datasource(DatasourceConfiguration config, String db, String defaultDriverClass) throws IOException, PropertyVetoException, URISyntaxException {
        ComboPooledDataSource ds = new ComboPooledDataSource();

        String username = config.username;
        String password = config.password;

        // try to extract username/password from the db URL when they're not provided
        if (StringUtils.isBlank(config.username) && StringUtils.isBlank(password)) {
            URI dbUri = new URI(config.url);

            String[] splitted = dbUri.getUserInfo().split(":");
            username = splitted[0];
            password = splitted[1];

            String dbUrl = String.format("jdbc:%s://%s:%d%s", db, dbUri.getHost(), dbUri.getPort(), dbUri.getPath());

            LOGGER.info("Only a DB URL was provided, stripped of username and password and connecting to " + dbUrl);

            ds.setJdbcUrl(dbUrl);
        } else {
            ds.setJdbcUrl(config.url);
        }

        ds.setUser(username);
        ds.setPassword(password);

        ds.setNumHelperThreads(10);

        ds.setDriverClass(StringUtils.isNotBlank(config.driver) ? config.driver : defaultDriverClass);
        ds.setInitialPoolSize(10);
        ds.setAcquireIncrement(2);


        ds.setIdleConnectionTestPeriod(30);
        ds.setTestConnectionOnCheckout(false);
        ds.setTestConnectionOnCheckin(true);

        ds.setMaxPoolSize(100);
        ds.setMaxStatements(0);
        ds.setMinPoolSize(10);
        ds.setCheckoutTimeout(config.checkoutTimeout);
        return ds;
    }
}
