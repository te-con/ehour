package net.rrm.ehour.ui.admin.backup.backup;

import net.rrm.ehour.backup.service.backup.DatabaseBackupService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BackupDbPanelTest extends BaseSpringWebAppTester {
    @Mock
    private DatabaseBackupService exportService;

    @Before
    public void init() throws Exception {
        getMockContext().putBean("databaseBackupService", exportService);
    }

    private BackupDbPanel startPanel() {
        return tester.startComponentInPage(BackupDbPanel.class);
    }

    @Test
    public void shouldClickBackupLink() {
        when(exportService.exportDatabase()).thenReturn("this should be xml".getBytes());

        startPanel();

        tester.clickLink("testObject:frame:frame_body:backupBorder:backupBorder_body:backupLink");
        tester.assertNoErrorMessage();
    }
}