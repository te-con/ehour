/**
 * Created on Nov 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.dao;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.project.ProjectAssignmentAggregate;

/**
 * Reporting data operations 
 * @author Thies
 *
 */

public interface ReportDAO
{
	/**
	 * Get cumulated hours per project assignment for user in a date range
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(Integer userId[], DateRange dateRange);

	/**
	 * Get cumulated hours per project assignment for users
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(Integer userId[]);

	/**
	 * Get cumulated hours per project assignment for users, projects
	 * @param userId
	 * @param projectId
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(Integer userId[], Integer projectId[]);

	/**
	 * Get cumulated hours per project assignment for users, projects in a date range
	 * @param userId
	 * @param projectId
	 * @param dateRange
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getCumulatedHoursPerAssignmentForUsers(Integer userId[], Integer projectId[], DateRange dateRange);

	/**
	 * Get the min/max timesheet date
	 * @return
	 */
	public DateRange getMinMaxDateTimesheetEntry();
	
	/**
	 * Get the min/max timesheet date for a user
	 * @return
	 */
	public DateRange getMinMaxDateTimesheetEntry(Integer userId);
}
