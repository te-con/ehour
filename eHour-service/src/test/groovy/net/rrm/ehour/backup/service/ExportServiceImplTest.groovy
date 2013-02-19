package net.rrm.ehour.backup.service;


import net.rrm.ehour.config.ConfigurationItem
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.config.service.ConfigurationService
import net.rrm.ehour.domain.Configuration
import net.rrm.ehour.persistence.backup.dao.BackupDao
import net.rrm.ehour.persistence.backup.dao.BackupEntityType
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.junit.Assert.assertTrue
import static org.mockito.Mockito.when

class ExportServiceImplTest {
    @Mock
    private BackupDao exportDao;

    @Mock
    private ConfigurationService configurationService

    private DatabaseBackupServiceImpl service;

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)

        service = new DatabaseBackupServiceImpl(backupDao: exportDao, configurationService: configurationService);
    }

    @Test
    void shouldProduceXml() {
        def map = ["ASSIGNMENT_ID": 1, "ENTRY_DATE": new Date()]
        def rows = [map]

        when(exportDao.findForType(BackupEntityType.TIMESHEET_ENTRY)).thenReturn(rows);

        def configuration = new EhourConfigStub(version: 0.9)
        when(configurationService.getConfiguration()).thenReturn(configuration);

        def configurationList = [new Configuration(ConfigurationItem.AVAILABLE_TRANSLATIONS.dbField, "nl")]
        when(configurationService.findAllConfiguration()).thenReturn(configurationList)

        String xml = service.exportDatabase()

        assertTrue xml.contains("0.9")
        assertTrue xml.contains("TIMESHEET_ENTRY")
        assertTrue xml.contains("CONFIG")

        assertTrue(xml.startsWith("<?xml version="))
    }
}
