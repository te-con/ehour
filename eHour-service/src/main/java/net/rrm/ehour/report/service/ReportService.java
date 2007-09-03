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

import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportDataAggregate;


/**
 * Provides reporting services on timesheets. * @author Thies
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
