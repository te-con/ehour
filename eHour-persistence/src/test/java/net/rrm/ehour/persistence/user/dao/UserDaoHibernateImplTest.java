package net.rrm.ehour.persistence.user.dao;

import com.google.common.collect.Sets;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserObjectMother;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 14, 2010 - 10:41:38 PM
 */
public class UserDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void shouldFindUsers() {
        List<User> results = userDao.findActiveUsers();
        assertEquals(4, results.size());
    }

    @Test
    public void shouldFindById() {
        User user = userDao.findById(1);

        assertEquals("thies", user.getUsername());
    }

    @Test
    public void shouldFindByUsername() {
        User user = userDao.findByUsername("thies");

        assertEquals("thies", user.getUsername());
    }

    @Test
    public void shouldPersist() {
        User user = UserObjectMother.createUser();
        user.setUserId(5);

        Activity activity = new Activity(user, new Project(1));
        activity.setName("tr");
        user.setActivities(Sets.newHashSet(activity));

        userDao.persist(user);

        assertNotNull(user.getUserId());
    }

    @Test
    public void shouldFindAllActiveUsers() {
        List<User> results = userDao.findActiveUsers();

        assertEquals(4, results.size());
    }
}
