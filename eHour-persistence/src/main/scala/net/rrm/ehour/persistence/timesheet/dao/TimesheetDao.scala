package net.rrm.ehour.persistence.timesheet.dao

import java.util
import java.util.Date

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{ProjectAssignment, TimesheetEntry, TimesheetEntryId}
import net.rrm.ehour.persistence.dao.GenericDao
import net.rrm.ehour.timesheet.dto.BookedDay

trait TimesheetDao extends GenericDao[TimesheetEntryId, TimesheetEntry] {
  /**
   * Get timesheet entries within date range for a user
   */
  def getTimesheetEntriesInRange(userId: Integer, dateRange: DateRange): util.List[TimesheetEntry]

  /**
   * Get timesheet entries within date range for an assignment
   */
  def getTimesheetEntriesInRange(assignment: ProjectAssignment, dateRange: DateRange): util.List[TimesheetEntry]

  /**
   * Get timesheet entries within date range
   */
  def getTimesheetEntriesInRange(dateRange: DateRange): util.List[TimesheetEntry]

  /**
   * Get timesheet entries before date
   */
  def getTimesheetEntriesBefore(assignment: ProjectAssignment, date: Date): util.List[TimesheetEntry]

  /**
   * Get timesheet entries after date
   */
  def getTimesheetEntriesAfter(assignment: ProjectAssignment, date: Date): util.List[TimesheetEntry]

  /**
   * Get cumulated hours per day for a date range

   */
  def getBookedHoursperDayInRange(userId: Integer, dateRange: DateRange): util.List[BookedDay]

  /**
   * Get latest timesheet entry for assignment
   */
  def getLatestTimesheetEntryForAssignment(assignmentId: Integer): TimesheetEntry

  /**
   * Delete timesheet entries for assignment
   */
  def deleteTimesheetEntries(assignmentIds: util.List[Integer]): Int
}


