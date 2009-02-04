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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.dao.DetailedReportDAO;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.util.EhourUtil;

/**
 * Report service for detailed reports implementation
 **/

public class DetailedReportServiceImpl extends AbstractReportServiceImpl<FlatReportElement>
										implements DetailedReportService
{
	private	DetailedReportDAO	detailedReportDAO;

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.DetailedReportService#getDetailedReportData(net.rrm.ehour.report.criteria.ReportCriteria)
	 */
	public ReportData<FlatReportElement> getDetailedReportData(ReportCriteria reportCriteria)
	{
		return getReportData(reportCriteria);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.AbstractReportServiceImpl#getReportElements(java.util.List, java.util.List, net.rrm.ehour.data.DateRange)
	 */
	protected List<FlatReportElement> getReportElements(List<User> users,
														List<Project >projects,
														DateRange reportRange)
	{
		List<FlatReportElement>	elements = new ArrayList<FlatReportElement>();
		
		if (users == null && projects == null)
		{
			elements = detailedReportDAO.getHoursPerDay(reportRange);
		}
		else if (projects == null && users != null)
		{
			elements = detailedReportDAO.getHoursPerDayForUsers(EhourUtil.getIdsFromDomainObjects(users), reportRange);
		}
		else if (projects != null && users == null)
		{
			elements = detailedReportDAO.getHoursPerDayForProjects(EhourUtil.getIdsFromDomainObjects(projects), reportRange);
		}
		else
		{
			elements = detailedReportDAO.getHoursPerDayForProjectsAndUsers(EhourUtil.getIdsFromDomainObjects(projects),
																			EhourUtil.getIdsFromDomainObjects(users),
																			reportRange);
		}
		
		return elements;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getReportData(java.util.List, net.rrm.ehour.data.DateRange)
	 */
	public ReportData<FlatReportElement> getDetailedReportData(Collection<ProjectAssignment> projectAssignments, DateRange dateRange)
	{
		ReportData<FlatReportElement> reportData;
		
		reportData = new ReportData<FlatReportElement>();
		reportData.setReportElements(detailedReportDAO.getHoursPerDayForAssignment(EhourUtil.getIdsFromDomainObjects(projectAssignments), dateRange));
		
		return reportData;
	}

	/**
	 * @param detailedReportDAO the detailedReportDAO to set
	 */
	public void setDetailedReportDAO(DetailedReportDAO detailedReportDAO)
	{
		this.detailedReportDAO = detailedReportDAO;
	}
}
