package net.rrm.ehour.persistence.dbvalidator;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/11/11 - 12:11 AM
 */
public class DerbyDbValidatorTest  {
    @Test
    public void shouldAlter() throws IOException {
        EmbeddedConnectionPoolDataSource dataSource = new EmbeddedConnectionPoolDataSource();
        dataSource.setDatabaseName("memory:db;create=true");

        DerbyDbValidator prevalidator = new DerbyDbValidator("1.4", dataSource);
        prevalidator.checkDatabaseState();

        DerbyDbValidator validator = new DerbyDbValidator("99", dataSource);
        DerbyDbValidator.DdlType state = validator.checkDatabaseState();

        assertEquals(DerbyDbValidator.DdlType.ALTER_TABLE, state);
    }
}
