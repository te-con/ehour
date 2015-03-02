package net.rrm.ehour.persistence.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfigFactory {
    @Value("${ehour.database}")
    public String databaseType;

    @Value("${ehour.database.driver:}")
    public String driver;

    @Value("${ehour.database.url:}")
    public String url;

    @Value("${ehour.database.username:}")
    public String username;

    @Value("${ehour.database.password:}")
    public String password;

    @Bean
    public DatabaseConfig createDatabaseConfig() {
        Database database = Database.valueOf(databaseType.toUpperCase());

        // TODO verify configuration

        return new DatabaseConfig(database, driver, url, username, password);
    }
}
