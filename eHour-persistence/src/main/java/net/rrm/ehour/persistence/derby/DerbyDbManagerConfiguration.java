package net.rrm.ehour.persistence.derby;

import net.rrm.ehour.appconfig.DatabaseType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DerbyDbManagerConfiguration {
    private static final Logger LOGGER = Logger.getLogger(DerbyDbManagerConfiguration.class);

    private static final DerbyDbManager DUMMY_MANAGER = new DerbyDbManager() {
    };

    @Value("${ehour.database}")
    private String databaseName;

    @Bean
    public DerbyDbManager createDerbyDbManager() {
        if (DatabaseType.DERBY.isDatabase(databaseName)) {
            LOGGER.info("Derby db management created");
            return new DerbyDbManagerImpl();
        } else {
            return DUMMY_MANAGER;
        }
    }
}
