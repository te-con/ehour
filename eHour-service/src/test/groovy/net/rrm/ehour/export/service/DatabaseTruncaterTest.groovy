package net.rrm.ehour.export.service

import net.rrm.ehour.persistence.export.dao.ExportType
import net.rrm.ehour.persistence.export.dao.ImportDao
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Matchers.anyObject
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 1/19/11 - 8:54 PM
 */
class DatabaseTruncaterTest
{
  @Mock
  ImportDao importDao

  DatabaseTruncater truncater

  @Before
  void setUp()
  {
    MockitoAnnotations.initMocks this
    truncater = new DatabaseTruncater()
    truncater.importDao = importDao
  }

  @Test
  void shouldTruncate()
  {
    truncater.truncateDatabase()

    verify(importDao, times(ExportType.reverseOrderedValues()[0].order + 4)).delete anyObject()

  }
}
