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
import net.rrm.ehour.report.project.ProjectAssignmentAggregate;
import net.rrm.ehour.report.project.ProjectReport;
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
		List<User>			users;
		List<Customer>		customers;
		List<Project>		projects;
		UserCriteria		userCriteria = reportCriteria.getUserCriteria();
		AvailableCriteria	availCriteria = reportCriteria.getAvailableCriteria();
		
		if (userCriteria.getUserFilter() == UserCriteria.USER_SINGLE)
		{
			syncCriteriaForUsers(reportCriteria);
		}
		else
		{
			// determine users
			switch (userCriteria.getUserFilter())
			{
	//			case UserCriteria.USER_SINGLE:
	//				users = new ArrayList<User>();
	//				users.add(new User(userCriteria.getUserIds()[0]));
	//				break;
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
			
			availCriteria.setUsers(users);
				
			availCriteria.setUserDepartments(userDepartmentDAO.findAll());

			// customers
			if (userCriteria.isOnlyActiveCustomers())
			{
				customers = customerDAO.findAll(true);
			}
			else
			{
				customers = customerDAO.findAll();
			}
				
			availCriteria.setCustomers(customers);
				
			// projects
			if (userCriteria.isOnlyActiveProjects())
			{
				projects = projectDAO.findAllActive();
			}
			else
			{
				projects = projectDAO.findAll();
			}
			
			availCriteria.setProjects(projects);
			
			// not entirely useful but for clarity
			reportCriteria.setAvailableCriteria(availCriteria);
		}	
		
		return reportCriteria;
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
		DateRange							reportRange;
		UserCriteria						criteria;
		
		criteria = reportCriteria.getUserCriteria();
		
		logger.debug("Creating project report for " + criteria);
		
		if (criteria.getReportRange() == null)
		{
			reportRange = reportCriteria.getAvailableCriteria().getReportRange();
		}
		else
		{
			reportRange = criteria.getReportRange();
		}
		
		if (criteria.getProjectIds() == null)
		{
			aggregates = reportDAO.getCumulatedHoursPerAssignmentForUsers(criteria.getUserIds(),
																			reportRange);
		}
		else
		{
			aggregates = reportDAO.getCumulatedHoursPerAssignmentForUsers(criteria.getUserIds(),
					criteria.getProjectIds(),
					reportRange);
		}
		
		projectReport.setUserCriteria(criteria);
		projectReport.initialize(aggregates);
		
		return projectReport;
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


}
