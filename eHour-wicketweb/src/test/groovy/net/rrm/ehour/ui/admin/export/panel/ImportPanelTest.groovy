package net.rrm.ehour.ui.admin.export.panel

import net.rrm.ehour.export.service.ImportService
import net.rrm.ehour.export.service.ParseSession
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 12/12/10 - 12:49 AM
 */
class ImportPanelTest extends AbstractSpringWebAppTester
{
  @Mock
  private ImportService importService

  @Before
  void initMock()
  {
    MockitoAnnotations.initMocks this
    getMockContext().putBean("importService", importService);
  }

  @Test
  public void shouldDisplayAfterImport()
  {
    ParseSession status = new ParseSession(imported: true)

    when(importService.importDatabase(status)).thenReturn(status)

    startPanel status

    tester.assertNoErrorMessage()
    tester.assertComponent "id:${ValidateImportPanel.ID_STATUS}", ParseStatusPanel.class
  }

  private void startPanel(final ParseSession session)
  {
    tester.startComponentInPage(new ImportPanel("id", session))
  }
}
