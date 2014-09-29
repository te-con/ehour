package net.rrm.ehour.persistence.user.dao

import net.rrm.ehour.domain.Activity
import net.rrm.ehour.domain.Project
import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 14, 2010 - 10:41:38 PM
 */
class UserDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
    @Autowired
    private UserDao userDao;

    @Test
    void shouldFindUsers()
    {
        def results = userDao.findActiveUsers();
        assertEquals(4, results.size());
    }

    @Test
    void shouldFindById()
    {
        def user = userDao.findById(1);

        assertEquals("thies", user.username)
    }

    @Test
    void shouldFindByUsername()
    {
        def user = userDao.findByUsername("thies")

        assertEquals("thies", user.username)
    }

    @Test
    void shouldPersist() {
        User user = UserObjectMother.createUser()
        user.setUserId 5

        Activity activity = new Activity(assignedUser: user, id: 1, name: "AA", code: "aa", project: new Project(1))

        def activities = [activity] as Set

        user.setActivities(activities)
        userDao.persist(user)

        assertNotNull(user.userId)
    }

    @Test
    void shouldFindAllActiveUsers()
    {
        def results = userDao.findActiveUsers();

        assertEquals(4, results.size());
    }

    @Test
    void shouldDeletePmWithoutProject()
    {
        userDao.deletePmWithoutProject();

        def user = userDao.findById(2)

        user.userRoles.each {if (it.role == "PROJECT_MANAGER") fail}
    }
}