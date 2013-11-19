package net.rrm.ehour.persistence.backup.dao

import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserDepartmentObjectMother
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertNotNull

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 19, 2010 - 12:11:37 AM
 */
class RestoreDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
  @Autowired
  private RestoreDao importDao

  @Test
  void shouldPersist()
  {
    def userDep = UserDepartmentObjectMother.createUserDepartment()
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
    def values = BackupEntityType.reverseOrderedValues()

    def delete = { if (it.domainObjectClass != null) { importDao.delete it.domainObjectClass }}
    values.each(delete)
  }

}
