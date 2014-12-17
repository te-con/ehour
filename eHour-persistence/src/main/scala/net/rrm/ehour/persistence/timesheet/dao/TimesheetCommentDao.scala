package net.rrm.ehour.persistence.timesheet.dao

import java.util

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{TimesheetComment, TimesheetCommentId}
import net.rrm.ehour.persistence.dao.GenericDao

/**
 * CRUD on timesheetComment domain obj
 **/
trait TimesheetCommentDao extends GenericDao[TimesheetCommentId, TimesheetComment] {
  /**
   * Delete comments for user
   */
  def deleteCommentsForUser(userId: Integer): Int

  def findCommentBetween(dateRange: DateRange): util.List[TimesheetComment]

  def findCommentBetweenForUsers(users: util.List[Integer], dateRange: DateRange): util.List[TimesheetComment]
}
