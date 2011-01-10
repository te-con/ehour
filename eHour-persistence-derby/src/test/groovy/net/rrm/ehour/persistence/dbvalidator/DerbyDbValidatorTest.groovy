package net.rrm.ehour.persistence.dbvalidator

import net.rrm.ehour.persistence.dbvalidator.DerbyDbValidator.DdlType
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource
import org.junit.Test

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 1/11/11 - 12:11 AM
 */
class DerbyDbValidatorTest {
  @Test
  void shouldCreate() {
    def dataSource = new EmbeddedConnectionPoolDataSource()
    dataSource.setDatabaseName("memory:db")

    def validator = new DerbyDbValidator("99", dataSource)
    def state = validator.checkDatabaseState()

    assert state == DdlType.CREATE_TABLE
  }
}
