package net.rrm.ehour.persistence.timesheetlock.dao

import java.util.{Date, List => JList}

import net.rrm.ehour.domain.TimesheetLock
import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateScalaImpl, GenericDao}
import org.springframework.stereotype.Repository

trait TimesheetLockDao extends GenericDao[Integer, TimesheetLock] {
  def findMatchingLock(start: Date, end: Date): JList[TimesheetLock]
}

@Repository("timesheetLockDao")
class TimesheetLockHibernateImpl extends AbstractGenericDaoHibernateScalaImpl[Integer, TimesheetLock](classOf[TimesheetLock]) with TimesheetLockDao {
  def findMatchingLock(start: Date, end: Date): JList[TimesheetLock] = {
    val keys = List("startDate", "endDate")
    val params = List(start, end)

    findByNamedQueryAndNamedParameters("TimesheetLock.getLocksMatchingDate", keys, params)
  }
}
