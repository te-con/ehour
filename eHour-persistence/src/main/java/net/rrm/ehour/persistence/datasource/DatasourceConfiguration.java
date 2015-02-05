package net.rrm.ehour.persistence.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;

@Configuration
public class DatasourceConfiguration {
    public static final String DB_DERBY = "derby";
    public static final String DB_MYSQL = "mysql";
    public static final String DB_POSTGRESQL = "postgresql";

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
    public DataSource createDatasource() throws IOException, PropertyVetoException {
        if (DB_DERBY.equalsIgnoreCase(databaseType)) {
            return DerbyDataSourceFactory.createDataSource(isInTestMode() ? "memory:ehourDb;create=true" : "derby");
        } else {
            ComboPooledDataSource ds = new ComboPooledDataSource();
            ds.setDriverClass(driver);
            ds.setJdbcUrl(url);
            ds.setUser(username);
            ds.setPassword(password);
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
