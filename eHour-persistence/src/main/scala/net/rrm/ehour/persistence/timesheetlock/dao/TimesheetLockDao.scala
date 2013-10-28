package net.rrm.ehour.persistence.timesheetlock.dao

import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import net.rrm.ehour.domain.TimesheetLock
import org.springframework.stereotype.Repository

trait TimesheetLockDao extends GenericDao[TimesheetLock, Integer] {
}

@Repository("timesheetLockDao")
class TimesheetLockHibernateImpl extends AbstractGenericDaoHibernateImpl[TimesheetLock, Integer](classOf[TimesheetLock]) with TimesheetLockDao {

}

