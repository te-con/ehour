package net.rrm.ehour.persistence.dbvalidator;

import net.rrm.ehour.config.PersistenceConfig;
import net.rrm.ehour.persistence.datasource.Database;
import net.rrm.ehour.persistence.datasource.DatabaseConfig;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
public class DerbyDbValidationListener implements ApplicationListener<ContextStartedEvent> {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DatabaseConfig databaseConfig;

//    @PostConstruct
//    public void onApplicationEvent() {
//        if (databaseConfig.databaseType == Database.DERBY) {
//            DataSource dataSource = SessionFactoryUtils.getDataSource(sessionFactory);
//            DerbyDbValidator validator = new DerbyDbValidator(PersistenceConfig.DB_VERSION, dataSource);
//            validator.checkDatabaseState();
//        }
//    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        if (databaseConfig.databaseType == Database.DERBY) {
            DataSource dataSource = SessionFactoryUtils.getDataSource(sessionFactory);
            DerbyDbValidator validator = new DerbyDbValidator(PersistenceConfig.DB_VERSION, dataSource);
            validator.checkDatabaseState();
        }
    }
}
