package net.rrm.ehour.backup.service.backup;

import com.google.common.collect.Lists;
import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.backup.config.EhourBackupConfig;
import net.rrm.ehour.config.ConfigurationItem;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.backup.dao.BackupDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseBackupServiceImplTest {
    @Mock
    private BackupDao exportDao;

    @Mock
    private ConfigurationService configurationService;

    private BackupConfig backupConfig;

    private DatabaseBackupServiceImpl service;

    @Before
    public void setUp() {
        backupConfig = new EhourBackupConfig();
        service = new DatabaseBackupServiceImpl(exportDao, configurationService, backupConfig);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldProduceXml() {
        Map<String, Object> map = new HashMap<>();
        map.put("ASSIGNMENT_ID", 1);
        map.put("ENTRY_DATE", new Date());
        map.put("COMMENT", "\uC3BC and \uC3B6 and <&> für");

        List<Map<String, Object>> rows = Lists.newArrayList(map);
        when(exportDao.findAll("TIMESHEET_ENTRY")).thenReturn(rows);

        EhourConfigStub configuration = new EhourConfigStub();
        configuration.setVersion("0.9");

        when(configurationService.getConfiguration()).thenReturn(configuration);

        List<Configuration> configurationList = Lists.newArrayList(new Configuration(ConfigurationItem.AVAILABLE_TRANSLATIONS.getDbField(), "nl"));
        when(configurationService.findAllConfiguration()).thenReturn(configurationList);

        byte[] xmlBytes = service.exportDatabase();
        String xml = new String(xmlBytes);

        assertThat(xml, containsString("0.9"));
        assertThat(xml, containsString("TIMESHEET_ENTRY"));
        assertThat(xml, containsString("CONFIG"));

        assertTrue(xml.startsWith("<?xml version="));
    }
}
