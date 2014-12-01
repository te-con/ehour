package net.rrm.ehour.persistence.timesheetlock.dao

import java.util.{Date, List => JList}

import net.rrm.ehour.domain.TimesheetLockDomain
import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import org.springframework.stereotype.Repository

trait TimesheetLockDao extends GenericDao[Integer, TimesheetLockDomain] {
  def findMatchingLock(start: Date, end: Date): JList[TimesheetLockDomain]
}

@Repository("timesheetLockDao")
class TimesheetLockDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, TimesheetLockDomain](classOf[TimesheetLockDomain]) with TimesheetLockDao {
  override def findMatchingLock(start: Date, end: Date): JList[TimesheetLockDomain] = {
    val keys = List("startDate", "endDate")
    val params = List(start, end)

    findByNamedQuery("TimesheetLock.getLocksMatchingDate", keys, params)
  }
}
