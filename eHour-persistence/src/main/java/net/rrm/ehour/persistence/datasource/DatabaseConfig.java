package net.rrm.ehour.persistence.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseConfig {
//    @Value("${ehour.database}")
    String databaseType;

//    @Value("${ehour.database.driver:}")
    String driver;

//    @Value("${ehour.database.url:}")
    String url;

//    @Value("${ehour.database.username:}")
    String username;

//    @Value("${ehour.database.password:}")
    String password;

//    @Value("${ehour.database.checkouttimeout:10000}")
    Integer checkoutTimeout;

    public DatabaseConfig(@Value("${ehour.database}") String databaseType,
                          @Value("${ehour.database.driver:}") String driver,
                          @Value("${ehour.database.url:}") String url,
                          @Value("${ehour.database.username:}") String username,
                          @Value("${ehour.database.password:}") String password,
                          @Value("${ehour.database.checkouttimeout:10000}") Integer checkoutTimeout) {
        this.databaseType = databaseType;
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.checkoutTimeout = checkoutTimeout;
    }

    @PostConstruct
    public void verifyConfig() {
        // TODO
    }
}
