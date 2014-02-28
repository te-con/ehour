package net.rrm.ehour.backup.service

import net.rrm.ehour.backup.domain.ParseSession
import net.rrm.ehour.persistence.backup.dao.BackupEntityType
import org.junit.Before
import org.junit.Test

import static junit.framework.TestCase.assertFalse

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 12/10/10 - 12:26 AM
 */
class ParseSessionTest {
  ParseSession session;

  @Before
  void setUp() {
    session = new ParseSession();
  }

  @Test
  void shouldNotBeImportableWithGlobalErrors() {
    session.globalError = true
    assertFalse session.importable
  }

  @Test
  void shouldNotBeImportableWhenImported() {
    session.imported = true
    assertFalse session.importable
  }

  @Test
  void shouldNotBeImportableWhenErrorOccured() {
    session.addError BackupEntityType.USER_ROLE, "fe"
    assertFalse session.importable
  }
}
