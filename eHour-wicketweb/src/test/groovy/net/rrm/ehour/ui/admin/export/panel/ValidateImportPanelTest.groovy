package net.rrm.ehour.ui.admin.export.panel

import net.rrm.ehour.export.service.ImportException
import net.rrm.ehour.export.service.ImportService
import net.rrm.ehour.export.service.ParseSession
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.Model
import org.apache.wicket.util.tester.TestPanelSource
import org.junit.Before
import org.junit.Test
import org.junit.Ignore
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
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
    ParseSession status = new ParseSession(imported: false)

    when(importService.prepareImportDatabase(Mockito.anyString())).thenReturn(status)

    startPanel "fefe"

    tester.assertNoErrorMessage()
    tester.assertComponent "panel:${ValidateImportPanel.ID_STATUS}", ParseStatusPanel.class

    tester.executeAjaxEvent "panel:${ValidateImportPanel.ID_IMPORT_LINK}", "onclick"
  }

  @Test
  public void shouldDisplayValidateWithFailingImport()
  {
    ParseSession status = new ParseSession(imported: false)

    when(importService.prepareImportDatabase(Mockito.anyString())).thenReturn(status)
    when(importService.importDatabase(status)).thenThrow new ImportException("fe")

    startPanel "fefe"

    tester.assertNoErrorMessage()
    tester.assertComponent "panel:${ValidateImportPanel.ID_STATUS}", ParseStatusPanel.class

    tester.executeAjaxEvent "panel:${ValidateImportPanel.ID_IMPORT_LINK}", "onclick"
    assertFalse status.imported
  //  tester.assertInvisible "panel:${ValidateImportPanel.ID_IMPORT_LINK}"
  }

	@Ignore
  @Test
  public void shouldDisplayExceptionMessageForValidate()
  {
    when(importService.prepareImportDatabase(Mockito.anyString())).thenThrow(new ImportException(("fefe")))

    startPanel "fefe"

    tester.assertComponent "panel:${ValidateImportPanel.ID_STATUS}", Label.class
  }

@Ignore
  @Test
  public void shouldDisplayAfterImport()
  {
    ParseSession status = new ParseSession(imported: true)

    when(importService.prepareImportDatabase(Mockito.anyString())).thenReturn(status)

    startPanel status

    tester.assertNoErrorMessage()
    tester.assertComponent "panel:${ValidateImportPanel.ID_STATUS}", ParseStatusPanel.class
    tester.assertInvisible "panel:${ValidateImportPanel.ID_IMPORT_LINK}"
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
        return new ValidateImportPanel(panelId, new Model(constructParameter))
      }
    })
  }

}
