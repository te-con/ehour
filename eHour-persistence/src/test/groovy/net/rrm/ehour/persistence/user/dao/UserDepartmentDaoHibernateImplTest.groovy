package net.rrm.ehour.persistence.user.dao

import net.rrm.ehour.domain.UserDepartmentObjectMother
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 14, 2010 - 11:46:46 PM
 */
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
    userDepartmentDao.deleteOnId(20);

    def dept = userDepartmentDao.findById(20);
    assertNull(dept);
  }

  @Test
  void shouldFindById()
  {
    def dept = userDepartmentDao.findById(20)
    assertEquals("EHOUR", dept.name)
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
    def department = UserDepartmentObjectMother.createUserDepartment()

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
