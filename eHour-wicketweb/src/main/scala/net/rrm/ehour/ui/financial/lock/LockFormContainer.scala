package net.rrm.ehour.ui.financial.lock

import org.apache.wicket.model.Model
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.event.IEvent

class LockFormContainer (id: String, bean: LockAdminBackingBean) extends AbstractBasePanel(id, new Model[LockAdminBackingBean](bean)) {
  val AffectedUserId = "affectedUsersPanel"
  val self = this

  override def onInitialize() {
    super.onInitialize()

    add(new LockFormPanel("lockFormPanel", getPanelModel))

    add(new LockAffectedUsersPanel(AffectedUserId, getPanelModel))
  }

  override def onEvent(event: IEvent[_]) {
    event.getPayload match {
      case event: DateChangeEvent =>
        event.refresh(self.get(AffectedUserId))
      case _ =>
    }
  }
}
