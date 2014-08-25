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
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportData;
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
