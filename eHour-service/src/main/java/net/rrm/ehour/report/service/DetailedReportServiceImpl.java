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

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.dao.ReportPerMonthDAO;
import net.rrm.ehour.report.reports.element.FlatReportElement;

import org.apache.log4j.Logger;

/**
 * Report service for detailed reports implementation
 **/

public class DetailedReportServiceImpl implements DetailedReportService
{
	private	ReportPerMonthDAO	reportPerMonthDAO;
	private	Logger				logger = Logger.getLogger(DetailedReportServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.DetailedReportService#getDetailedReportData(net.rrm.ehour.report.criteria.ReportCriteria)
	 */
	public List<FlatReportElement> getDetailedReportData(ReportCriteria reportCriteria)
	{
		logger.debug("Getting detailed report data");
//		List<ProjectAssignment> assignments = projectAssignmentDAO.findProjectAssignmentsForCustomer(customer, dateRange);
//		List<Serializable> assignmentIds = ReportUtil.getPKsFromDomainObjects(assignments);
//		
//		if (assignmentIds.isEmpty())
//		{
//			return new ArrayList<FlatReportElement>();
//		}
//		else
//		{
//			return getDetailedReportData(assignmentIds, dateRange);
//		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getReportData(java.util.List, net.rrm.ehour.data.DateRange)
	 */
	public List<FlatReportElement> getDetailedReportData(List<Serializable> projectAssignmentIds, DateRange dateRange)
	{
		return reportPerMonthDAO.getHoursPerDayForAssignment(projectAssignmentIds, dateRange);
	}

	/**
	 * @param reportPerMonthDAO the reportPerMonthDAO to set
	 */
	public void setReportPerMonthDAO(ReportPerMonthDAO reportPerMonthDAO)
	{
		this.reportPerMonthDAO = reportPerMonthDAO;
	}
}
