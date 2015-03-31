package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.backup.config.EhourBackupConfig;
import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.DatabaseTruncater;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.task.TaskExecutor;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 6:02:34 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class RestoreServiceImplTest {
    @Mock
    private ConfigurationDao configurationDao;

    @Mock
    private DatabaseTruncater truncater;

    @Mock
    private ConfigurationParserDao configurationParserDao;

    @Mock
    private EntityParserDao entityParserDao;

    @Mock
    private TaskExecutor taskExecutor;

    private BackupConfig backupConfig;

    private UserRoleParserDaoValidatorImpl userRoleParserDao;

    private RestoreServiceImpl restoreService;

    private EhourConfigStub configStub;

    @Before
    public void setUp() {
        userRoleParserDao = new UserRoleParserDaoValidatorImpl();

        backupConfig = new EhourBackupConfig();

        configStub = new EhourConfigStub();
        restoreService = new RestoreServiceImpl(configurationDao, configurationParserDao, entityParserDao, truncater, configStub, backupConfig, taskExecutor);
        restoreService.setConfigurationDao(configurationDao);
        restoreService.setDatabaseTruncater(truncater);

        when(entityParserDao.persist(any(User.class))).thenReturn(10);
        when(entityParserDao.persist(any(UserRole.class))).thenReturn("ADMIN");
        when(entityParserDao.persist(any(UserDepartment.class))).thenReturn(2);
    }
/*
    @Test
    public void shouldPrepareImport() throws IOException {
        Configuration configuration = new Configuration("version", "0.8.3");

        when(configurationDao.findById("version")).thenReturn(configuration);

        String file = "src/test/resources/import/import_data.xml";
        String xml = FileUtils.readFileToString(new File(file));

        ParseSession session = new ParseSession();
        restoreService.validateDatabaseBackupFile(session, xml);

        assertTrue(session.isImportable());
    }

    @Test
    public void shouldFailOnPrepareImportForWrongDb() throws IOException {
        Configuration configuration = new Configuration("version", "0.8.2");

        when(configurationDao.findById("version")).thenReturn(configuration);

        String file = "src/test/resources/import/import_data.xml";
        String xml = FileUtils.readFileToString(new File(file));
        ParseSession session = new ParseSession();
        restoreService.validateDatabaseBackupFile(session, xml);

        assertFalse(session.isImportable());
        assertTrue(session.getGlobalErrorMessage().contains("version"));
    }*/

    @Test
    public void shouldImport() throws IOException, ImportException {
        Configuration version = new Configuration("version", "0.8.3");

        when(configurationDao.findById("version")).thenReturn(version);

        File file = new File("src/test/resources/import/import_data_full.xml");
        String tempPath = FileUtils.getTempDirectoryPath();
        File destFile = new File(tempPath + "/tmp.xml");
        FileUtils.copyFile(file, destFile);

        ParseSession session = new ParseSession();
        session.setFilename(destFile.getAbsolutePath());

        restoreService.importDatabase(session);

        assertFalse(session.isImportable());
        assertFalse(destFile.exists());

        verify(entityParserDao, times(10)).persist(any(DomainObject.class));
    }

    @Test
    public void shouldNotImportInDemoMode() throws IOException, ImportException {
        Configuration version = new Configuration("version", "0.8.3");

        when(configurationDao.findById("version")).thenReturn(version);

        File file = new File("src/test/resources/import/import_data_full.xml");
        String tempPath = FileUtils.getTempDirectoryPath();
        File destFile = new File(tempPath + "/tmp.xml");
        FileUtils.copyFile(file, destFile);

        ParseSession session = new ParseSession();

        session.setFilename(destFile.getAbsolutePath());

        configStub.setDemoMode(true);

        restoreService.importDatabase(session);

        assertFalse(session.isImportable());

        assertFalse(destFile.exists());
        assertEquals(0, userRoleParserDao.getFindUserCount());
    }
}
