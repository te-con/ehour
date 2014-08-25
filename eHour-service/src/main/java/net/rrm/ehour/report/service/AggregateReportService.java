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

import net.rrm.ehour.data.DateRange;
<<<<<<< HEAD
import net.rrm.ehour.domain.Project;
=======
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.User;
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportData;
<<<<<<< HEAD
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import java.util.List;


/**
 * Provides reporting services on timesheets. * @author Thies
 */

public interface AggregateReportService {

    /**
     * Get the booked hours per project assignment for a date range
     *
     * @param userId
     * @param calendar
     * @return List with projectReport objects
     */

    List<AssignmentAggregateReportElement> getHoursPerAssignmentInRange(Integer userId, DateRange dateRange);

    /**
     * Get all booked hours for assignments
     *
     * @param projectAssignmentIds
     * @return
     */
    List<AssignmentAggregateReportElement> getHoursPerAssignment(List<Integer> projectAssignmentIds);

    /**
     * Get aggregate report data
     *
     * @param criteria
     * @return
     */
    ReportData getAggregateReportData(ReportCriteria criteria);

    /**
     * Get project manager report
     *
     * @return
     */
    ProjectManagerReport getProjectManagerDetailedReport(Project project);
}
=======
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;


/**
 * Provides reporting services on timesheets. * @author Thies
 *
 */

public interface AggregateReportService
{

	/**
	 * Get the booked hours per {@link Activity} for a date range
	 * @param userId
	 * @param calendar
	 * @return List with projectReport objects
	 */

	public List<ActivityAggregateReportElement> getHoursPerActivityInRange(Integer userId, DateRange dateRange);
	
	/**
	 * Get all booked hours for {@link Activity}s
	 * @param activityIds
	 * @return
	 */
	public List<ActivityAggregateReportElement> getHoursPerActivity(List<? extends Serializable> activityIds);
	
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
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
