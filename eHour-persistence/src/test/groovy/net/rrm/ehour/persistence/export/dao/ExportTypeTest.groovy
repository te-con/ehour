package net.rrm.ehour.persistence.export.dao

import org.junit.Assert
import org.junit.Test

/**
 * Created by IntelliJ IDEA.
 * User: thies
 * Date: 11/23/10
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
class ExportTypeTest
{

  @Test
  void shouldReturnOrderedTypes()
  {
    def values = ExportType.orderedValues()

    Assert.assertEquals 0, values[0].order
    Assert.assertEquals 1, values[1].order
    Assert.assertEquals 2, values[2].order


  }

}
