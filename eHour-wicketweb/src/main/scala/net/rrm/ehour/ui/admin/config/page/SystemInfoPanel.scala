package net.rrm.ehour.ui.admin.config.page

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.markup.html.basic.Label
import net.rrm.ehour.ui.EhourWebApplication
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.request.resource.CssResourceReference
import net.rrm.ehour.sysinfo.SysInfoService
import org.apache.wicket.spring.injection.annot.SpringBean

class SystemInfoPanel(id: String) extends AbstractBasePanel(id) {

  @SpringBean
  protected var infoService: SysInfoService = _

  val Css = new CssResourceReference(classOf[SystemInfoPanel], "systemInfo.css")

  override def onInitialize() {
    super.onInitialize()

    add(new Label("ehour.version", EhourWebApplication.get().getVersion))
    add(new Label("ehour.build", EhourWebApplication.get().getBuild))
    add(new Label("ehour.home", EhourWebApplication.get().geteHourHome()))

    add(new Label("java.version", System.getProperty("java.runtime.version")))
    add(new Label("java.vendor", System.getProperty("java.vm.vendor")))

    add(new Label("os.name", System.getProperty("os.name")))
    add(new Label("os.arch", System.getProperty("os.arch")))

    add(new Label("server.name", EhourWebApplication.get().getServletContext.getServerInfo))

    val info = infoService.info
    add(new Label("db.name", info.databaseName))
    add(new Label("db.driver", info.jdbcDriver))
    add(new Label("db.url", info.databaseUrl))

  }

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}
