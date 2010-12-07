package net.rrm.ehour.ui.admin.export.panel

import net.rrm.ehour.export.service.ImportService
import net.rrm.ehour.export.service.ParseSession
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.util.tester.TestPanelSource
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 12/7/10 - 2:12 AM
 */
class ValidateImportPanelTest extends AbstractSpringWebAppTester
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
  public void shouldDisplayValidate()
  {
    ParseSession status = new ParseSession(imported: true)
//    status.addError ExportType.USERS, "failed"
//    status.addError ExportType.USERS, "failed again"

    when(importService.prepareImportDatabase(Mockito.anyString())).thenReturn(status)

    startPanel "fefe"

    tester.assertNoErrorMessage()
//    Component msg = tester.getComponentFromLastRenderedPage("panel:importLink")
//    assertTrue msg.visible
  }


  private void startPanel(final String constructParameter)
  {
    tester.startPanel(new TestPanelSource()
    {
      @Override
      Panel getTestPanel(String panelId)
      {
        return new ValidateImportPanel(panelId, constructParameter)
      }
    })
  }

  private void startPanel(final ParseSession constructParameter)
  {
    tester.startPanel(new TestPanelSource()
    {
      @Override
      Panel getTestPanel(String panelId)
      {
        return new ValidateImportPanel(panelId, constructParameter)
      }
    })
  }

}
