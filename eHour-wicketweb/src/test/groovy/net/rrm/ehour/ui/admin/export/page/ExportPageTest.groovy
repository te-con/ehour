package net.rrm.ehour.ui.admin.export.page

import net.rrm.ehour.export.service.ExportService
import net.rrm.ehour.export.service.ImportService
import net.rrm.ehour.export.service.ParseSession
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.apache.wicket.Page
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.protocol.http.MockHttpServletRequest
import org.apache.wicket.util.file.File
import org.apache.wicket.util.tester.FormTester
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.verifyZeroInteractions
import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 12, 2010 - 11:42:00 PM
 */
class ExportPageTest extends AbstractSpringWebAppTester
{
  @Mock
  private ExportService exportService

  @Mock
  private ImportService importService

  @Before
  void initMock()
  {
    MockitoAnnotations.initMocks this
    getMockContext().putBean("exportService", exportService);
    getMockContext().putBean("importService", importService);
  }

  @Test
  void shouldRenderPage()
  {
    startPage()
    tester.assertRenderedPage ExportPage
    tester.assertNoErrorMessage()
  }

  private Page startPage()
  {

    def page = getTester().startPage(ExportPage)

    MockHttpServletRequest request = tester.getServletRequest()
    request.setUseMultiPartContentType true

    return page
  }

  @Test
  void shouldClickExportLink()
  {
    when(exportService.exportDatabase()).thenReturn("this should be xml");

    startPage()
    tester.clickLink "frame:backupBorder:exportLink"
    tester.assertRenderedPage ExportPage.class
  }

  @Test
  void shouldUploadXML()
  {
    startPage()

    FormTester formTester = tester.newFormTester("frame:restoreBorder:form")

    when(importService.prepareImportDatabase(Mockito.any())).thenReturn(new ParseSession());

    formTester.setFile "file", new File("src/test/resources/import_ok.xml"), "text/xml"
    tester.executeAjaxEvent "frame:restoreBorder:form:ajaxSubmit", "onclick"
    tester.assertComponent "frame:restoreBorder:form:parseStatus", AjaxLazyLoadPanel.class
  }

  @Test
  void shouldFailForWrongContentType()
  {
    startPage()

    FormTester formTester = tester.newFormTester("frame:restoreBorder:form")
    formTester.setFile "file", new File("src/test/resources/import_ok.xml"), "application/zip"

    tester.executeAjaxEvent "frame:restoreBorder:form:ajaxSubmit", "onclick"
    tester.assertComponent "frame:restoreBorder:form:parseStatus", Label.class

    verifyZeroInteractions(importService)
  }

  @Test
  void shouldFailForNoFile()
  {
    startPage()

    tester.executeAjaxEvent "frame:restoreBorder:form:ajaxSubmit", "onclick"
    tester.assertComponent "frame:restoreBorder:form:parseStatus", Label.class

    verifyZeroInteractions(importService)
  }
}
