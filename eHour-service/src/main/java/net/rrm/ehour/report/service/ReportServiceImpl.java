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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.mail.domain.MailLogAssignment;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.dao.ReportPerMonthDAO;
import net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportDataAggregate;
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
	private	MailService			mailService;
	private	ProjectAssignmentService	projectAssignmentService;
	
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
	public List<ProjectAssignmentAggregate> getHoursPerAssignment(List<Integer> projectAssignmentIds)
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

		List<Integer>	userIds = new ArrayList<Integer>();
		userIds.add(userId);
		projectAssignmentAggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(userIds, dateRange);

		return projectAssignmentAggregates;
	}	
	
	
	/**
	 * Create report data based on criteria 
	 * @return
	 */
	public ReportDataAggregate createAggregateReportData(ReportCriteria reportCriteria)
	{
		ReportDataAggregate		reportDataAggregate = new ReportDataAggregate();
		UserCriteria	userCriteria;
		List<Integer>	projectIds = null;
		List<Integer>	userIds = null;
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
		
		reportDataAggregate.setProjectAssignmentAggregates(getProjectAssignmentAggregates(userIds, projectIds, reportRange));
		reportDataAggregate.setFlatProjectAssignmentAggregates(getWeeklyReportData(userIds, projectIds, reportRange));
		reportDataAggregate.setReportCriteria(reportCriteria);
		
		return reportDataAggregate;
	}
	
	/**
	 * Get project assignments aggregates
	 * @param reportCriteria
	 * @return
	 */
	private List<ProjectAssignmentAggregate> getProjectAssignmentAggregates(List<Integer> userIds,
																			List<Integer >projectIds,
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
	private List<FlatProjectAssignmentAggregate> getWeeklyReportData(List<Integer> userIds,
																	 List<Integer> projectIds,
																		DateRange reportRange)
	{
		List<FlatProjectAssignmentAggregate> aggregates = null;

		if (userIds == null && projectIds == null)
		{
			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignment(reportRange);
		}
		else if (projectIds == null && userIds != null)
		{		
			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForUsers(userIds, reportRange);
		}
		else if (projectIds != null && userIds == null)
		{
			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForProjects(projectIds, reportRange);
		}
		else
		{
			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForUsers(userIds, projectIds, reportRange);
		}	
		
		return aggregates;
	}	

	/**
	 * Get project id's based on selected customers
	 * @param userCriteria
	 * @return
	 */
	private List<Integer> getProjectIds(UserCriteria userCriteria)
	{
		List<Integer>	projectIds;
		List<Project>	projects;
		
		
		// No projects selected by the user, use any given customer limitation 
		if (userCriteria.isEmptyProjects())
		{
			if (!userCriteria.isEmptyCustomers())
			{
				logger.debug("Using customers to determine projects");
				projects = projectDAO.findProjectForCustomers(userCriteria.getCustomerIds(),
																userCriteria.isOnlyActiveProjects());
				
				projectIds = ReportUtil.getPKsFromDomainObjects(projects);
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
	private List<Integer> getUserIds(UserCriteria userCriteria)
	{
		List<Integer>	userIds;
		List<User>		users;
		
		if (userCriteria.isEmptyUsers())
		{
			if (!userCriteria.isEmptyDepartments())
			{
				logger.debug("Using departments to determine users");
				users = userDAO.findUsersForDepartments(null,
														userCriteria.getDepartmentIds(),
														userCriteria.isOnlyActiveUsers());
				userIds = ReportUtil.getPKsFromDomainObjects(users);
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
	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getProjectManagerReport(net.rrm.ehour.data.DateRange, java.lang.Integer)
	 */
	public ProjectManagerReport getProjectManagerReport(DateRange reportRange, Integer projectId)
	{
		ProjectManagerReport	report = new ProjectManagerReport();
		SortedSet<ProjectAssignmentAggregate>	aggregates;
		List<MailLogAssignment>	sentMail;
		Project					project;
		List<Integer>			assignmentIds = new ArrayList<Integer>();
		List<ProjectAssignment>	allAssignments;
		ProjectAssignmentAggregate	emptyAggregate;
		
		// get the project
		project = projectDAO.findById(projectId);
		report.setProject(project);
		logger.debug("PM report for project " + project.getName());
		
		// get a proper report range
		reportRange = getReportRangeForProject(reportRange, project);
		report.setReportRange(reportRange);
		
		// get all aggregates
		List<Integer>	projectIds = new ArrayList<Integer>();
		projectIds.add(projectId);
		aggregates = new TreeSet<ProjectAssignmentAggregate>(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(projectIds, reportRange));

		// filter out just the id's
		for (ProjectAssignmentAggregate aggregate : aggregates)
		{
			assignmentIds.add(aggregate.getProjectAssignment().getAssignmentId());
		}		
		
		// get all assignments for this period regardless whether they booked hours on it
		allAssignments = projectAssignmentService.getProjectAssignments(project, reportRange);
		
		for (ProjectAssignment assignment : allAssignments)
		{
			if (!assignmentIds.contains(assignment.getAssignmentId()))
			{
				emptyAggregate = new ProjectAssignmentAggregate();
				emptyAggregate.setProjectAssignment(assignment);
			
				aggregates.add(emptyAggregate);	
			}
		}
		
		report.setAggregates(aggregates);
		

		// get mail sent for this project
		if (assignmentIds.size() > 0)
		{
			sentMail = mailService.getSentMailForAssignment((Integer[])assignmentIds.toArray(new Integer[assignmentIds.size()]));
			report.setSentMail(new TreeSet<MailLogAssignment>(sentMail));
		}
		
		report.deriveTotals();
		
		return report;
	}
	
	/**
	 * Get report range for project
	 * @param reportRange
	 * @param project
	 * @return
	 */
	private DateRange getReportRangeForProject(DateRange reportRange, Project project)
	{
		DateRange	minMaxRange;
		
		if (reportRange.getDateStart() == null || reportRange.getDateEnd() == null)
		{
			minMaxRange = reportAggregatedDAO.getMinMaxDateTimesheetEntry(project);
			
			if (reportRange.getDateStart() == null)
			{
				reportRange.setDateStart(minMaxRange.getDateStart());
			}

			if (reportRange.getDateEnd() == null)
			{
				reportRange.setDateEnd(minMaxRange.getDateEnd());
			}
		}
		
		logger.debug("Used date range for pm report: " + reportRange + " on report " + project);
		
		return reportRange;
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

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService)
	{
		this.mailService = mailService;
	}

	/**
	 * @param projectAssignmentService the projectAssignmentService to set
	 */
	public void setProjectAssignmentService(ProjectAssignmentService projectAssignmentService)
	{
		this.projectAssignmentService = projectAssignmentService;
	}
}
