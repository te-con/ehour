package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.restore.RestoreService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    private void startPanel(final ParseSession session) {
        tester.startComponentInPage(new RestoreDbPanel("id", session));
    }

    @Test
    public void should_render() {
        startPanel(new ParseSession());
        tester.assertNoErrorMessage();
        tester.assertNoInfoMessage();
    }

    @Mock
    private RestoreService importService;
}
