package net.rrm.ehour.persistence.report.dao

import java.util

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.report.reports.element.FlatReportElement

trait DetailedReportDao {
  /**
   * Get hours per day for assignments
   */
  def getHoursPerDayForAssignment(assignmentId: util.List[Integer], dateRange: DateRange): util.List[FlatReportElement]

  /**
   * Get hours per day for users
   */
  def getHoursPerDayForUsers(userIds: util.List[Integer], dateRange: DateRange): util.List[FlatReportElement]

  /**
   * Get hours per day for projects
   */
  def getHoursPerDayForProjects(projectIds: util.List[Integer], dateRange: DateRange): util.List[FlatReportElement]

  /**
   * Get hours per day for projects & users
   */
  def getHoursPerDayForProjectsAndUsers(projectIds: util.List[Integer], userIds: util.List[Integer], dateRange: DateRange): util.List[FlatReportElement]

  /**
   * Get hours per day
   */
  def getHoursPerDay(dateRange: DateRange): util.List[FlatReportElement]
}
