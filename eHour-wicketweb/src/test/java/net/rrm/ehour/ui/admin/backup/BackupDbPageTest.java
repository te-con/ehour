package net.rrm.ehour.ui.admin.backup;

import net.rrm.ehour.backup.service.backup.DatabaseBackupService;
import net.rrm.ehour.backup.service.restore.RestoreService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 12, 2010 - 11:42:00 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class BackupDbPageTest extends BaseSpringWebAppTester {
    @Mock
    private DatabaseBackupService exportService;

    @Mock
    private RestoreService importService;

    @Before
    public void initMock() {
        MockitoAnnotations.initMocks(this);
        getMockContext().putBean("databaseBackupService", exportService);
        getMockContext().putBean("restoreService", importService);
    }

    @Test
    public void shouldRenderPage() {
        startPage();
        tester.assertRenderedPage(BackupDbPage.class);
        tester.assertNoErrorMessage();
    }

    private Page startPage() {
        BackupDbPage page = tester.startPage(BackupDbPage.class);

        MockHttpServletRequest request = tester.getRequest();
        request.setUseMultiPartContentType(true);

        return page;
    }
}
