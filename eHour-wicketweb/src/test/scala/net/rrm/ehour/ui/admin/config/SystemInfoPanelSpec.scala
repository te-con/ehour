package net.rrm.ehour.ui.admin.config

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.sysinfo.{SystemInfo, SystemInfoService}
import org.mockito.Mockito._


class SystemInfoPanelSpec extends AbstractSpringWebAppSpec {
  "System Info Panel"  should {
    val sysInfoService = mock[SystemInfoService]
    springTester.getMockContext.putBean(sysInfoService)

    "render" in {
      when(sysInfoService.info).thenReturn(SystemInfo("mysql", "..", "jdbc.Driver"))

      tester.startComponentInPage(new SystemInfoPanel("id"))
      tester.assertNoErrorMessage()
    }
  }

}
