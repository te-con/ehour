package net.rrm.ehour.ui.manage.lock

import java.util.Date

import net.rrm.ehour.domain.TimesheetLock

object LockAdminBackingBeanObjectMother {
  def create = {
    val lock = new TimesheetLock()
    lock.setDateStart(new Date())
    lock.setDateEnd(new Date)

    new LockAdminBackingBean(lock)
  }
}
