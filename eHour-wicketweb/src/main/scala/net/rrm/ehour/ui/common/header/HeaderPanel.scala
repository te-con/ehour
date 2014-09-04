package net.rrm.ehour.ui.common.header

import java.util

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.util.AuthUtil
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.spring.injection.annot.SpringBean

class HeaderPanel(id: String) extends AbstractBasePanel(id) {
  @SpringBean
  protected var authUtil: AuthUtil = _

  @SpringBean(name = "menuDefinition")
  protected var menuDefinition: util.List[_ <: MenuItem] = _

  override def onInitialize(): Unit = {
    super.onInitialize()

    val homepage = authUtil.getHomepageForRole(EhourWebSession.getSession.getRoles)
    add(new BookmarkablePageLink("homeLink", homepage.homePage, homepage.parameters))
    add(createNav("nav"))
    add(new LoggedInAsPanel("loggedInAs"))
  }

  private def createNav(id: String) = new TreeBasedMenu(id, menuDefinition)
}