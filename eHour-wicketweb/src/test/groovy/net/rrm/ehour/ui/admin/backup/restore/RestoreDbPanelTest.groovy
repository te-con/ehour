package net.rrm.ehour.ui.admin.backup.restore

import net.rrm.ehour.backup.domain.ParseSession
import net.rrm.ehour.backup.service.RestoreService
import net.rrm.ehour.ui.common.BaseSpringWebAppTester
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 12/12/10 - 12:49 AM
 */
class RestoreDbPanelTest extends BaseSpringWebAppTester
{
  @Mock
  private RestoreService importService

  @Before
  void initMock()
  {
    MockitoAnnotations.initMocks this
    getMockContext().putBean("restoreService", importService);
  }

  @Test
  public void shouldDisplayAfterImport()
  {
    ParseSession status = new ParseSession(imported: true)

    when(importService.importDatabase(status)).thenReturn(status)

    startPanel status

    tester.assertNoErrorMessage()
    tester.assertComponent "id:${ValidateRestorePanel.ID_STATUS}", ParseStatusPanel.class
  }

  private void startPanel(final ParseSession session)
  {
    tester.startComponentInPage(new RestoreDbPanel("id", session))
  }
}
