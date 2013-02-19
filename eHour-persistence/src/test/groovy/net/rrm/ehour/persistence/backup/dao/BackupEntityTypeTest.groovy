package net.rrm.ehour.persistence.backup.dao

import net.rrm.ehour.domain.UserDepartment
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


    assert 10 == values[0].order
    assert 9 == values[1].order
  }

  @Test
  void shouldFetchForClazz()
  {
    Assert.assertEquals BackupEntityType.USER_DEPARTMENT, BackupEntityType.forClass(UserDepartment.class)
  }

}
