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

package net.rrm.ehour.persistence.report.dao;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;

/**
 * Reporting data operations 
 * @author Thies
 *
 */

public interface ReportAggregatedDao
{
	/**
	 * Get cumulated hours per {@link Activity} for user in a date range
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	public List<ActivityAggregateReportElement> getCumulatedHoursPerActivityForUsers(List<User> users, DateRange dateRange);

	/**
	 * Get cumulated hours per {@link Activity} for users
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	public List<ActivityAggregateReportElement> getCumulatedHoursPerActivityForUsers(List<User> users);
	
	/**
	 * Get cumulated hours per {@link Activity} for {@link Activity}s
	 * @param activityIds
	 * @return
	 */
	public List<ActivityAggregateReportElement> getCumulatedHoursPerActivityForActivities(List<? extends Serializable> activityIds);

	/**
	 * Get cumulative hours per {@link Activity} for users, projects
	 * @param userId
	 * @param projectId
	 * @return
	 */
	public List<ActivityAggregateReportElement> getCumulatedHoursPerActivityForUsers(List<User> users, List<Project> projects);

	/**
	 * Get cumulative hours per {@link Activity} for users, projects in a date range
	 * @param userId
	 * @param projectId
	 * @param dateRange
	 * @return
	 */
	public List<ActivityAggregateReportElement> getCumulatedHoursPerActivityForUsers(List<User> users, List<Project> projects, DateRange dateRange);

	/**
	 * Get cumulative hours per {@link Activity} for all projects in a date range
	 * @param projectId
	 * @param dateRange
	 * @return
	 */
	public List<ActivityAggregateReportElement> getCumulatedHoursPerActivityForProjects(List<Project> projects, DateRange dateRange);

	/**
	 * Get cumulative hours for an {@link Activity}
	 * @param activity
	 * @return
	 */
	public ActivityAggregateReportElement getCumulatedHoursForActivity(Activity activity);
	
	/**
	 * Get cumulative hours
	 * @param dateRange
	 * @return
	 */
	public List<ActivityAggregateReportElement> getCumulatedHoursPerActivity(DateRange dateRange);
	
	/**
	 * Get the min/max timesheet date
	 * @return
	 */
	public DateRange getMinMaxDateTimesheetEntry();
	
	/**
	 * Get the min/max timesheet date for a user
	 * @return
	 */
	public DateRange getMinMaxDateTimesheetEntry(User user);
	
	/**
	 * Get the min/max timesheet date for a project
	 * @param project
	 * @return
	 */
	public DateRange getMinMaxDateTimesheetEntry(Project project);
}
