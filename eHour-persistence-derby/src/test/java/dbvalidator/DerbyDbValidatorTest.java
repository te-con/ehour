package dbvalidator;

import net.rrm.ehour.persistence.dbvalidator.DerbyDbValidator;
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/11/11 - 12:11 AM
 */
public class DerbyDbValidatorTest  {
    @Test
    public void shouldCreate() {
        EmbeddedConnectionPoolDataSource dataSource = new EmbeddedConnectionPoolDataSource();
        dataSource.setDatabaseName("memory:db");

        DerbyDbValidator validator = new DerbyDbValidator("99", dataSource);
        DerbyDbValidator.DdlType state = validator.checkDatabaseState();

        assertEquals(DerbyDbValidator.DdlType.CREATE_TABLE, state);
    }
}
