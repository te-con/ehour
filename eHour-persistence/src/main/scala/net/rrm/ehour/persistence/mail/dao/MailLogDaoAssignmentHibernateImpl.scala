package net.rrm.ehour.persistence.mail.dao

import java.util

import net.rrm.ehour.domain.MailLogAssignment
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateScalaImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.springframework.stereotype.Repository

/**
 * DAO for MailLog db operations
 */
@Repository("mailLogAssignmentDao")
class MailLogDaoAssignmentHibernateImpl extends AbstractGenericDaoHibernateScalaImpl[Integer, MailLogAssignment](classOf[MailLogAssignment]) with MailLogAssignmentDao {
  override def findMailLogOnAssignmentIds(projectAssignmentIds: Array[Integer]): util.List[MailLogAssignment] = {
    def op = () => findByNamedQueryAndNamedParameters("MailLogAssignment.findOnAssignmentIds", List("assignmentIds"), projectAssignmentIds.toList)
    ExponentialBackoffRetryPolicy.retry(op)
  }

}
