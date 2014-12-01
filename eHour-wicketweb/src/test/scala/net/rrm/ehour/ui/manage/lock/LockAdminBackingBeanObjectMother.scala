package net.rrm.ehour.ui.manage.lock

import java.util.Date

import net.rrm.ehour.domain.TimesheetLockDomain

object LockAdminBackingBeanObjectMother {
  def create = {
    val lock = new TimesheetLockDomain()
    lock.setDateStart(new Date())
    lock.setDateEnd(new Date)

    new LockAdminBackingBean(lock)
  }
}
