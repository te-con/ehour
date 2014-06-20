package net.rrm.ehour.persistence.timesheetlock.dao

import java.util.{Date, List => JList}

import net.rrm.ehour.domain.TimesheetLock
import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import org.springframework.stereotype.Repository

trait TimesheetLockDao extends GenericDao[TimesheetLock, Integer] {
  def findMatchingLock(start: Date, end: Date): JList[TimesheetLock]
}

@Repository("timesheetLockDao")
class TimesheetLockHibernateImpl extends AbstractGenericDaoHibernateImpl[TimesheetLock, Integer](classOf[TimesheetLock]) with TimesheetLockDao {
  def findMatchingLock(start: Date, end: Date): JList[TimesheetLock] = {
    val keys = Array[String]("startDate", "endDate")
    val params = Array[AnyRef](start, end)

    findByNamedQueryAndNamedParam("TimesheetLock.getLocksMatchingDate", keys, params).asInstanceOf[JList[TimesheetLock]]
  }
}
