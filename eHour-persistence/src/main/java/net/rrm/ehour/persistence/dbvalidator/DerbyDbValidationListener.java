package net.rrm.ehour.persistence.dbvalidator;

import net.rrm.ehour.config.PersistenceConfig;
import net.rrm.ehour.persistence.appconfig.DerbyConnectionProvider;
import net.rrm.ehour.persistence.datasource.Database;
import net.rrm.ehour.persistence.datasource.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DerbyDbValidationListener implements ApplicationListener<ContextStartedEvent> {
    @Autowired
    private DatabaseConfig databaseConfig;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        if (databaseConfig.databaseType == Database.DERBY) {
            DataSource dataSource = DerbyConnectionProvider.dataSource;
            DerbyDbValidator validator = new DerbyDbValidator(PersistenceConfig.DB_VERSION, dataSource);
            validator.checkDatabaseState();
        }
    }
}
