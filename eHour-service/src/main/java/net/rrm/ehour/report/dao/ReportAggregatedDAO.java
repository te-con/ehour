/**
 * Created on Nov 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.dao;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.user.domain.User;

/**
 * Reporting data operations 
 * @author Thies
 *
 */

public interface ReportAggregatedDAO
{
	/**
	 * Get cumulated hours per project assignment for user in a date range
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(List<User> users, DateRange dateRange);

	/**
	 * Get cumulated hours per project assignment for users
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(List<User> users);
	
	/**
	 * Get cumulated hours per project assignment for assignments
	 * @param projectAssignmentIds
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForAssignments(List<Integer> projectAssignmentIds);

	/**
	 * Get cumulated hours per project assignment for users, projects
	 * @param userId
	 * @param projectId
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(List<User> users, List<Project> projects);

	/**
	 * Get cumulated hours per project assignment for users, projects in a date range
	 * @param userId
	 * @param projectId
	 * @param dateRange
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(List<User> users, List<Project> projects, DateRange dateRange);

	/**
	 * Get cumulated hours per project assignment for all users, projects in a date range
	 * @param projectId
	 * @param dateRange
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForProjects(List<Project> projects, DateRange dateRange);

	/**
	 * Get cumulated hours for a project assignment
	 * @param userId
	 * @param projectAssignmentId
	 * @return
	 */
	public ProjectAssignmentAggregate getCumulatedHoursForAssignment(ProjectAssignment assignment);
	
	/**
	 * Get cumulated hours
	 * @param dateRange
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignment(DateRange dateRange);
	
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
