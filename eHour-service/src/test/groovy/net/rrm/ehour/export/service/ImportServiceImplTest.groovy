package net.rrm.ehour.export.service

import net.rrm.ehour.domain.Configuration
import net.rrm.ehour.persistence.config.dao.ConfigurationDao
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 13, 2010 - 6:02:34 PM
 */
class ImportServiceImplTest
{
  ImportServiceImpl importService

  @Mock
  ConfigurationDao configurationDao


  @Before
  void setUp()
  {
    MockitoAnnotations.initMocks this

    importService = new ImportServiceImpl()
    importService.setConfigurationDao configurationDao
  }

  @Test
  void shouldPrepareImport()
  {
    def configuration = new Configuration("version", "0.8.3")

    when(configurationDao.findById("version")).thenReturn(configuration)

    def file = "src/test/resources/import/import_data.xml"
    def xml = new File(file).text
    def status = importService.prepareImportDatabase(xml)
    status.deleteFile()
  }


  @Test
  void shouldFailOnPrepareImportForWrongDb()
  {
    def configuration = new Configuration("version", "0.8.2")

    when(configurationDao.findById("version")).thenReturn(configuration)

    def file = "src/test/resources/import/import_data.xml"
    def xml = new File(file).text
    def session = importService.prepareImportDatabase(xml)
    assertFalse session.importable
    assertEquals "import.error.invalidDatabaseVersion", session.globalErrorMessage
  }

  @Test
  void shouldValidateImport()
  {
    def configuration = new Configuration("version", "0.8.3")

    when(configurationDao.findById("version")).thenReturn(configuration)

    def file = "src/test/resources/import/import_data_full.xml"
    def xml = new File(file).text
    def status = importService.prepareImportDatabase(xml)
    assertFalse status.hasErrors()

    status.deleteFile()
  }
}
