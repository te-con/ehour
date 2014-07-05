package net.rrm.ehour.ui.common.header

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.util.AuthUtil
import net.rrm.ehour.ui.login.page.Logout
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.{BookmarkablePageLink, Link}
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.model.Model
import org.apache.wicket.spring.injection.annot.SpringBean

class LoggedInAsPanel(id: String) extends AbstractBasePanel(id) {
  @SpringBean
  protected var authUtil: AuthUtil = _

  override def onInitialize() = {
    super.onInitialize()

    val link = new BookmarkablePageLink("prefsLink", classOf[UserPreferencePage])
    val loggedInUserLabel = new Label("loggedInUser", new Model[String](EhourWebSession.getUser.getFullName))
    link.add(loggedInUserLabel)

    addOrReplace(link)

    addOrReplace(new BookmarkablePageLink("logoffLink", classOf[Logout]))

    addOrReplace(createImpersonatingPanel("impersonatingNotification"))
  }

  private def createImpersonatingPanel(id: String) = {
    if (EhourWebSession.getSession.isImpersonating) {
      val fragment = new Fragment(id, "impersonating", this)
      fragment.add(new Link("stop") {
        def onClick() {
          val session = EhourWebSession.getSession
          session.stopImpersonating()

          val homepage = authUtil.getHomepageForRole(session.getRoles)
          setResponsePage(homepage.homePage, homepage.parameters)
        }
      })

      fragment.add(new Label("name", EhourWebSession.getUser.getFullName))

      fragment
    }
    else {
      new WebMarkupContainer(id).setVisible(false)
    }
  }
}