package net.rrm.ehour.ui.admin.export.page

import net.rrm.ehour.export.service.ExportService
import net.rrm.ehour.export.service.ImportService
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.apache.wicket.Page
import org.apache.wicket.protocol.http.MockHttpServletRequest
import org.apache.wicket.util.file.File
import org.apache.wicket.util.tester.FormTester
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
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
    return getTester().startPage(ExportPage)
  }

  @Test
  void shouldClickExportLink()
  {
    when(exportService.exportDatabase()).thenReturn("this should be xml");

    startPage()
    tester.clickLink "exportLink"
  }

  @Test
  void shouldUploadXMl()
  {
    startPage()

    FormTester formTester = tester.newFormTester("form")
    MockHttpServletRequest request = tester.getServletRequest()
    request.setUseMultiPartContentType true

    formTester.setFile "file", new File("src/test/resources/import_ok.xml"), "text/xml"
    tester.executeAjaxEvent "form:ajaxSubmit", "onclick"
  }
}
