package net.rrm.ehour.ui.manage.user

import java.util

import net.rrm.ehour.domain.User
import net.rrm.ehour.security.SecurityRules
import net.rrm.ehour.ui.common.border.{GreyBlueRoundedBorder, GreyRoundedBorder}
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectedEvent
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.util.AuthUtil
import net.rrm.ehour.ui.common.wicket.AjaxLink
import net.rrm.ehour.ui.common.wicket.AjaxLink._
import net.rrm.ehour.ui.manage.AbstractManagePage
import net.rrm.ehour.user.service.UserService
import org.apache.wicket.event.IEvent
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.model.ResourceModel
import org.apache.wicket.spring.injection.annot.SpringBean

import scala.collection.convert.{WrapAsJava, WrapAsScala}

class ImpersonateUserPage extends AbstractManagePage(new ResourceModel("admin.impersonate.title")) {
  val Self = this

  val BorderId = "border"
  val FrameId = "frame"
  val ContentId = "content"

  val frame = new GreyRoundedBorder(FrameId, new ResourceModel("admin.impersonate.title"))
  val border = new GreyBlueRoundedBorder(BorderId).setOutputMarkupId(true).asInstanceOf[GreyBlueRoundedBorder]

  @SpringBean
  protected var userService: UserService = _

  @SpringBean
  protected var authUtil: AuthUtil = _

  override def onInitialize() {
    super.onInitialize()

    val splitAdminRole = EhourWebSession.getEhourConfig.isSplitAdminRole
    val user = EhourWebSession.getUser

    val filter = (xs:util.List[User]) => {
      val xsS = WrapAsScala.asScalaBuffer(xs)
      WrapAsJava.bufferAsJavaList(xsS.filter(u => SecurityRules.allowedToModify(user, u, splitAdminRole)))
    }

    add(new UserSelectionPanel("userSelection",
      showInactiveToggle = false,
      titleResourceKey = None,
      filter))
    add(frame)
    frame.add(border)

    border.add(if (getEhourWebSession.isImpersonating)
      createAlreadyImpersonatingFragment(ContentId)
    else
      createNoUserSelectedFragment(ContentId))
  }

  override def onEvent(wrappedEvent: IEvent[_]) {
    wrappedEvent.getPayload match {
      case event: EntrySelectedEvent if !getEhourWebSession.isImpersonating =>
          border.addOrReplace(createUserSelectedFragment(ContentId, event.userId))
          event.refresh(border)
      case _ =>
    }
  }

  private def createNoUserSelectedFragment(id: String) = new Fragment(id, "noUserSelected", Self).setOutputMarkupId(true)

  private def createAlreadyImpersonatingFragment(id: String) = new Fragment(id, "alreadyImpersonating", Self).setOutputMarkupId(true)

  private def createUserSelectedFragment(id: String, userId: Integer) = {

    val f = new Fragment(id, "userSelected", Self)
    f.setOutputMarkupId(true)

    val linkCallback: LinkCallback = target => {
      val user = userService.getUser(userId)
      val session = EhourWebSession.getSession
      session.impersonateUser(user)
      val roles = session.getRoles

      val homepage = authUtil.getHomepageForRole(roles)
      setResponsePage(homepage.homePage, homepage.parameters)
    }

    val link = new AjaxLink("impersonateLink", linkCallback)
    f.add(link)

    val user = userService.getUser(userId)
    f.add(new Label("name", user.getFullName))

    f
  }
}


