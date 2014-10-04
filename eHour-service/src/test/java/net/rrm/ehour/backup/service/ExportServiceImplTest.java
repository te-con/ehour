package net.rrm.ehour.backup.service;

import com.google.common.collect.Lists;
import net.rrm.ehour.config.ConfigurationItem;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.backup.dao.BackupDao;
import net.rrm.ehour.persistence.backup.dao.BackupEntityType;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ExportServiceImplTest {
    @Mock
    private BackupDao exportDao;

    @Mock
    private ConfigurationService configurationService;
    private DatabaseBackupServiceImpl service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new DatabaseBackupServiceImpl();
        service.setBackupDao(exportDao);
        service.setConfigurationService(configurationService);
    }

    @Test
    public void shouldProduceXml() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ASSIGNMENT_ID", 1);
        map.put("ENTRY_DATE", new Date());
        List<Map<String, Object>> rows = Lists.newArrayList(map);

        when(exportDao.findForType(BackupEntityType.TIMESHEET_ENTRY)).thenReturn(rows);

        EhourConfigStub configuration = new EhourConfigStub();
        configuration.setVersion("0.9");

        when(configurationService.getConfiguration()).thenReturn(configuration);

        ArrayList<Configuration> configurationList = new ArrayList<Configuration>(Arrays.asList(new Configuration(ConfigurationItem.AVAILABLE_TRANSLATIONS.getDbField(), "nl")));
        when(configurationService.findAllConfiguration()).thenReturn(configurationList);

        String xml = service.exportDatabase();

        assertTrue(DefaultGroovyMethods.contains(xml, "0.9"));
        assertTrue(DefaultGroovyMethods.contains(xml, "TIMESHEET_ENTRY"));
        assertTrue(DefaultGroovyMethods.contains(xml, "CONFIG"));

        assertTrue(xml.startsWith("<?xml version="));
    }

}
