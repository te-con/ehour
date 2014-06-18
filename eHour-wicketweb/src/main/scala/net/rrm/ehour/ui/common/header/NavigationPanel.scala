package net.rrm.ehour.ui.common.header

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.util.AuthUtil
import net.rrm.ehour.ui.login.page.Logout
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.model.Model

class NavigationPanel(id: String) extends AbstractBasePanel[Void](id) {


  override def onInitialize(): Unit = {
    super.onInitialize()

    add(new BookmarkablePageLink("homeLink", AuthUtil.getHomepageForRole(EhourWebSession.getSession.getRoles)))
    add(createNav("nav"))
    add(new BookmarkablePageLink("logoffLink", classOf[Logout]))
    add(addLoggedInUser("prefsLink"))
  }

  private def createNav(id: String) = new TreeBasedMenu(id, MenuDefinition.createMenuDefinition)

  private def addLoggedInUser(id: String) = {
    val link = new BookmarkablePageLink(id, classOf[UserPreferencePage])
    val loggedInUserLabel = new Label("loggedInUser", new Model[String](AuthUtil.getUser.getFullName))
    link.add(loggedInUserLabel)
  }

  private final val serialVersionUID: Long = 854412484275829659L
}