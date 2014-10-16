package net.rrm.ehour.persistence.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class DatasourceConfiguration {
    public static final String DERBY_DB = "ehourDb";

    public static final String DERBY_IN_MEMORY_DB = "memory:ehourDb";

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
        if ("derby".equalsIgnoreCase(databaseType)) {
            return DerbyDataSourceFactory.createDataSource(isInTestMode() ? DERBY_IN_MEMORY_DB : DERBY_DB);
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
