package net.rrm.ehour.persistence.timesheetlock.dao

import java.util.{Date, List => JList}

import net.rrm.ehour.domain.TimesheetLock
import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import org.springframework.stereotype.Repository

trait TimesheetLockDao extends GenericDao[Integer, TimesheetLock] {
  def findMatchingLock(start: Date, end: Date): JList[TimesheetLock]
}

@Repository("timesheetLockDao")
class TimesheetLockDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, TimesheetLock](classOf[TimesheetLock]) with TimesheetLockDao {
  override def findMatchingLock(start: Date, end: Date): JList[TimesheetLock] = {
    val keys = List("startDate", "endDate")
    val params = List(start, end)

    findByNamedQuery("TimesheetLock.getLocksMatchingDate", keys, params)
  }
}
