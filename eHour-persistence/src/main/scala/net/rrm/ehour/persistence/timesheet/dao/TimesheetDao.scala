package net.rrm.ehour.persistence.timesheet.dao

import java.util
import java.util.Date

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{Activity, TimesheetEntry, TimesheetEntryId}
import net.rrm.ehour.persistence.dao.GenericDao
import net.rrm.ehour.timesheet.dto.BookedDay

trait TimesheetDao extends GenericDao[TimesheetEntryId, TimesheetEntry] {
  /**
   * Get timesheet entries within date range for a user
   */
  def getTimesheetEntriesInRange(userId: Integer, dateRange: DateRange): util.List[TimesheetEntry]

  /**
   * Get timesheet entries within date range for an activity
   */
  def getTimesheetEntriesInRange(activity: Activity, dateRange: DateRange): util.List[TimesheetEntry]

  /**
   * Get timesheet entries within date range
   */
  def getTimesheetEntriesInRange(dateRange: DateRange): util.List[TimesheetEntry]

  /**
   * Get timesheet entries before date
   */
  def getTimesheetEntriesBefore(activity: Activity, date: Date): util.List[TimesheetEntry]

  /**
   * Get timesheet entries after date
   */
  def getTimesheetEntriesAfter(activity: Activity, date: Date): util.List[TimesheetEntry]

  /**
   * Get cumulated hours per day for a date range

   */
  def getBookedHoursperDayInRange(userId: Integer, dateRange: DateRange): util.List[BookedDay]

  /**
   * Get latest timesheet entry for activity
   */
  def getLatestTimesheetEntryForActivity(activityId: Integer): TimesheetEntry

  /**
   * Delete timesheet entries for activity
   */
  def deleteTimesheetEntries(activityIds: util.List[Integer]): Int
}


