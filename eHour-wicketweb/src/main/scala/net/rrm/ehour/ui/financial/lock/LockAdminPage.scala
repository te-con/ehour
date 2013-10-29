package net.rrm.ehour.ui.financial.lock

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.common.page.AbstractBasePage
import org.apache.wicket.model.ResourceModel

@AuthorizeInstantiation(value = Array(UserRole.ROLE_ADMIN))
class LockAdminPage extends AbstractBasePage[String](new ResourceModel("report.summary.title")) {
  override def onInitialize() {
    super.onInitialize()

    add(new ExistingLocksPanel(LockAdminPage.ExistingLocksId))
    add(new LockDetailsPanel(LockAdminPage.LockDetailsId))
  }
}

object LockAdminPage {
  val FrameId = "frame"
  
  val ExistingLocksId = "existingLocks"

  val LockDetailsId = "lockDetails"
}
