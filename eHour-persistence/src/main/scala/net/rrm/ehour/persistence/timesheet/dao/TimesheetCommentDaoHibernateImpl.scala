package net.rrm.ehour.persistence.timesheet.dao

import net.rrm.ehour.domain.{TimesheetComment, TimesheetCommentId}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.springframework.stereotype.Repository

/**
 * CRUD on timesheetComment domain obj
 **/
@Repository("timesheetCommentDao")
class TimesheetCommentDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[TimesheetCommentId, TimesheetComment](classOf[TimesheetComment]) with TimesheetCommentDao {
  override def deleteCommentsForUser(userId: Integer): Int = {
    val session = getSession
    val query = session.getNamedQuery("TimesheetComment.deleteUserId")
    query.setParameter("userId", userId)

    ExponentialBackoffRetryPolicy retry query.executeUpdate
  }
}