package net.rrm.ehour.backup.domain;

import junit.framework.TestCase;
import net.rrm.ehour.backup.common.BackupEntityType;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/10/10 - 12:26 AM
 */
public class ParseSessionTest {
    @Before
    public void setUp() {
        session = new ParseSession();
    }

    @Test
    public void shouldNotBeImportableWithGlobalErrors() {
        session.setGlobalError(true);
        TestCase.assertFalse(session.isImportable());
    }

    @Test
    public void shouldNotBeImportableWhenImported() {
        session.setImported(true);
        TestCase.assertFalse(session.isImportable());
    }

    @Test
    public void shouldNotBeImportableWhenErrorOccured() {
        session.addError(mock(BackupEntityType.class), "fe");
        TestCase.assertFalse(session.isImportable());
    }

    public ParseSession getSession() {
        return session;
    }

    public void setSession(ParseSession session) {
        this.session = session;
    }

    private ParseSession session;
}
