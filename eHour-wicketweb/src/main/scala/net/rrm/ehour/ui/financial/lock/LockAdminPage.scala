package net.rrm.ehour.ui.financial.lock

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.common.page.AbstractBasePage
import org.apache.wicket.model.ResourceModel
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}

@AuthorizeInstantiation(value = Array(UserRole.ROLE_ADMIN))
class LockAdminPage extends AbstractBasePage[String](new ResourceModel("op.lock.admin.title")) {
  val Css = new CssResourceReference(classOf[LockAdminPage], "timesheetlocks.css")

  override def onInitialize() {
    super.onInitialize()

    add(new ExistingLocksPanel(LockAdminPage.ExistingLocksId))
    add(new LockDetailsPanel(LockAdminPage.LockDetailsId))
  }

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}

object LockAdminPage {
  val FrameId = "frame"
  val ExistingLocksId = "existingLocks"
  val LockDetailsId = "lockDetails"
}
