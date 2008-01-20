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
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.DetailedReportDAO;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.user.domain.User;

import org.apache.log4j.Logger;

/**
 * Report service for detailed reports implementation
 **/

public class DetailedReportServiceImpl implements DetailedReportService
{
	private	DetailedReportDAO	detailedReportDAO;
	private	Logger				logger = Logger.getLogger(DetailedReportServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.DetailedReportService#getDetailedReportData(net.rrm.ehour.report.criteria.ReportCriteria)
	 */
	public List<FlatReportElement> getDetailedReportData(ReportCriteria reportCriteria)
	{
//		UserCriteria	userCriteria;
//		List<Project>	projects = null;
//		List<User>		users = null;
//		boolean			ignoreUsers;
//		boolean			ignoreProjects;
//		DateRange		reportRange;
//		
//		userCriteria = reportCriteria.getUserCriteria();
//		logger.debug("Getting detailed report data for " + userCriteria);
//		
//		reportRange = reportCriteria.getReportRange();
//		
//		ignoreUsers = userCriteria.isEmptyDepartments() && userCriteria.isEmptyUsers();
//		ignoreProjects = userCriteria.isEmptyCustomers() && userCriteria.isEmptyProjects();
//		
//		if (ignoreProjects && ignoreUsers)
//		{
//			logger.debug("creating full report");
//		}
//		else if (ignoreProjects && !ignoreUsers)
//		{
//			logger.debug("creating report for only selected users");
//			users = getUsers(userCriteria);
//		}
//		else if (!ignoreProjects && ignoreUsers)
//		{
//			logger.debug("creating report for only selected project");
//			projects = getProjects(userCriteria);
//		}
//		else
//		{
//			logger.debug("creating report for selected users & projects");
//			users = getUsers(userCriteria);
//			projects = getProjects(userCriteria);
//		}		
//		
//		reportData.setReportElements(getProjectAssignmentAggregates(users, projects, reportRange));
//		reportData.setReportCriteria(reportCriteria);
//		
//		return reportData;
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getReportData(java.util.List, net.rrm.ehour.data.DateRange)
	 */
	public List<FlatReportElement> getDetailedReportData(List<Serializable> projectAssignmentIds, DateRange dateRange)
	{
		return detailedReportDAO.getHoursPerDayForAssignment(projectAssignmentIds, dateRange);
	}

	/**
	 * @param detailedReportDAO the detailedReportDAO to set
	 */
	public void setDetailedReportDAO(DetailedReportDAO detailedReportDAO)
	{
		this.detailedReportDAO = detailedReportDAO;
	}
}
