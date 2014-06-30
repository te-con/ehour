package net.rrm.ehour.persistence.mail.dao

import java.util

import net.rrm.ehour.domain.MailLogAssignment
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import org.springframework.stereotype.Repository

/**
 * DAO for MailLog db operations
 */
@Repository("mailLogAssignmentDao")
class MailLogDaoAssignmentHibernateImpl extends AbstractGenericDaoHibernateImpl[Integer, MailLogAssignment](classOf[MailLogAssignment]) with MailLogAssignmentDao {
  override def findMailLogOnAssignmentIds(projectAssignmentIds: Array[Integer]): util.List[MailLogAssignment] =
    findByNamedQuery("MailLogAssignment.findOnAssignmentIds", List("assignmentIds"), projectAssignmentIds.toList)
}
