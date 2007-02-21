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

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportDAO;
import net.rrm.ehour.report.dao.ReportPerMonthDAO;
import net.rrm.ehour.report.project.AssignmentReport;
import net.rrm.ehour.report.project.ProjectAssignmentAggregate;
import net.rrm.ehour.report.project.WeeklyProjectAssignmentAggregate;
import net.rrm.ehour.report.util.ReportUtil;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * Provides reporting services on timesheets.
 * 
 * @author Thies
 *
 */

public class ReportServiceImpl implements ReportService
{
	private	ReportDAO				reportDAO;
	private	ReportPerMonthDAO		reportPerMonthDAO;
	private	ProjectDAO				projectDAO;

	private	Logger					logger = Logger.getLogger(this.getClass());
	
	/**
	 * Get the booked hours per project assignment for a month
	 * @param userId
	 * @param calendar
	 * @return SortedMap with ProjectAssignment on key and a Float representation of the booked hours as value
	 */	
	
	public List<ProjectAssignmentAggregate> getHoursPerAssignmentInMonth(Integer userId, Calendar requestedDate)
	{
		DateRange	monthRange;
		
		monthRange = DateUtil.calendarToMonthRange(requestedDate);

		return getHoursPerAssignmentInRange(userId, monthRange);
	}
	
	/**
	 * Get the booked hours per project assignment for a date range
	 * @param userId
	 * @param calendar
	 * @return 
	 */		
	public List<ProjectAssignmentAggregate> getHoursPerAssignmentInRange(Integer userId, DateRange dateRange)
	{
		List<ProjectAssignmentAggregate>	projectAssignmentAggregates;

		projectAssignmentAggregates = reportDAO.getCumulatedHoursPerAssignmentForUsers(new Integer[]{userId}, dateRange);

		return projectAssignmentAggregates;
	}	
	
	
	/**
	 * 
	 * @return
	 */
	public AssignmentReport createAssignmentReport(ReportCriteria reportCriteria)
	{
		AssignmentReport					assignmentReport = new AssignmentReport();
		List<ProjectAssignmentAggregate>	aggregates;
		UserCriteria						userCriteria;
		
		userCriteria = reportCriteria.getUserCriteria();
		
		logger.debug("Creating assignment report for " + userCriteria);
	
		if (userCriteria.getProjectIds() == null)
		{
			aggregates = reportDAO.getCumulatedHoursPerAssignmentForUsers(userCriteria.getUserIds(),
																			reportCriteria.getReportRange());
		}
		else
		{
			aggregates = reportDAO.getCumulatedHoursPerAssignmentForUsers(userCriteria.getUserIds(),
					userCriteria.getProjectIds(),
					reportCriteria.getReportRange());
		}
		
		assignmentReport.setReportCriteria(reportCriteria);
		assignmentReport.initialize(aggregates);
		
		return assignmentReport;
	}

	/**
	 * Get project id's based on selected customers
	 * @param userCriteria
	 * @return
	 */
	private Integer[] getProjectIds(UserCriteria userCriteria)
	{
		Integer[]		projectIds;
		List<Project>	projects;
		
		// No projects selected by the user, use any given customer limitation 
		if (userCriteria.isEmptyProjects())
		{
			if (!userCriteria.isEmptyProjects())
			{
				logger.debug("No customers or projects selected");
				projects = projectDAO.findProjectForCustomers(userCriteria.getCustomerIds(), userCriteria.isOnlyActiveProjects());
				projectIds = ReportUtil.getProjectIds(projects);
			}
			else
			{
				logger.debug("No customers or projects selected");
				projectIds = null;
			}
		}
		else
		{
			logger.debug("Using user provided projects");
			projectIds = userCriteria.getProjectIds();
		}
		
		return projectIds;
	}
	
	/**
	 * Get weekly project report
	 * @param criteria
	 * @return
	 */
	public List<WeeklyProjectAssignmentAggregate> createWeeklyProjectReport(ReportCriteria reportCriteria)
	{
		UserCriteria userCriteria = reportCriteria.getUserCriteria();
		List<WeeklyProjectAssignmentAggregate> aggregates = null;
		
		if (userCriteria.getProjectIds() == null)
		{
			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForUsers(Arrays.asList(userCriteria.getUserIds()), 
																					reportCriteria.getReportRange());
		}
		else
		{
			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForUsers(Arrays.asList(userCriteria.getUserIds()),
																					Arrays.asList(userCriteria.getProjectIds()),
																					reportCriteria.getReportRange());
		}	
		
		return aggregates;
	}
	
	/**
	 *  
	 *
	 */
	public void setReportDAO(ReportDAO reportDAO)
	{
		this.reportDAO = reportDAO;
	}

	/**
	 * @param reportPerMonthDAO the reportPerMonthDAO to set
	 */
	public void setReportPerMonthDAO(ReportPerMonthDAO reportPerMonthDAO)
	{
		this.reportPerMonthDAO = reportPerMonthDAO;
	}

	/**
	 * @param projectDAO the projectDAO to set
	 */
	public void setProjectDAO(ProjectDAO projectDAO)
	{
		this.projectDAO = projectDAO;
	}
}
