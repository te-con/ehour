package net.rrm.ehour.persistence.export.dao

import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserDepartmentMother
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import static org.junit.Assert.assertNotNull

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 19, 2010 - 12:11:37 AM
 */
class ImportDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
  @Autowired
  private ImportDao importDao

  @Test
  void shouldPersist()
  {
    def userDep = UserDepartmentMother.createUserDepartment()
    userDep.departmentId = null

    importDao.persist userDep

    assertNotNull userDep.departmentId
  }

  @Test
  void shouldFind()
  {
    def user = importDao.find(3, User.class)

    assertNotNull user
  }

  @Test
  void shouldDelete()
  {
    def values = ExportType.reverseOrderedValues()

    def delete = { if (it.domainObjectClass != null) { importDao.delete it.domainObjectClass }}
    values.each(delete)
  }

}
