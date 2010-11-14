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
class UserDaoImplTest extends AbstractAnnotationDaoTest
{
  @Autowired
  private UserDao userDAO;

  @Test
  void shouldFindUsersByPattern()
  {
    def results;

    results = userDAO.findUsersByNameMatch("thies", true);
    assertEquals(1, results.size());

    results = userDAO.findUsersByNameMatch("ede", true);
    assertEquals(2, results.size());

    results = userDAO.findUsersByNameMatch("zed", false);
    assertEquals(3, results.size());

    results = userDAO.findUsersByNameMatch("in", false);
    assertEquals(2, results.size());

    results = userDAO.findUsersByNameMatch("zed", true);
    assertEquals(2, results.size());

    results = userDAO.findUsersByNameMatch(null, true);
    assertEquals(4, results.size());

  }

  @Test
  void shouldFindUsers()
  {
    def results = userDAO.findAllActiveUsers();
    assertEquals(4, results.size());
  }

  @Test
  void shouldFindById()
  {
    def user = userDAO.findById(1);

    assertEquals("thies", user.username)
  }

  @Test
  void shouldFindByUsername()
  {
    def user = userDAO.findByUsername("thies")

    assertEquals("thies", user.username)
  }

  @Test
  public void shouldPersist()
  {
    def org = UserDepartmentMother.createUserDepartment()

    User user = UserMother.createUser()
    user.setUserId null

    ProjectAssignment pa = new ProjectAssignment(user: user, assignmentId: 1, project: new Project(1), assignmentType: new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE))

    def assignments = [pa] as Set

    user.setUserDepartment(org)
    user.setProjectAssignments(assignments)
    userDAO.persist(user)

    assertNotNull(user.userId)
  }

  @Test
  public void shouldFindUsersForDepartments()
  {
    List<UserDepartment> ids = new ArrayList<UserDepartment>();
    ids.add(new UserDepartment(1));

    List<User> results = userDAO.findUsersForDepartments("in", ids, false);

    assertEquals(2, results.size());
  }

  @Test
  public void shouldFindAllActiveUsersWithEmailSet()
  {
    List<User> results = userDAO.findAllActiveUsersWithEmailSet();

    assertEquals(2, results.size());
  }

  @Test
  public void shouldDeletePmWithoutProject()
  {
    userDAO.deletePmWithoutProject();
  }
}
