package net.rrm.ehour.persistence.backup.dao

import net.rrm.ehour.domain.User
<<<<<<< HEAD:eHour-persistence/src/test/groovy/net/rrm/ehour/persistence/backup/dao/RestoreDaoHibernateImplTest.groovy
import net.rrm.ehour.domain.UserDepartmentObjectMother
=======
>>>>>>> 9f7e93a... EHV-52 - changed concept, User will be combination of db user and LDAP and UserService combines UserDao and LDAP - always enriching the User object:eHour-persistence/src/test/groovy/net/rrm/ehour/persistence/export/dao/ImportDaoHibernateImplTest.groovy
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
<<<<<<< HEAD:eHour-persistence/src/test/groovy/net/rrm/ehour/persistence/backup/dao/RestoreDaoHibernateImplTest.groovy
  void shouldPersist()
  {
    def userDep = UserDepartmentObjectMother.createUserDepartment()
    userDep.departmentId = null

    importDao.persist userDep

    assertNotNull userDep.departmentId
  }

  @Test
=======
>>>>>>> 9f7e93a... EHV-52 - changed concept, User will be combination of db user and LDAP and UserService combines UserDao and LDAP - always enriching the User object:eHour-persistence/src/test/groovy/net/rrm/ehour/persistence/export/dao/ImportDaoHibernateImplTest.groovy
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
