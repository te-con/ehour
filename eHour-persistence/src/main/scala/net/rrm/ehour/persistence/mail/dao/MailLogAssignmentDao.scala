package net.rrm.ehour.persistence.mail.dao

import java.util

import net.rrm.ehour.domain.MailLogAssignment
import net.rrm.ehour.persistence.dao.GenericDao

trait MailLogAssignmentDao extends GenericDao[Integer, MailLogAssignment] {
  /**
   * Get mail log on project assignment id's
   */
  def findMailLogOnAssignmentIds(projectAssignmentId: Array[Integer]): util.List[MailLogAssignment]
}

