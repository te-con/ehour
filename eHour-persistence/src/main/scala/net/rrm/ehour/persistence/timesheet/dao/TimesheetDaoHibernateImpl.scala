package net.rrm.ehour.persistence.timesheet.dao

import java.util
import java.util.Date

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{ProjectAssignment, TimesheetEntry, TimesheetEntryId}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import net.rrm.ehour.timesheet.dto.BookedDay
import org.springframework.stereotype.Repository

@Repository("timesheetDAO")
class TimesheetDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[TimesheetEntryId, TimesheetEntry](classOf[TimesheetEntry]) with TimesheetDao {
  override def getTimesheetEntriesInRange(userId: Integer, dateRange: DateRange): util.List[TimesheetEntry] =
    applyConstraintsAndExecute(userId, dateRange, "Timesheet.getEntriesBetweenDateForUserId", classOf[TimesheetEntry])

  override def getTimesheetEntriesInRange(assignment: ProjectAssignment, dateRange: DateRange): util.List[TimesheetEntry] = {
    val keys = List("dateStart", "dateEnd", "assignment")
    val params = List(dateRange.getDateStart, dateRange.getDateEnd, assignment)
    findByNamedQuery("Timesheet.getEntriesBetweenDateForAssignment", keys, params)
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

  override def getLatestTimesheetEntryForAssignment(assignmentId: Integer): TimesheetEntry = {
    val results = findByNamedQuery("Timesheet.getLatestEntryForAssignmentId", "assignmentId", assignmentId)
    if (results.size > 0) results.get(0) else null
  }

  override def deleteTimesheetEntries(assignmentIds: util.List[Integer]): Int = {
    val query = getSession.getNamedQuery("Timesheet.deleteOnAssignmentIds")
    query.setParameterList("assignmentIds", assignmentIds)
    ExponentialBackoffRetryPolicy retry query.executeUpdate
  }

  override def getTimesheetEntriesAfter(assignment: ProjectAssignment, date: Date): util.List[TimesheetEntry] = {
    val keys = List("date", "assignment")
    val params = List(date, assignment)
    findByNamedQuery("Timesheet.getEntriesAfterDateForAssignment", keys, params)
  }

  override def getTimesheetEntriesBefore(assignment: ProjectAssignment, date: Date): util.List[TimesheetEntry] = {
    val keys = List("date", "assignment")
    val params = List(date, assignment)
    findByNamedQuery("Timesheet.getEntriesBeforeDateForAssignment", keys, params)
  }
}