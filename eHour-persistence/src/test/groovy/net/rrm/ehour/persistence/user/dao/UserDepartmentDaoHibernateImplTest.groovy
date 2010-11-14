package net.rrm.ehour.persistence.user.dao

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 14, 2010 - 11:46:46 PM
 */

import net.rrm.ehour.domain.UserDepartmentMother
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import static org.junit.Assert.*

class UserDepartmentDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
  @Autowired
  UserDepartmentDao userDepartmentDao;

  @Test
  void shouldDelete()
  {
    def dept = userDepartmentDao.findById(2);
    assertNotNull(dept);

    userDepartmentDao.delete(dept);

    dept = userDepartmentDao.findById(2);
    assertNull(dept);
  }

  @Test
  void shouldFindById()
  {
    def dept = userDepartmentDao.findById(2)
    assertEquals("DUMMY DEPT", dept.name)
  }

  @Test
  void shouldGetAllDepartments()
  {
    def depts = userDepartmentDao.findAll()
    assertEquals(2, depts.size())
  }

  @Test
  void shouldPersist()
  {
    def department = UserDepartmentMother.createUserDepartment()

    userDepartmentDao.persist(department)

    def dept2 = userDepartmentDao.findById(department.departmentId)

    assertEquals(department.name, dept2.name)
  }

  @Test
  void shouldFindByNameAndCode()
  {
    assertNotNull(userDepartmentDao.findOnNameAndCode("TE-CON", "TEC"))
  }
}
