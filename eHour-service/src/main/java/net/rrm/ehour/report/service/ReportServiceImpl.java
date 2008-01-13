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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.mail.domain.MailLogAssignment;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.dao.ReportPerMonthDAO;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.util.ReportUtil;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * Provides reporting services on timesheets. * @author Thies
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
	private ProjectAssignmentDAO	projectAssignmentDAO;
	
	private	Logger				logger = Logger.getLogger(this.getClass());
	
	/**
	 * Get the booked hours per project assignment for a month
	 * @param userId
	 * @param 
	 * @return SortedMap with ProjectAssignment on key and a Float representation of the booked hours as value
	 */	
	
	public List<AssignmentAggregateReportElement> getHoursPerAssignmentInMonth(Integer userId, Calendar requestedDate)
	{
		DateRange	monthRange;
		
		monthRange = DateUtil.calendarToMonthRange(requestedDate);

		return getHoursPerAssignmentInRange(userId, monthRange);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getHoursPerAssignment(java.lang.Integer[])
	 */
	public List<AssignmentAggregateReportElement> getHoursPerAssignment(List<Integer> projectAssignmentIds)
	{
		return reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(projectAssignmentIds);
	}
	
	/**
	 * Get the booked hours per project assignment for a date range
	 * @param userId
	 * @param
	 * @return 
	 */		
	public List<AssignmentAggregateReportElement> getHoursPerAssignmentInRange(Integer userId, DateRange dateRange)
	{
		List<AssignmentAggregateReportElement>	assignmentAggregateReportElements;

		List<User>	users = new ArrayList<User>();
		users.add(new User(userId));
		assignmentAggregateReportElements = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(users, dateRange);

		return assignmentAggregateReportElements;
	}	
	
	
	/**
	 * Create report data based on criteria 
	 * @return
	 */
	public ReportData createAggregateReportData(ReportCriteria reportCriteria)
	{
		ReportData reportData = new ReportData();
		UserCriteria	userCriteria;
		List<Project>	projects = null;
		List<User>		users = null;
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
			users = getUsers(userCriteria);
		}
		else if (!ignoreProjects && ignoreUsers)
		{
			logger.debug("creating report for only selected project");
			projects = getProjects(userCriteria);
		}
		else
		{
			logger.debug("creating report for selected users & projects");
			users = getUsers(userCriteria);
			projects = getProjects(userCriteria);
		}		
		
		reportData.setReportElements(getProjectAssignmentAggregates(users, projects, reportRange));
		reportData.setReportCriteria(reportCriteria);
		
		return reportData;
	}
	
	/**
	 * Get project assignments aggregates
	 * @param
	 * @return
	 */
	private List<AssignmentAggregateReportElement> getProjectAssignmentAggregates(List<User> users,
																			List<Project >projects,
																			DateRange reportRange)
	{
		List<AssignmentAggregateReportElement>	aggregates;
		
		if (users == null && projects == null)
		{
			aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignment(reportRange);
		}
		else if (projects == null && users != null)
		{
			aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(users, reportRange);
		}
		else if (projects != null && users == null)
		{
			aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(projects, reportRange);
		}
		else
		{
			aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(users, projects, reportRange);
		}
		
		return aggregates;
	}
	
	
	/**
	 * Get weekly project report
	 * @param 
	 * @return
	 */
