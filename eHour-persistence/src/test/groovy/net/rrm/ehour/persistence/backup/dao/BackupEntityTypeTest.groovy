package net.rrm.ehour.persistence.backup.dao

import net.rrm.ehour.domain.User
import org.junit.Assert
import org.junit.Test

/**
 * Created by IntelliJ IDEA.
 * User: thies
 * Date: 11/23/10
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
class BackupEntityTypeTest
{

  @Test
  void shouldReturnOrderedTypes()
  {
    def values = BackupEntityType.orderedValues()

    Assert.assertEquals 0, values[0].order
    Assert.assertEquals 1, values[1].order
    Assert.assertEquals 2, values[2].order
  }

  @Test
  void shouldReturnReverserOrderedTypes()
  {
    def values = BackupEntityType.reverseOrderedValues();

    assert 8 == values[0].order
    assert 7 == values[1].order
  }

  @Test
  void shouldFetchForClazz()
  {
<<<<<<< HEAD:eHour-persistence/src/test/groovy/net/rrm/ehour/persistence/backup/dao/BackupEntityTypeTest.groovy
    Assert.assertEquals BackupEntityType.USER_DEPARTMENT, BackupEntityType.forClass(UserDepartment.class)
=======
    Assert.assertEquals ExportType.USERS, ExportType.forClass(User.class)
>>>>>>> 9f7e93a... EHV-52 - changed concept, User will be combination of db user and LDAP and UserService combines UserDao and LDAP - always enriching the User object:eHour-persistence/src/test/groovy/net/rrm/ehour/persistence/export/dao/ExportTypeTest.groovy
  }

}
