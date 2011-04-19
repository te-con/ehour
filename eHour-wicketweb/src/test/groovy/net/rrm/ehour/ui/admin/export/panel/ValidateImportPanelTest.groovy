package net.rrm.ehour.ui.admin.export.panel

import net.rrm.ehour.export.service.ImportService
import net.rrm.ehour.export.service.ParseSession
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import net.rrm.ehour.ui.common.event.AjaxEventHook
import net.rrm.ehour.ui.common.event.EventPublisher
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.Model
import org.apache.wicket.util.tester.ITestPanelSource
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
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
  public void shouldDisplayValidateAndClickImport()
  {
    ParseSession session = new ParseSession(imported: false)

    when(importService.prepareImportDatabase(Mockito.anyString())).thenReturn(session)

    startPanel "fefe"

    tester.assertNoErrorMessage()
    tester.assertComponent "panel:${ValidateImportPanel.ID_STATUS}", ParseStatusPanel.class

    def hook = new AjaxEventHook()
    EventPublisher.listenerHook = hook

    tester.executeAjaxEvent "panel:${ValidateImportPanel.ID_IMPORT_LINK}", "onclick"

    assertEquals 1, hook.events.size()
    def event = hook.events[0] as PayloadAjaxEvent
    assertEquals(session, event.payload)

  }

  @Test
  public void shouldDisplayValidateWithFailingImport()
  {
    ParseSession status = new ParseSession(globalError: true, globalErrorMessage: "n/a")

    when(importService.prepareImportDatabase(Mockito.anyString())).thenReturn(status)

    startPanel "fefe"

    tester.assertNoErrorMessage()
    tester.assertComponent "panel:${ValidateImportPanel.ID_STATUS}", ParseStatusPanel.class
    tester.assertComponent "panel:${ValidateImportPanel.ID_STATUS}:globalError", Label.class
    tester.assertModelValue "panel:${ValidateImportPanel.ID_STATUS}:globalError", "n/a"

    assertFalse status.importable
  }


  private void startPanel(final String constructParameter)
  {
    tester.startPanel(new ITestPanelSource()
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
    tester.startPanel(new ITestPanelSource()
    {
      @Override
      Panel getTestPanel(String panelId)
      {
        return new ValidateImportPanel(panelId, new Model(constructParameter))
      }
    })
  }

}