//	private List<FlatProjectAssignmentAggregate> getWeeklyReportData(List<User> users,
//																	 List<Project> projects,
//																		DateRange reportRange)
//	{
//		List<FlatProjectAssignmentAggregate> aggregates = null;
//
//		if (users == null && projects == null)
//		{
//			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignment(reportRange);
//		}
//		else if (projects == null && users != null)
//		{		
//			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForUsers(ReportUtil.getPKsFromDomainObjects(users), reportRange);
//		}
//		else if (projects != null && users == null)
//		{
//			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForProjects(ReportUtil.getPKsFromDomainObjects(projects), reportRange);
//		}
//		else
//		{
//			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForUsers(ReportUtil.getPKsFromDomainObjects(users),
//																					ReportUtil.getPKsFromDomainObjects(projects), reportRange);
//		}	
//		
//		return aggregates;
//	}	

	/**
	 * Get project id's based on selected customers
	 * @param userCriteria
	 * @return
	 */
	private List<Project> getProjects(UserCriteria userCriteria)
	{
		List<Project>	projects;
		
		// No projects selected by the user, use any given customer limitation 
		if (userCriteria.isEmptyProjects())
		{
			if (!userCriteria.isEmptyCustomers())
			{
				logger.debug("Using customers to determine projects");
				projects = projectDAO.findProjectForCustomers(userCriteria.getCustomers(),
																userCriteria.isOnlyActiveProjects());
			}
			else
			{
				logger.debug("No customers or projects selected");
				projects = null;
			}
		}
		else
		{
			logger.debug("Using user provided projects");
			projects = userCriteria.getProjects();
		}
		
		return projects;
	}
	
	/**
	 * Get users based on selected departments
	 * @param userCriteria
	 * @return
	 */
	private List<User> getUsers(UserCriteria userCriteria)
	{
		List<User>		users;
		
		if (userCriteria.isEmptyUsers())
		{
			if (!userCriteria.isEmptyDepartments())
			{
				logger.debug("Using departments to determine users");
				users = userDAO.findUsersForDepartments(null,
														userCriteria.getDepartments(),
														userCriteria.isOnlyActiveUsers());
			}
			else
			{
				logger.debug("No departments or users selected");
				users = null;
			}
		}
		else
		{
			logger.debug("Using user provided users");
			users = userCriteria.getUsers();
		}
		
		return users;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getProjectManagerReport(net.rrm.ehour.data.DateRange, java.lang.Integer)
	 */
	public ProjectManagerReport getProjectManagerReport(DateRange reportRange, Integer projectId)
	{
		ProjectManagerReport	report = new ProjectManagerReport();
		SortedSet<AssignmentAggregateReportElement>	aggregates;
		List<MailLogAssignment>	sentMail;
		Project					project;
		List<Integer>			assignmentIds = new ArrayList<Integer>();
		List<ProjectAssignment>	allAssignments;
		AssignmentAggregateReportElement	emptyAggregate;
		
		// get the project
		project = projectDAO.findById(projectId);
		report.setProject(project);
		logger.debug("PM report for project " + project.getName());
		
		// get a proper report range
		reportRange = getReportRangeForProject(reportRange, project);
		report.setReportRange(reportRange);
		
		// get all aggregates
		List<Project>	projects = new ArrayList<Project>();
		projects.add(new Project(projectId));
		aggregates = new TreeSet<AssignmentAggregateReportElement>(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(projects, reportRange));

		// filter out just the id's
		for (AssignmentAggregateReportElement aggregate : aggregates)
		{
			assignmentIds.add(aggregate.getProjectAssignment().getAssignmentId());
		}		
		
		// get all assignments for this period regardless whether they booked hours on it
		allAssignments = projectAssignmentService.getProjectAssignments(project, reportRange);
		
		for (ProjectAssignment assignment : allAssignments)
		{
			if (!assignmentIds.contains(assignment.getAssignmentId()))
			{
				emptyAggregate = new AssignmentAggregateReportElement();
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

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getReportData(net.rrm.ehour.customer.domain.Customer, net.rrm.ehour.data.DateRange)
	 */
	public List<FlatReportElement> getReportData(Customer customer, DateRange dateRange)
	{
		List<ProjectAssignment> assignments = projectAssignmentDAO.findProjectAssignmentsForCustomer(customer, dateRange);
		List<Serializable> assignmentIds = ReportUtil.getPKsFromDomainObjects(assignments);
		
		if (assignmentIds.isEmpty())
		{
			return new ArrayList<FlatReportElement>();
		}
		else
		{
			return getReportData(assignmentIds, dateRange);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getReportData(java.util.List, net.rrm.ehour.data.DateRange)
	 */
	public List<FlatReportElement> getReportData(List<Serializable> projectAssignmentIds, DateRange dateRange)
	{
		return reportPerMonthDAO.getHoursPerDayForAssignment(projectAssignmentIds, dateRange);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getReportData(net.rrm.ehour.project.domain.Project, net.rrm.ehour.data.DateRange)
	 */
	public List<FlatReportElement> getReportData(Project[] projects, DateRange dateRange)
	{
		List<Serializable> assignmentIds = new ArrayList<Serializable>();
		
		for (Project project : projects)
		{
			List<ProjectAssignment> assignments = projectAssignmentService.getProjectAssignments(project, dateRange);
			assignmentIds.addAll(ReportUtil.getPKsFromDomainObjects(assignments));
		}

		return getReportData(assignmentIds, dateRange);
	}

	/**
	 * @param projectAssignmentDAO the projectAssignmentDAO to set
	 */
	public void setProjectAssignmentDAO(ProjectAssignmentDAO projectAssignmentDAO)
	{
		this.projectAssignmentDAO = projectAssignmentDAO;
	}
}
