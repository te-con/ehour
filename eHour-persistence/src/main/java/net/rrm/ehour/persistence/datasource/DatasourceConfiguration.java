package net.rrm.ehour.persistence.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatasourceConfiguration {
    public static final String DB_DERBY = "derby";
    public static final String DB_MYSQL = "mysql";
    public static final String DB_POSTGRESQL = "postgresql";

    private static final Logger LOGGER = Logger.getLogger(DatasourceConfiguration.class);

    @Value("${ehour.database}")
    private String databaseType;

    @Value("${ehour.database.driver:}")
    private String driver;

    @Value("${ehour.database.url:}")
    private String url;

    @Value("${ehour.database.username:}")
    private String username;

    @Value("${ehour.database.password:}")
    private String password;

    @Bean
    public DataSource createDatasource() throws IOException, PropertyVetoException, URISyntaxException {
        if (DB_DERBY.equalsIgnoreCase(databaseType)) {
            return DerbyDataSourceFactory.createDataSource(isInTestMode() ? "memory:ehourDb;create=true" : "derby");
        } else {
            ComboPooledDataSource ds = new ComboPooledDataSource();

            // try to extract username/password from the db URL when they're not provided
            if (StringUtils.isBlank(username) && StringUtils.isBlank(password)) {
                URI dbUri = new URI(url);

                String[] splitted = dbUri.getUserInfo().split(":");
                username = splitted[0];
                password = splitted[1];

                String db = dbUri.getScheme();

                if (db.toLowerCase().startsWith("postgres")) {
                    db = DB_POSTGRESQL; // postgres is the schema while it should be postgresql
                }

                String dbUrl = String.format("jdbc:%s://%s:%d%s", db, dbUri.getHost(), dbUri.getPort(), dbUri.getPath());

                LOGGER.info("Only a DB URL was provided, stripped of username and password and connecting to " + dbUrl);

                ds.setJdbcUrl(dbUrl);
            } else {
                ds.setJdbcUrl(url);
            }

            ds.setUser(username);
            ds.setPassword(password);

            ds.setDriverClass(driver);
            ds.setInitialPoolSize(5);
            ds.setAcquireIncrement(2);

            ds.setIdleConnectionTestPeriod(30);
            ds.setTestConnectionOnCheckout(false);
            ds.setTestConnectionOnCheckin(true);

            ds.setMaxPoolSize(100);
            ds.setMaxStatements(0);
            ds.setMinPoolSize(10);
            ds.setCheckoutTimeout(100);
            return ds;
        }
    }

    private boolean isInTestMode() {
        return Boolean.parseBoolean(System.getProperty("EHOUR_TEST", "false"));
    }
}
