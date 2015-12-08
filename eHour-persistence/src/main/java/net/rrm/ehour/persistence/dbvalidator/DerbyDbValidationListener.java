package net.rrm.ehour.persistence.dbvalidator;

import net.rrm.ehour.config.PersistenceConfig;
import net.rrm.ehour.persistence.database.Database;
import net.rrm.ehour.persistence.database.DatabaseConfig;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
public class DerbyDbValidationListener  {
    @Autowired
    private DatabaseConfig databaseConfig;

    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    public void onApplicationEvent() {
        if (databaseConfig.databaseType == Database.DERBY) {
            DataSource dataSource = SessionFactoryUtils.getDataSource(sessionFactory);
            DerbyDbValidator validator = new DerbyDbValidator(PersistenceConfig.DB_VERSION, dataSource);
            validator.checkDatabaseState();
        }
    }
}
