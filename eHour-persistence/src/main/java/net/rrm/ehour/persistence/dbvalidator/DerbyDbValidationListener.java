package net.rrm.ehour.persistence.dbvalidator;

import net.rrm.ehour.config.PersistenceConfig;
import net.rrm.ehour.persistence.datasource.SupportedDatabases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
public class DerbyDbValidationListener  {
    @Autowired
    private DataSource dataSource;

    @Value("${ehour.database}")
    private String databaseType;

    @PostConstruct
    public void onApplicationEvent() {
        if (SupportedDatabases.DERBY.name().equalsIgnoreCase(databaseType)) {
            DerbyDbValidator validator = new DerbyDbValidator(PersistenceConfig.DB_VERSION, dataSource);
            validator.checkDatabaseState();
        }
    }
}
