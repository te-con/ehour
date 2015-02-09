package net.rrm.ehour.persistence.backup.dao;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserDepartmentObjectMother;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 19, 2010 - 12:11:37 AM
 */
public class RestoreDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Test
    public void shouldPersist() {
        UserDepartment userDep = UserDepartmentObjectMother.createUserDepartment();
        userDep.setDepartmentId(null);

        importDao.persist(userDep);

        assertNotNull(userDep.getDepartmentId());
    }

    @Test
    public void shouldFind() {
        User user = importDao.find(3, User.class);

        assertNotNull(user);
    }

    @Test
    public void shouldDelete() {
        importDao.delete(User.class);

        User user = importDao.find(3, User.class);

        assertNull(user);
    }

    @Autowired
    private RestoreDao importDao;
}
