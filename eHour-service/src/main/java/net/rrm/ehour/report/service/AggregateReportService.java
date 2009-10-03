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

package net.rrm.ehour.report.service;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ProjectManagerDashboard;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;


/**
 * Provides reporting services on timesheets. * @author Thies
 *
 */

public interface AggregateReportService
{

	/**
	 * Get the booked hours per project assignment for a date range
	 * @param userId
	 * @param calendar
	 * @return List with projectReport objects
	 */

	public List<AssignmentAggregateReportElement> getHoursPerAssignmentInRange(Integer userId, DateRange dateRange);
	
	/**
	 * Get all booked hours for assignments
	 * @param projectAssignmentIds
	 * @return
	 */
	public List<AssignmentAggregateReportElement> getHoursPerAssignment(List<Serializable> projectAssignmentIds);
	
	/**
	 * Get aggregate report data
	 * @param criteria
	 * @return
	 */
	public ReportData getAggregateReportData(ReportCriteria criteria);
	
	/**
	 * Get project manager report
	 * @param reportCriteria
	 * @return
	 */
	public ProjectManagerReport getProjectManagerDetailedReport(DateRange reportRange, Integer projectId);
	
	
	/**
	 * Get dashboard for projects where user is PM
	 * @param reportRange
	 * @param projectId
	 * @return
	 */
	public ProjectManagerDashboard getProjectManagerDashboard(User user);
}
