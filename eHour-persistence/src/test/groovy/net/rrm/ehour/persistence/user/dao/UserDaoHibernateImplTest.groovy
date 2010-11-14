package net.rrm.ehour.persistence.user.dao

import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import net.rrm.ehour.util.EhourConstants
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import static junit.framework.Assert.assertEquals
import static junit.framework.Assert.assertNotNull
import net.rrm.ehour.domain.*

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 14, 2010 - 10:41:38 PM
 */
class UserDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
  @Autowired
  private UserDao userDao;

  @Test
  void shouldFindUsersByPattern()
  {
    def results;

    results = userDao.findUsersByNameMatch("thies", true);
    assertEquals(1, results.size());

    results = userDao.findUsersByNameMatch("ede", true);
    assertEquals(2, results.size());

    results = userDao.findUsersByNameMatch("zed", false);
    assertEquals(3, results.size());

    results = userDao.findUsersByNameMatch("in", false);
    assertEquals(2, results.size());

    results = userDao.findUsersByNameMatch("zed", true);
    assertEquals(2, results.size());

    results = userDao.findUsersByNameMatch(null, true);
    assertEquals(4, results.size());

  }

  @Test
  void shouldFindUsers()
  {
    def results = userDao.findAllActiveUsers();
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
    def org = UserDepartmentMother.createUserDepartment()

    User user = UserMother.createUser()
    user.setUserId null

    ProjectAssignment pa = new ProjectAssignment(user: user, assignmentId: 1, project: new Project(1), assignmentType: new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE))

    def assignments = [pa] as Set

    user.setUserDepartment(org)
    user.setProjectAssignments(assignments)
    userDao.persist(user)

    assertNotNull(user.userId)
  }

  @Test
  void shouldFindUsersForDepartments()
  {
    def ids = [new UserDepartment(1)]

    def results = userDao.findUsersForDepartments("in", ids, false);

    assertEquals(2, results.size());
  }

  @Test
  void shouldFindAllActiveUsers()
  {
    def results = userDao.findAllActiveUsers();

    assertEquals(4, results.size());
  }

  @Test
  void shouldFindAllActiveUsersWithEmailSet()
  {
    def results = userDao.findAllActiveUsersWithEmailSet();

    assertEquals(2, results.size());
  }

  @Test
  void shouldDeletePmWithoutProject()
  {
    userDao.deletePmWithoutProject();

    def user = userDao.findById(2)

    user.userRoles.each {if (it.role == "PROJECT_MANAGER") fail}
  }
}

