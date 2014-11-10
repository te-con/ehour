package net.rrm.ehour.init;

import net.rrm.ehour.user.service.UserDepartmentMigrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Migrate user departments in the user from one-to-many to many-to-many relation
 */
@Service
public class UserDepartmentMigratorTrigger {
    private final UserDepartmentMigrator migrator;

    @Autowired
    public UserDepartmentMigratorTrigger(UserDepartmentMigrator migrator) {
        this.migrator = migrator;
    }

    @PostConstruct
    public void postConstruct() {
        migrator.migrate();
    }

}
