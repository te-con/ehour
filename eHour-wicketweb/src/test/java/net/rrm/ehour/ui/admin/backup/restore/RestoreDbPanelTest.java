package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.restore.RestoreService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/12/10 - 12:49 AM
 */
public class RestoreDbPanelTest extends BaseSpringWebAppTester {
    @Before
    public void initMock() {
        MockitoAnnotations.initMocks(this);
        getMockContext().putBean("restoreService", importService);
    }

    @Test
    public void shouldDisplayAfterImport() throws ImportException {
        ParseSession session = new ParseSession();

        session.setImported(true);

        when(importService.importDatabase(session)).thenReturn(session);

        startPanel(session);

        tester.assertNoErrorMessage();
        tester.assertComponent("id:" + ValidateRestorePanel.ID_STATUS, ParseStatusPanel.class);
    }

    private void startPanel(final ParseSession session) {
        tester.startComponentInPage(new RestoreDbPanel("id", session));
    }

    @Mock
    private RestoreService importService;
}
