package net.rrm.ehour.backup.service;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.restore.ConfigurationParserDao;
import net.rrm.ehour.backup.service.restore.DomainObjectParserDao;
import net.rrm.ehour.backup.service.restore.UserRoleParserDaoValidatorImpl;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 6:02:34 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class ImportServiceImplTest {
    @Mock
    private ConfigurationDao configurationDao;
    @Mock
    private DatabaseTruncater truncater;
    @Mock
    private ConfigurationParserDao configurationParserDao;

    @Mock
    private DomainObjectParserDao domainObjectParserDao;

    private UserRoleParserDaoValidatorImpl userRoleParserDao;

    private RestoreServiceImpl importService;

    private EhourConfigStub configStub;

    @Before
    public void setUp() {
        userRoleParserDao = new UserRoleParserDaoValidatorImpl();

        configStub = new EhourConfigStub();
        importService = new RestoreServiceImpl(configurationDao, configurationParserDao, domainObjectParserDao, userRoleParserDao, truncater, configStub);
        importService.setConfigurationDao(configurationDao);
        importService.setDatabaseTruncater(truncater);

        when(domainObjectParserDao.persist(any(User.class))).thenReturn(10);
    }

    @Test
    public void shouldPrepareImport() throws IOException {
        Configuration configuration = new Configuration("version", "0.8.3");

        when(configurationDao.findById("version")).thenReturn(configuration);

        String file = "src/test/resources/import/import_data.xml";
        String xml = FileUtils.readFileToString(new File(file));
        ParseSession status = importService.prepareImportDatabase(xml);

        assertTrue(status.isImportable());
    }

    @Test
    public void shouldFailOnPrepareImportForWrongDb() throws IOException {
        Configuration configuration = new Configuration("version", "0.8.2");

        when(configurationDao.findById("version")).thenReturn(configuration);

        String file = "src/test/resources/import/import_data.xml";
        String xml = FileUtils.readFileToString(new File(file));
        ParseSession session = importService.prepareImportDatabase(xml);

        assertFalse(session.isImportable());
        assertTrue(session.getGlobalErrorMessage().contains("version"));
    }

    @Test
    public void shouldImport() throws IOException {
        Configuration version = new Configuration("version", "0.8.3");

        when(configurationDao.findById("version")).thenReturn(version);

        File file = new File("src/test/resources/import/import_data_full.xml");
        String tempPath = FileUtils.getTempDirectoryPath();
        File destFile = new File(tempPath + "/tmp.xml");
        FileUtils.copyFile(file, destFile);

        ParseSession session = new ParseSession();

        session.setFilename(destFile.getAbsolutePath());

        ParseSession status = importService.importDatabase(session);

        assertFalse(status.isImportable());

        assertFalse(destFile.exists());
        assertEquals(6, userRoleParserDao.getFindUserCount());
    }

    @Test
    public void shouldNotImportInDemoMode() throws IOException {
        Configuration version = new Configuration("version", "0.8.3");

        when(configurationDao.findById("version")).thenReturn(version);

        File file = new File("src/test/resources/import/import_data_full.xml");
        String tempPath = FileUtils.getTempDirectoryPath();
        File destFile = new File(tempPath + "/tmp.xml");
        FileUtils.copyFile(file, destFile);

        ParseSession session = new ParseSession();

        session.setFilename(destFile.getAbsolutePath());

        configStub.setDemoMode(true);

        ParseSession status = importService.importDatabase(session);

        assertFalse(status.isImportable());

        assertFalse(destFile.exists());
        assertEquals(0, userRoleParserDao.getFindUserCount());
    }
}
