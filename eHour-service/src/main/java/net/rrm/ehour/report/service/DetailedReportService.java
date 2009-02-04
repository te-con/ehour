/**
 * Created on Jan 20, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.service;

import java.util.Collection;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;

/**
 * Report service for detailed reports
 **/

public interface DetailedReportService
{
	/**
	 * Get report data
	 * @param projectAssignmentIds
	 * @param dateRange
	 * @return
	 */
	public ReportData<FlatReportElement> getDetailedReportData(Collection<ProjectAssignment> projectAssignments, DateRange dateRange);

	/**
	 * Get report data for customer 
	 * @param customer
	 * @param dateRange
	 * @return
	 */
	public ReportData<FlatReportElement> getDetailedReportData(ReportCriteria criteria);
}
