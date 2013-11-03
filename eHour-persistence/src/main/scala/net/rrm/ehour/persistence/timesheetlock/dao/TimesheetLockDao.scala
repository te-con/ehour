package net.rrm.ehour.persistence.timesheetlock.dao

import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import net.rrm.ehour.domain.TimesheetLock
import org.springframework.stereotype.Repository
import java.util.Date
import java.util.{List => JList}

trait TimesheetLockDao extends GenericDao[TimesheetLock, Integer] {
  def findMatchingLock(start: Date, end: Date): JList[TimesheetLock]
}

@Repository("timesheetLockDao")
class TimesheetLockHibernateImpl extends AbstractGenericDaoHibernateImpl[TimesheetLock, Integer](classOf[TimesheetLock]) with TimesheetLockDao {
  def findMatchingLock(start: Date, end: Date): JList[TimesheetLock] = {
    val keys = Array[String]("start", "end")
    val params = Array[AnyRef](start, end)

    getHibernateTemplate.findByNamedQueryAndNamedParam("TimesheetLock.getLocksMatchingDate", keys, params).asInstanceOf[JList[TimesheetLock]]
  }
}
