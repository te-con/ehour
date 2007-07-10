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

package net.rrm.ehour.report.service;

import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportDataAggregate;


/**
 * Provides reporting services on timesheets.
 * 
 * @author Thies
 *
 */

public interface ReportService
{
	
	/**
	 * Get the booked hours per project assignment for a month
	 * @param userId
	 * @param calendar
	 * @return List with projectReport objects
	 */
	public List<ProjectAssignmentAggregate> getHoursPerAssignmentInMonth(Integer userId, Calendar calendar);

	/**
	 * Get the booked hours per project assignment for a date range
	 * @param userId
	 * @param calendar
	 * @return List with projectReport objects
	 */

	public List<ProjectAssignmentAggregate> getHoursPerAssignmentInRange(Integer userId, DateRange dateRange);
	
	/**
	 * Get all booked hours for assignments
	 * @param projectAssignmentIds
	 * @return
	 */
	public List<ProjectAssignmentAggregate> getHoursPerAssignment(List<Integer> projectAssignmentIds);
	
	/**
	 * Get project report for reporting role
	 * @param criteria
	 * @return
	 */
	public ReportDataAggregate createAggregateReportData(ReportCriteria criteria);
	
	
	/**
	 * Create report for printing
	 * @param projectAssignmentIds
	 * @param dateRange
	 * @return
	 */
	public List<FlatProjectAssignmentAggregate> getPrintReportData(List<Integer> projectAssignmentIds, DateRange dateRange);
	
	/**
	 * Get project manager report
	 * @param reportCriteria
	 * @return
	 */
	public ProjectManagerReport getProjectManagerReport(DateRange reportRange, Integer projectId);
}
