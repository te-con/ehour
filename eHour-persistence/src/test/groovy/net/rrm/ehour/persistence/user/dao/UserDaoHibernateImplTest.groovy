package net.rrm.ehour.persistence.user.dao

<<<<<<< HEAD
import net.rrm.ehour.domain.*
=======
import net.rrm.ehour.domain.Activity
import net.rrm.ehour.domain.Project
import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserMother
>>>>>>> 9f7e93a... EHV-52 - changed concept, User will be combination of db user and LDAP and UserService combines UserDao and LDAP - always enriching the User object
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

<<<<<<< HEAD
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
=======
import static junit.framework.Assert.assertEquals
import static junit.framework.Assert.assertNotNull
>>>>>>> 9f7e93a... EHV-52 - changed concept, User will be combination of db user and LDAP and UserService combines UserDao and LDAP - always enriching the User object

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
  void shouldPersist()
  {
<<<<<<< HEAD
    def org = UserDepartmentObjectMother.createUserDepartment()

    User user = UserObjectMother.createUser()
=======
    User user = UserMother.createUser()
>>>>>>> 9f7e93a... EHV-52 - changed concept, User will be combination of db user and LDAP and UserService combines UserDao and LDAP - always enriching the User object
    user.setUserId 5

    Activity activity = new Activity(assignedUser: user, id: 1, project: new Project(1),)

    def activities = [activity] as Set

    user.setActivities(activities)
    userDao.persist(user)

    assertNotNull(user.userId)
  }

  @Test
<<<<<<< HEAD
  void shouldFindUsersForDepartments()
  {
    def ids = [new UserDepartment(10)]

    def results = userDao.findUsersForDepartments(ids, false);

    assertEquals(5, results.size());
  }

  @Test
=======
>>>>>>> 9f7e93a... EHV-52 - changed concept, User will be combination of db user and LDAP and UserService combines UserDao and LDAP - always enriching the User object
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

