/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.rrm.ehour.persistence.report.dao

import java.util

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{Project, Activity, User}
import net.rrm.ehour.persistence.dao.{AbstractAnnotationDaoHibernate4Impl, FindByNamedQuery}
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement
import org.springframework.stereotype.Repository

/**
 * Reporting data operations
 *
 * @author Thies
 */
@Repository("reportAggregatedDao")
class ReportAggregatedDaoHibernateImpl extends AbstractAnnotationDaoHibernate4Impl
                                        with ReportAggregatedDao
                                        with FindByNamedQuery {
  private final val CacheRegion = Some("query.Report")

  override def getCumulatedHoursPerActivityForUsers(userIds: util.List[Integer], dateRange: DateRange): util.List[ActivityAggregateReportElement] = {
    val keys = List("dateStart", "dateEnd", "userIds")
    val params = List(dateRange.getDateStart, dateRange.getDateEnd, userIds)
    findByNamedQuery[ActivityAggregateReportElement]("Report.getCumulatedHoursPerAssignmentOnDateForUsers", keys, params, CacheRegion)
  }

  override def getCumulatedHoursPerActivityForUsers(userIds: util.List[Integer]): util.List[ActivityAggregateReportElement] =
    findByNamedQuery[ActivityAggregateReportElement]("Report.getCumulatedHoursPerAssignmentForUsers", "userIds", userIds)

  override def getMinMaxDateTimesheetEntry: DateRange = {
    val results = findByNamedQuery[DateRange]("Report.getMinMaxTimesheetEntryDate")
    getFirstOr(results, new DateRange())
  }

  override def getCumulatedHoursPerActivityForUsers(userIds: util.List[Integer], projectIds: util.List[Integer]): util.List[ActivityAggregateReportElement] = {
    val keys = List("userIds", "projectIds")
    val params = List(userIds, projectIds)
    findByNamedQuery[ActivityAggregateReportElement]("Report.getCumulatedHoursPerAssignmentForUsersAndProjects", keys, params)
  }

  override def getCumulatedHoursPerActivityForUsers(userIds: util.List[Integer], projectIds: util.List[Integer], dateRange: DateRange): util.List[ActivityAggregateReportElement] = {
    val keys = List("dateStart", "dateEnd", "userIds", "projectIds")
    val params = List(dateRange.getDateStart, dateRange.getDateEnd, userIds, projectIds)
    findByNamedQuery[ActivityAggregateReportElement]("Report.getCumulatedHoursPerAssignmentOnDateForUsersAndProjects", keys, params)
  }

  override def getMinMaxDateTimesheetEntry(user: User): DateRange = {
    val results = findByNamedQuery[DateRange]("Report.getMinMaxTimesheetEntryDateForUser", "user", List(user))
    getFirstOr(results, new DateRange())
  }

  override def getCumulatedHoursPerActivity(dateRange: DateRange): util.List[ActivityAggregateReportElement] = {
    val keys = List("dateStart", "dateEnd")
    val params = List(dateRange.getDateStart, dateRange.getDateEnd)
    findByNamedQuery[ActivityAggregateReportElement]("Report.getCumulatedHoursPerAssignment", keys, params)
  }

  override def getCumulatedHoursPerActivityForProjects(projectIds: util.List[Integer], dateRange: DateRange): util.List[ActivityAggregateReportElement] = {
    val keys = List("dateStart", "dateEnd", "projectIds")
    val params = List(dateRange.getDateStart, dateRange.getDateEnd, projectIds)
    findByNamedQuery[ActivityAggregateReportElement]("Report.getCumulatedHoursPerAssignmentOnDateForProjects", keys, params)
  }

  override def getCumulatedHoursForActivity(activity: Activity): ActivityAggregateReportElement = {
    val results = findByNamedQuery[ActivityAggregateReportElement]("Report.getCumulatedHoursForActivity", "activity", List(activity))
    getFirstOr(results, null)
  }

  override def getCumulatedHoursPerActivityForActivities(projectActivityIds: util.List[Integer]): util.List[ActivityAggregateReportElement] = {
    findByNamedQuery[ActivityAggregateReportElement]("Report.getCumulatedHoursPerAssignmentForAssignmentIds", "assignmentIds", projectActivityIds)
  }

  override def getMinMaxDateTimesheetEntry(project: Project): DateRange = {
    val results = findByNamedQuery[DateRange]("Report.getMinMaxTimesheetEntryDateForProject", "project", List(project))
    getFirstOr(results, new DateRange)
  }

  /**
   * Added by LLI for Richemont
   * Get the min/max timesheet date for a assignment of a User
   * @param user
   * @param activity
   * @return
   */
  override def getMinMaxDateTimesheetEntryForUserAndAssignment(user: User, activity: Activity): DateRange = {
    val keys = List("user", "activity")
    val params = List(user, activity)

    val results = findByNamedQuery[DateRange]("Report.getMinMaxTimesheetEntryDateForUserAndAssignment",  keys, params)
    getFirstOr(results, new DateRange)
  }

  override def getActivitiesWithoutBookings(dateRange: DateRange): util.List[Activity] = {
    val keys = List("dateStart", "dateEnd")
    val params = List(dateRange.getDateStart, dateRange.getDateEnd)
    findByNamedQuery[Activity]("Report.getActivitiesWithoutBookings", keys, params)
  }

  private def getFirstOr[A](results: util.List[A], or: => A):A = {
    if (results.size > 0) results.get(0) else or
  }
}

