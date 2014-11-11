package net.rrm.ehour.user.service;

import net.rrm.ehour.data.LegacyUserDepartment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.persistence.user.dao.UserDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Migrate user departments in the user from one-to-many to many-to-many relation
 */
@Service
public class UserDepartmentMigrator {
    private static final Logger LOGGER = Logger.getLogger(UserDepartmentMigrator.class);

    private UserDao userDao;

    @Autowired
    public UserDepartmentMigrator(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void migrate() {
        LOGGER.info("Migrating user departments in user to a more flexible structure...");

        List<LegacyUserDepartment> legacyUserDepartments = userDao.findLegacyUserDepartments();

        int count = 0;

        for (LegacyUserDepartment legacyUserDepartment : legacyUserDepartments) {
            User user = userDao.findById(legacyUserDepartment.getUserId());
            user.clearLegacyDepartment();
            user.getUserDepartments().clear();
            user.getUserDepartments().add(new UserDepartment(legacyUserDepartment.getDepartmentId()));

            userDao.persist(user);
            count++;
        }

        LOGGER.info(String.format("%d users found with legacy user department, all converted.", count));
    }
}
