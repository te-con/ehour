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

package net.rrm.ehour.report.service;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
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
	public List<AssignmentAggregateReportElement> getHoursPerAssignment(List<Integer> projectAssignmentIds);
	
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
	public ProjectManagerReport getProjectManagerReport(DateRange reportRange, Integer projectId);
}
