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
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.dao.ReportPerMonthDAO;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate;
import net.rrm.ehour.report.util.ReportUtil;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.domain.User;
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
	private	ReportAggregatedDAO	reportAggregatedDAO;
	private	ReportPerMonthDAO	reportPerMonthDAO;
	private	ProjectDAO			projectDAO;
	private	UserDAO				userDAO;

	private	Logger				logger = Logger.getLogger(this.getClass());
	
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
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getHoursPerAssignment(java.lang.Integer[])
	 */
	public List<ProjectAssignmentAggregate> getHoursPerAssignment(Integer[] projectAssignmentIds)
	{
		return reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(projectAssignmentIds);
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

		projectAssignmentAggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(new Integer[]{userId}, dateRange);

		return projectAssignmentAggregates;
	}	
	
	
	/**
	 * 
	 * @return
	 */
	public ReportData createReportData(ReportCriteria reportCriteria)
	{
		ReportData		reportData = new ReportData();
		UserCriteria	userCriteria;
		Integer[]		projectIds = null;
		Integer[]		userIds = null;
		boolean			ignoreUsers;
		boolean			ignoreProjects;
		DateRange		reportRange;
		
		userCriteria = reportCriteria.getUserCriteria();
		logger.debug("Creating report for " + userCriteria);
		
		reportRange = reportCriteria.getReportRange();
		
		ignoreUsers = userCriteria.isEmptyDepartments() && userCriteria.isEmptyUsers();
		ignoreProjects = userCriteria.isEmptyCustomers() && userCriteria.isEmptyProjects();
		
		if (ignoreProjects && ignoreUsers)
		{
			logger.debug("creating full report");
		}
		else if (ignoreProjects && !ignoreUsers)
		{
			logger.debug("creating report for only selected users");
			userIds = getUserIds(userCriteria);
		}
		else if (!ignoreProjects && ignoreUsers)
		{
			logger.debug("creating report for only selected project");
			projectIds = getProjectIds(userCriteria);
		}
		else
		{
			logger.debug("creating report for selected users & projects");
			userIds = getUserIds(userCriteria);
			projectIds = getProjectIds(userCriteria);
		}		
		
		reportData.setProjectAssignmentAggregates(getProjectAssignmentAggregates(userIds, projectIds, reportRange));
		reportData.setFlatProjectAssignmentAggregates(getWeeklyReportData(userIds, projectIds, reportRange));
		reportData.setReportCriteria(reportCriteria);
		
		return reportData;
	}
	
	/**
	 * Get project assignments aggregates
	 * @param reportCriteria
	 * @return
	 */
	private List<ProjectAssignmentAggregate> getProjectAssignmentAggregates(Integer[] userIds,
																			Integer[] projectIds,
																			DateRange reportRange)
	{
		List<ProjectAssignmentAggregate>	aggregates;
		
		if (userIds == null && projectIds == null)
		{
			aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignment(reportRange);
		}
		else if (projectIds == null && userIds != null)
		{
			aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(userIds, reportRange);
		}
		else if (projectIds != null && userIds == null)
		{
			aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(projectIds, reportRange);
		}
		else
		{
			aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(userIds, projectIds, reportRange);
		}
		
		return aggregates;
	}
	
	
	/**
	 * Get weekly project report
	 * @param criteria
	 * @return
	 */
	private List<FlatProjectAssignmentAggregate> getWeeklyReportData(Integer[] userIds,
																		Integer[] projectIds,
																		DateRange reportRange)
	{
		List<FlatProjectAssignmentAggregate> aggregates = null;

		if (userIds == null && projectIds == null)
		{
			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignment(reportRange);
		}
		else if (projectIds == null && userIds != null)
		{		
			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForUsers(Arrays.asList(userIds), reportRange);
		}
		else if (projectIds != null && userIds == null)
		{
			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForProjects(Arrays.asList(projectIds), reportRange);
		}
		else
		{
			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForUsers(Arrays.asList(userIds),
																					Arrays.asList(projectIds),
																					reportRange);
		}	
		
		return aggregates;
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
			if (!userCriteria.isEmptyCustomers())
			{
				logger.debug("Using customers to determine projects");
				projects = projectDAO.findProjectForCustomers(userCriteria.getCustomerIds(),
																userCriteria.isOnlyActiveProjects());
				
				projectIds = (Integer[])ReportUtil.getPKsFromDomainObjects(projects).toArray(new Integer[]{});
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
	 * Get user id's based on selected departments
	 * @param userCriteria
	 * @return
	 */
	private Integer[] getUserIds(UserCriteria userCriteria)
	{
		Integer[]	userIds;
		List<User>	users;
		
		if (userCriteria.isEmptyUsers())
		{
			if (!userCriteria.isEmptyDepartments())
			{
				logger.debug("Using departments to determine users");
				users = userDAO.findUsersForDepartments(null,
														userCriteria.getDepartmentIds(),
														userCriteria.isOnlyActiveUsers());
				userIds = (Integer[])ReportUtil.getPKsFromDomainObjects(users).toArray(new Integer[]{});
			}
			else
			{
				logger.debug("No departments or users selected");
				userIds = null;
			}
		}
		else
		{
			logger.debug("Using user provided users");
			userIds = userCriteria.getUserIds();
		}
		
		return userIds;
	}


	/**
	 * Create print report
	 */
	public List<FlatProjectAssignmentAggregate> getPrintReportData(List<Integer> projectAssignmentIds, DateRange dateRange)
	{
		List<FlatProjectAssignmentAggregate>	aggregates;
		
		aggregates = reportPerMonthDAO.getHoursPerDayForAssignment(projectAssignmentIds, dateRange);
		
		return aggregates;
	}
	
	
	/**
	 *  
	 *
	 */
	public void setReportAggregatedDAO(ReportAggregatedDAO reportAggregatedDAO)
	{
		this.reportAggregatedDAO = reportAggregatedDAO;
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

	/**
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO)
	{
		this.userDAO = userDAO;
	}
}
