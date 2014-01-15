package net.rrm.ehour.ui.financial.lock

import org.apache.wicket.model.Model
import net.rrm.ehour.ui.common.panel.AbstractBasePanel

class LockFormContainer (id: String, bean: LockAdminBackingBean) extends AbstractBasePanel(id, new Model[LockAdminBackingBean](bean)) {

}
