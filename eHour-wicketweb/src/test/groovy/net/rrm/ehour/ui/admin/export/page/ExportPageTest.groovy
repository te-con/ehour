package net.rrm.ehour.ui.admin.export.page

import net.rrm.ehour.export.service.ExportService
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.apache.wicket.Page
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 12, 2010 - 11:42:00 PM
 */
class ExportPageTest  extends AbstractSpringWebAppTester {

  @Mock
  private ExportService exportService

  @Before
  void initMock()
  {
    MockitoAnnotations.initMocks this
    getMockContext().putBean("exportService", exportService);
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

}
