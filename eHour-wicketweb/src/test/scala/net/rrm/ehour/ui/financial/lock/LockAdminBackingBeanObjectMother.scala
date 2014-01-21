package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.domain.TimesheetLock
import java.util.Date

object LockAdminBackingBeanObjectMother {
  def create = {
    val lock = new TimesheetLock()
    lock.setDateStart(new Date())
    lock.setDateEnd(new Date)

    new LockAdminBackingBean(lock)
  }
}
