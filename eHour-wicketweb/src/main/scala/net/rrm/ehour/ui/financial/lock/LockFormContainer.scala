package net.rrm.ehour.ui.financial.lock

import org.apache.wicket.model.Model
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.wicket.Container

class LockFormContainer (id: String, bean: LockAdminBackingBean) extends AbstractBasePanel(id, new Model[LockAdminBackingBean](bean)) {
  override def onInitialize() {
    super.onInitialize()

    add(new LockFormPanel("lockFormPanel", getPanelModel))
    add(new Container("affectedUsersPanel"))
  }
}
