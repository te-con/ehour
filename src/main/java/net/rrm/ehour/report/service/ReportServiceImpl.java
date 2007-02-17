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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportDAO;
import net.rrm.ehour.report.dao.ReportPerMonthDAO;
import net.rrm.ehour.report.project.ProjectAssignmentAggregate;
import net.rrm.ehour.report.project.ProjectReport;
import net.rrm.ehour.report.project.WeeklyProjectAssignmentAggregate;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;
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
	private	ReportDAO				reportDAO;
	private	ReportPerMonthDAO		reportPerMonthDAO;
	private	UserDAO					userDAO;
	private	UserDepartmentDAO		userDepartmentDAO;
	private	CustomerDAO				customerDAO;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentDAO	projectAssignmentDAO;
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
	 * Update available report criteria 
	 */
	
	public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria)
	{
		UserCriteria		userCriteria = reportCriteria.getUserCriteria();
		AvailableCriteria	availCriteria = reportCriteria.getAvailableCriteria();
		
		if (userCriteria.getUserFilter() == UserCriteria.USER_SINGLE)
		{
			syncCriteriaForUsers(reportCriteria);
		}
		else
		{
			availCriteria.setUsers(getAvailableUsers(userCriteria));
				
			availCriteria.setUserDepartments(userDepartmentDAO.findAll());

			availCriteria.setCustomers(getAvailableCustomers(userCriteria));
			availCriteria.setProjects(getAvailableProjects(userCriteria));
			
			availCriteria.setReportRange(reportDAO.getMinMaxDateTimesheetEntry());
			
			// not entirely useful but for clarity
			reportCriteria.setAvailableCriteria(availCriteria);
		}	
		
		return reportCriteria;
	}
	
	/**
	 * Get available users
	 * @param userCriteria
	 * @return
	 */
	
	private List<User> getAvailableUsers(UserCriteria userCriteria)
	{
		List<User> users;
		
		switch (userCriteria.getUserFilter())
		{
			case UserCriteria.USER_ALL:
				users = userDAO.findAll();
				break;
			case UserCriteria.USER_ACTIVE:
				users = userDAO.findAllActiveUsers();
				break;
			default:
				users = null;
				break;
		}
		
		return users;
	}
	
	/**
	 * Get available customers
	 * @param userCriteria
	 * @return
	 */
	private List<Customer> getAvailableCustomers(UserCriteria userCriteria)
	{
		List<Customer> customers;
		
		if (userCriteria.isOnlyActiveCustomers())
		{
			customers = customerDAO.findAll(true);
		}
		else
		{
			customers = customerDAO.findAll();
		}
		
		return customers;
	}
	
	/**
	 * Get available projects depended on the userCriteria
	 * @param userCriteria
	 * @return
	 */
	private List<Project> getAvailableProjects(UserCriteria userCriteria)
	{
		List<Project>	projects;
		
		if (userCriteria.getCustomerIds() == null || 
			userCriteria.getCustomerIds().length == 0)
		{
			if (userCriteria.isOnlyActiveProjects())
			{
				logger.debug("Fetching only active projects");

				projects = projectDAO.findAllActive();
			}
			else
			{
				logger.debug("Fetching all projects");
				
				projects = projectDAO.findAll();
			}
		}
		else
		{
			logger.debug("Fetching projects for selected customers");
			
			projects = projectDAO.findProjectForCustomers(userCriteria.getCustomerIds(), userCriteria.isOnlyActiveProjects());
		}
		
		return projects;
	}
		
	/**
	 * Sync criteria for users, only customers & projects
	 * are displayed for users in this list
	 * @param reportCriteria
	 */
	private void syncCriteriaForUsers(ReportCriteria reportCriteria)
	{
		List<ProjectAssignment>	assignments = null;
		Set<Customer>			customers = new HashSet<Customer>();
		Set<Project>			projects = new HashSet<Project>();
		AvailableCriteria		availCriteria = reportCriteria.getAvailableCriteria();
		Integer					userId;
		
		userId = reportCriteria.getUserCriteria().getUserIds()[0];
		
		assignments = projectAssignmentDAO.findProjectAssignmentsForUser(userId);
		
		for (ProjectAssignment assignment : assignments)
		{
			customers.add(assignment.getProject().getCustomer());
			projects.add(assignment.getProject());
		}
		
		availCriteria.setCustomers(customers);
		availCriteria.setProjects(projects);
		
		availCriteria.setReportRange(reportDAO.getMinMaxDateTimesheetEntry(userId));
	}
	
	/**
	 * 
	 * @return
	 */
	public ProjectReport createProjectReport(ReportCriteria reportCriteria)
	{
		ProjectReport						projectReport = new ProjectReport();
		List<ProjectAssignmentAggregate>	aggregates;
		UserCriteria						userCriteria;
		
		userCriteria = reportCriteria.getUserCriteria();
		
		logger.debug("Creating project report for " + userCriteria);
	
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
		
		projectReport.setReportCriteria(reportCriteria);
		projectReport.initialize(aggregates);
		
		return projectReport;
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
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO)
	{
		this.userDAO = userDAO;
	}

	/**
	 * @param customerDAO the customerDAO to set
	 */
	public void setCustomerDAO(CustomerDAO customerDAO)
	{
		this.customerDAO = customerDAO;
	}

	/**
	 * @param projectDAO the projectDAO to set
	 */
	public void setProjectDAO(ProjectDAO projectDAO)
	{
		this.projectDAO = projectDAO;
	}

	/**
	 * @param userDepartmentDAO the userDepartmentDAO to set
	 */
	public void setUserDepartmentDAO(UserDepartmentDAO userDepartmentDAO)
	{
		this.userDepartmentDAO = userDepartmentDAO;
	}

	/**
	 * @param projectAssignmentDAO the projectAssignmentDAO to set
	 */
	public void setProjectAssignmentDAO(ProjectAssignmentDAO projectAssignmentDAO)
	{
		this.projectAssignmentDAO = projectAssignmentDAO;
	}

	/**
	 * @param reportPerMonthDAO the reportPerMonthDAO to set
	 */
	public void setReportPerMonthDAO(ReportPerMonthDAO reportPerMonthDAO)
	{
		this.reportPerMonthDAO = reportPerMonthDAO;
	}
}
