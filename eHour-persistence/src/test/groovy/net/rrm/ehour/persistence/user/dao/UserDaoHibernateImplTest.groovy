package net.rrm.ehour.persistence.user.dao

import net.rrm.ehour.domain.*
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import net.rrm.ehour.util.EhourConstants
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
  void shouldPersist()
  {
    def org = UserDepartmentObjectMother.createUserDepartment()

    User user = UserObjectMother.createUser()
    user.setUserId 5

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
    def ids = [new UserDepartment(10)]

    def results = userDao.findUsersForDepartments(ids, false);

    assertEquals(5, results.size());
  }

  @Test
  void shouldFindAllActiveUsers()
  {
    def results = userDao.findActiveUsers();

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

