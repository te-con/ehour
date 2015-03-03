package net.rrm.ehour.persistence.dbvalidator;

import net.rrm.ehour.config.PersistenceConfig;
import net.rrm.ehour.persistence.hibernate.Database;
import net.rrm.ehour.persistence.hibernate.DatabaseConfig;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DerbyDbValidationListener implements ApplicationListener<ContextStartedEvent> {
    @Autowired
    private DatabaseConfig databaseConfig;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        if (databaseConfig.databaseType == Database.DERBY) {
            DataSource dataSource = SessionFactoryUtils.getDataSource(sessionFactory);
            DerbyDbValidator validator = new DerbyDbValidator(PersistenceConfig.DB_VERSION, dataSource);
            validator.checkDatabaseState();
        }
    }
}
