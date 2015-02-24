package net.rrm.ehour.persistence.datasource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
public class DatasourceConfiguration {
    public static final String DB_DERBY = "derby";
    public static final String DB_MYSQL = "mysql";
    public static final String DB_POSTGRESQL = "postgresql";

    private static final Logger LOGGER = Logger.getLogger(DatasourceConfiguration.class);

    @Value("${ehour.database}")
    String databaseType;

    @Value("${ehour.database.driver:}")
    String driver;

    @Value("${ehour.database.url:}")
    String url;

    @Value("${ehour.database.username:}")
    String username;

    @Value("${ehour.database.password:}")
    String password;

    @Value("${ehour.database.checkouttimeout:10000}")
    Integer checkoutTimeout;

    @Bean
    public DataSource createDatasource() throws IOException, PropertyVetoException, URISyntaxException {
        return getDatabaseType().createDatasource(this);
    }

    public SupportedDatabases getDatabaseType() {
        return SupportedDatabases.valueOf(databaseType.toUpperCase());
    }
}
