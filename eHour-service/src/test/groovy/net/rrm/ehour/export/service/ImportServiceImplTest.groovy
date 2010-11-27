package net.rrm.ehour.export.service

import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.config.service.ConfigurationService
import net.rrm.ehour.persistence.export.dao.ImportDao
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 13, 2010 - 6:02:34 PM
 */
class ImportServiceImplTest
{
  ImportServiceImpl importService

  @Mock
  ConfigurationService configurationService

  @Mock
  ImportDao importDao

  @Before
  void setUp()
  {
    MockitoAnnotations.initMocks this

    importService = new ImportServiceImpl()
    importService.setConfigurationService configurationService
    importService.setImportDao importDao
  }

  @Test
  void shouldPrepareImport()
  {
    def configuration = new EhourConfigStub(version: '0.8.3')

    when(configurationService.configuration).thenReturn configuration

    def file = "src/test/resources/import/import_data.xml"
    def xml = new File(file).text
    importService.prepareImportDatabase(xml)
  }

  @Test(expected = ImportException)
  void shouldFailOnPrepareImportForWrongDb()
  {
    def configuration = new EhourConfigStub(version: '0.8.2')

    when(configurationService.configuration).thenReturn configuration

    def file = "src/test/resources/import/import_data.xml"
    def xml = new File(file).text
    importService.prepareImportDatabase(xml)
  }

  @Test
  void shouldImport()
  {
    def configuration = new EhourConfigStub(version: '0.8.3')

    when(configurationService.configuration).thenReturn configuration

    def file = "src/test/resources/import/import_data_full.xml"
    def xml = new File(file).text
    importService.prepareImportDatabase(xml)
  }
}
