package net.rrm.ehour.init;

import net.rrm.ehour.persistence.user.dao.UserDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * In version < 0.9.1 there was an issue with the PM role, it was not deleted properly.
 * This class is a cleanup job, when eHour is started it deletes the PM userrole when a
 * user is not listed as PM in a project.
 */

@Service
public class EmptyPmRoleDeletor {
    private static final Logger LOGGER = Logger.getLogger(EmptyPmRoleDeletor.class);

    @Autowired
    private UserDao userDao;

    @PostConstruct
    public void cleanUpPmRoles() {
        LOGGER.info("Cleaning up users with ProjectManagement roles but without project...");
        userDao.deletePmWithoutProject();
    }
}
