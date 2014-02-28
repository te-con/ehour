package net.rrm.ehour.backup.service

import net.rrm.ehour.backup.domain.ParseSession
import net.rrm.ehour.backup.service.restore.ConfigurationParserDao
import net.rrm.ehour.backup.service.restore.DomainObjectParserDaoValidatorImpl
import net.rrm.ehour.backup.service.restore.UserRoleParserDaoValidatorImpl
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.domain.Configuration
import net.rrm.ehour.persistence.config.dao.ConfigurationDao
import org.apache.commons.io.FileUtils
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 13, 2010 - 6:02:34 PM
 */
class ImportServiceImplTest
{
  RestoreServiceImpl importService

  @Mock
  ConfigurationDao configurationDao

  @Mock
  DatabaseTruncater truncater

  @Mock
  ConfigurationParserDao configurationParserDao

  @Before
  void setUp()
  {
    MockitoAnnotations.initMocks this

    importService = new RestoreServiceImpl()
    importService.configurationDao = configurationDao
    importService.databaseTruncater = truncater
  }

  @Test
  void shouldPrepareImport()
  {
    def configuration = new Configuration("version", "0.8.3")

    when(configurationDao.findById("version")).thenReturn(configuration)

    def file = "src/test/resources/import/import_data.xml"
    def xml = new File(file).text
    ParseSession status = importService.prepareImportDatabase(xml)

    assertTrue status.importable
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
    assertTrue session.globalErrorMessage.contains("version")
  }

  @Test
  void shouldImport()
  {
    def version = new Configuration("version", "0.8.3")

    when(configurationDao.findById("version")).thenReturn(version)

    def file = new File("src/test/resources/import/import_data_full.xml");
    def tempPath = FileUtils.getTempDirectoryPath()
    def destFile = new File(tempPath + "/tmp.xml");
    FileUtils.copyFile(file, destFile)

    ParseSession session = new ParseSession(filename: destFile.getAbsolutePath())

    def userVal = new UserRoleParserDaoValidatorImpl()

    importService.domainObjectParserDao = new DomainObjectParserDaoValidatorImpl()
    importService.userRoleParserDao = userVal
    importService.configurationParserDao = configurationParserDao
    importService.ehourConfig = new EhourConfigStub()

    def status = importService.importDatabase(session)

    assertFalse status.importable

    assertFalse destFile.exists()
    assert userVal.findUserCount == 6
  }

    @Test
    void shouldNotImportInDemoMode()
    {
        def version = new Configuration("version", "0.8.3")

        when(configurationDao.findById("version")).thenReturn(version)

        def file = new File("src/test/resources/import/import_data_full.xml");
        def tempPath = FileUtils.getTempDirectoryPath()
        def destFile = new File(tempPath + "/tmp.xml");
        FileUtils.copyFile(file, destFile)

        ParseSession session = new ParseSession(filename: destFile.getAbsolutePath())

        def userVal = new UserRoleParserDaoValidatorImpl()

        importService.domainObjectParserDao = new DomainObjectParserDaoValidatorImpl()
        importService.userRoleParserDao = userVal
        importService.configurationParserDao = configurationParserDao
        def config = new EhourConfigStub()
        config.setDemoMode(true)
        importService.ehourConfig = config

        def status = importService.importDatabase(session)

        assertFalse status.importable

        assertFalse destFile.exists()
        assert userVal.findUserCount == 0
    }
}
