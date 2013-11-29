package net.rrm.ehour.ui.admin.config.page

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.sysinfo.{SysInfo, SysInfoService}
import org.mockito.Mockito._


class SystemInfoPanelSpec extends AbstractSpringWebAppSpec {
  "System Info Panel"  should {
    val sysInfoService = mock[SysInfoService]
    springTester.getMockContext.putBean(sysInfoService)

    "render" in {
      when(sysInfoService.info).thenReturn(SysInfo("mysql", "..", "jdbc.Driver"))

      tester.startComponentInPage(new SystemInfoPanel("id"))
      tester.assertNoErrorMessage()
    }
  }

}
