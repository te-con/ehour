package net.rrm.ehour.persistence.database;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfigFactory {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConfigFactory.class);

    @Bean
    public DatabaseConfig createDatabaseConfig(@Value("${ehour.database}") String databaseType,
                                               @Value("${ehour.database.driver:}") String driver,
                                               @Value("${ehour.database.url:}") String url,
                                               @Value("${ehour.database.username:}") String username,
                                               @Value("${ehour.database.password:}") String password) throws URISyntaxException {
        Database database = Database.valueOf(databaseType.toUpperCase());

        String sanitizedDriver = StringUtils.isBlank(driver) ? database.defaultDriver : driver;

        String sanitizedUrl = null;
        String sanitizedUsername = null;
        String sanitizedPassword = null;

        if (database != Database.DERBY) {
            if (StringUtils.isBlank(username) && StringUtils.isBlank(password)) {
                URI dbUri = new URI(url);

                String userInfo = dbUri.getUserInfo();

                if (StringUtils.isNotBlank(userInfo)) {
                    String[] splitted = userInfo.split(":");

                    if (splitted.length == 2) {
                        sanitizedUsername = splitted[0];
                        sanitizedPassword = splitted[1];
                    }
                }

                String query = (StringUtils.isNotBlank(dbUri.getQuery()))  ? "?" + dbUri.getQuery() : "";
                String dbUrl = String.format("jdbc:%s://%s:%d%s%s", database.name().toLowerCase(), dbUri.getHost(), dbUri.getPort(), dbUri.getPath(), query);

                LOGGER.info("Only a DB URL was provided, stripped of username and password and using url " + dbUrl);

                sanitizedUrl = dbUrl;
            } else {
                sanitizedUrl = url;
                sanitizedUsername = username;
                sanitizedPassword = password;
            }

            verifyUsernamePassword(sanitizedUsername, sanitizedPassword);
            verifyItem("ehour.database.url", sanitizedUrl);
        }

        return new DatabaseConfig(database, sanitizedDriver, sanitizedUrl, sanitizedUsername, sanitizedPassword);
    }

    private void verifyUsernamePassword(String username, String password) {
        verifyItem("ehour.database.username", username);
        verifyItem("ehour.database.password", password);
    }

    private void verifyItem(String name, String value) {
        if (StringUtils.isBlank(value)) {
            throw new ConfigurationException(String.format("%s is required but no value provided", name));
        }
    }
}
