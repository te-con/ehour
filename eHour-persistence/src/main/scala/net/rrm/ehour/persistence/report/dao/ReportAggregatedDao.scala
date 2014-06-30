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
import net.rrm.ehour.domain.{Project, ProjectAssignment, User}
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement

/**
 * Reporting data operations
 *
 * @author Thies
 */
trait ReportAggregatedDao {
  /**
   * Get cumulated hours per project assignment for user in a date range
   */
  def getCumulatedHoursPerAssignmentForUsers(users: util.List[User], dateRange: DateRange): util.List[AssignmentAggregateReportElement]

  /**
   * Get cumulated hours per project assignment for users
   */
  def getCumulatedHoursPerAssignmentForUsers(users: util.List[User]): util.List[AssignmentAggregateReportElement]

  /**
   * Get cumulated hours per project assignment for assignments
   */
  def getCumulatedHoursPerAssignmentForAssignments(projectAssignmentIds: util.List[Integer]): util.List[AssignmentAggregateReportElement]

  /**
   * Get cumulated hours per project assignment for users, projects
   */
  def getCumulatedHoursPerAssignmentForUsers(users: util.List[User], projects: util.List[Project]): util.List[AssignmentAggregateReportElement]

  /**
   * Get cumulated hours per project assignment for users, projects in a date range
   */
  def getCumulatedHoursPerAssignmentForUsers(users: util.List[User], projects: util.List[Project], dateRange: DateRange): util.List[AssignmentAggregateReportElement]

  /**
   * Get cumulated hours per project assignment for all users, projects in a date range
   */
  def getCumulatedHoursPerAssignmentForProjects(projects: util.List[Project], dateRange: DateRange): util.List[AssignmentAggregateReportElement]

  /**
   * Get cumulated hours for a project assignment
   */
  def getCumulatedHoursForAssignment(assignment: ProjectAssignment): AssignmentAggregateReportElement

  /**
   * Get cumulated hours
   */
  def getCumulatedHoursPerAssignment(dateRange: DateRange): util.List[AssignmentAggregateReportElement]

  /**
   * Get the min/max timesheet date
   */
  def getMinMaxDateTimesheetEntry: DateRange

  /**
   * Get the min/max timesheet date for a user
   */
  def getMinMaxDateTimesheetEntry(user: User): DateRange

  /**
   * Get the min/max timesheet date for a project
   */
  def getMinMaxDateTimesheetEntry(project: Project): DateRange

  /**
   * Get assignments without bookings for a particular range
   */
  def getAssignmentsWithoutBookings(dateRange: DateRange): util.List[ProjectAssignment]
}

