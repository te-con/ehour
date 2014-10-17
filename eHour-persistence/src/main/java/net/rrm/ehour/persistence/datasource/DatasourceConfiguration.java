package net.rrm.ehour.persistence.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
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
    public DataSource createDatasource() throws IOException {
        if (DB_DERBY.equalsIgnoreCase(databaseType)) {
            return DerbyDataSourceFactory.createDataSource(isInTestMode() ? "memory:ehourDb;create=true" : "derby");
        } else {
            org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setInitialSize(5);
            ds.setMaxActive(10);
            ds.setMaxIdle(5);
            ds.setMinIdle(2);
            return ds;
        }
    }

    private boolean isInTestMode() {
        return Boolean.parseBoolean(System.getProperty("EHOUR_TEST", "false"));
    }
}
