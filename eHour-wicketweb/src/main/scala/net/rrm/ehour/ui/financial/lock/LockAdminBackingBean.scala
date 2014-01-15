package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl
import net.rrm.ehour.domain.TimesheetLock

class LockAdminBackingBean(lock: TimesheetLock) extends AdminBackingBeanImpl {
  override def getDomainObject: TimesheetLock = lock
}
