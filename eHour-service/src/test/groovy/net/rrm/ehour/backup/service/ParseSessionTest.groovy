package net.rrm.ehour.backup.service

import junit.framework.Assert
import net.rrm.ehour.persistence.backup.dao.BackupEntityType
import org.junit.Before
import org.junit.Test

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
    Assert.assertFalse session.importable
  }

  @Test
  void shouldNotBeImportableWhenImported() {
    session.imported = true
    Assert.assertFalse session.importable
  }

  @Test
  void shouldNotBeImportableWhenErrorOccured() {
    session.addError BackupEntityType.USER_ROLE, "fe"
    Assert.assertFalse session.importable
  }
}
