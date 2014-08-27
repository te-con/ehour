package net.rrm.ehour.persistence.timesheet.dao

import java.util
import java.util.Date

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{Activity, ProjectAssignment, TimesheetEntry, TimesheetEntryId}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import net.rrm.ehour.timesheet.dto.BookedDay
import org.springframework.stereotype.Repository

@Repository("timesheetDAO")
class TimesheetDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[TimesheetEntryId, TimesheetEntry](classOf[TimesheetEntry]) with TimesheetDao {
  override def getTimesheetEntriesInRange(userId: Integer, dateRange: DateRange): util.List[TimesheetEntry] =
    applyConstraintsAndExecute(userId, dateRange, "Timesheet.getEntriesBetweenDateForUserId", classOf[TimesheetEntry])

  override def getTimesheetEntriesInRange(activity: Activity, dateRange: DateRange): util.List[TimesheetEntry] = {
    val keys = List("dateStart", "dateEnd", "activity")
    val params = List(dateRange.getDateStart, dateRange.getDateEnd, activity)
    findByNamedQuery("Timesheet.getEntriesBetweenDateForActivity", keys, params)
  }

  override def getTimesheetEntriesInRange(dateRange: DateRange): util.List[TimesheetEntry] = {
    val keys = List("dateStart", "dateEnd")
    val params = List(dateRange.getDateStart, dateRange.getDateEnd)
    findByNamedQuery("Timesheet.getEntriesBetweenDate", keys, params)
  }

  override def getBookedHoursperDayInRange(userId: Integer, dateRange: DateRange): util.List[BookedDay] =
    applyConstraintsAndExecute(userId, dateRange, "Timesheet.getBookedDaysInRangeForUserId", classOf[BookedDay])

  private def applyConstraintsAndExecute[T](userId: Integer, dateRange: DateRange, hql: String, clazz: Class[T]): util.List[T] = {
    val keys = List("dateStart", "dateEnd", "userId")
    val params = List(dateRange.getDateStart, dateRange.getDateEnd, userId)
    findByNamedQuery(hql, keys, params).asInstanceOf[util.List[T]]
  }

  override def getLatestTimesheetEntryForActivity(activityId: Integer): TimesheetEntry = {
    val results = findByNamedQuery("Timesheet.getLatestEntryForActivityId", "activityId", activityId)
    if (results.size > 0) results.get(0) else null
  }

  override def deleteTimesheetEntries(activityIds: util.List[Integer]): Int = {
    val query = getSession.getNamedQuery("Timesheet.deleteOnActivityIds")
    query.setParameterList("activityIds", activityIds)
    ExponentialBackoffRetryPolicy retry query.executeUpdate
  }

  override def getTimesheetEntriesAfter(activity: Activity, date: Date): util.List[TimesheetEntry] = {
    val keys = List("date", "activity")
    val params = List(date, activity)
    findByNamedQuery("Timesheet.getEntriesBeforeDateForActivity", keys, params)
  }

  override def getTimesheetEntriesBefore(activity: Activity, date: Date): util.List[TimesheetEntry] = {
    val keys = List("date", "activity")
    val params = List(date, activity)
    findByNamedQuery("Timesheet.getEntriesAfterDateForActivity", keys, params)
  }
}