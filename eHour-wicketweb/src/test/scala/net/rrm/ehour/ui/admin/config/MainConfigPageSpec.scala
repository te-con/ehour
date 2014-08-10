package net.rrm.ehour.ui.admin.config

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.config.service.{ConfigurationService, IPersistConfiguration}
import net.rrm.ehour.mail.service.MailMan
import net.rrm.ehour.sysinfo.{SystemInfo, SystemInfoService}
import net.rrm.ehour.user.service.UserService
import org.mockito.Mockito._

class MainConfigPageSpec extends AbstractSpringWebAppSpec {
  "Main Config page"  should {
    val sysInfoService = mockService[SystemInfoService]
    val configService = mockService[ConfigurationService]
    mockService[UserService]
    mockService[IPersistConfiguration]
    mockService[MailMan]

    "render" in {
      when(sysInfoService.info).thenReturn(SystemInfo("mysql", "..", "jdbc.Driver"))
      when(configService.getConfiguration).thenReturn(new EhourConfigStub)

      tester.startPage(classOf[MainConfigPage])
      tester.assertNoErrorMessage()
    }
  }
}
