package net.rrm.ehour.ui.common.header

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.util.AuthUtil
import org.apache.wicket.markup.html.link.BookmarkablePageLink

class HeaderPanel(id: String) extends AbstractBasePanel(id) {
  override def onInitialize(): Unit = {
    super.onInitialize()

    add(new BookmarkablePageLink("homeLink", AuthUtil.getHomepageForRole(EhourWebSession.getSession.getRoles)))
    add(createNav("nav"))
    add(new LoggedInAsPanel("loggedInAs"))
  }

  private def createNav(id: String) = new TreeBasedMenu(id, MenuDefinition.createMenuDefinition)
}