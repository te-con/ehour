package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl
import net.rrm.ehour.domain.TimesheetLock

class LockAdminBackingBean(var lock: TimesheetLock) extends AdminBackingBeanImpl[TimesheetLock] {
  override def getDomainObject: TimesheetLock = lock

  def getLock = lock

  def setLock(newLock: TimesheetLock) { this.lock = newLock }

  def isNew = lock.getPK == null

}
