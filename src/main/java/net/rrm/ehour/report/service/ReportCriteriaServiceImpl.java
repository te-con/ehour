/**
 * Created on 21-feb-2007
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;
import net.rrm.ehour.user.domain.User;

/**
 * TODO 
 **/

public class ReportCriteriaServiceImpl implements ReportCriteriaService
{
	private	UserDAO					userDAO;
	private	UserDepartmentDAO		userDepartmentDAO;
	private	CustomerDAO				customerDAO;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private	ReportAggregatedDAO				reportAggregatedDAO;
	private	Logger					logger = Logger.getLogger(this.getClass());
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportCriteriaService#syncUserReportCriteria(net.rrm.ehour.report.criteria.ReportCriteria, int)
	 */
	/**
	 * Update available report criteria 
	 */
	
	public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria, int updateType)
	{
		UserCriteria		userCriteria = reportCriteria.getUserCriteria();
		AvailableCriteria	availCriteria = reportCriteria.getAvailableCriteria();
		
		if (userCriteria.isSingleUser())
		{
			syncCriteriaForSingleUser(reportCriteria);
		}
		else
		{
			if (updateType == ReportCriteria.UPDATE_USERS ||
				updateType == ReportCriteria.UPDATE_ALL)
			{
				availCriteria.setUsers(getAvailableUsers(userCriteria));
			}

			if (updateType == ReportCriteria.UPDATE_CUSTOMERS ||
				updateType == ReportCriteria.UPDATE_ALL)
			{
				availCriteria.setCustomers(getAvailableCustomers(userCriteria));
			}

			if (updateType == ReportCriteria.UPDATE_PROJECTS ||
				updateType == ReportCriteria.UPDATE_ALL)
			{
				availCriteria.setProjects(getAvailableProjects(userCriteria));
			}
			
			if (updateType == ReportCriteria.UPDATE_ALL)
			{
				availCriteria.setUserDepartments(userDepartmentDAO.findAll());
				availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry());
			}
			
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
		List<User> 	users;

		if (userCriteria.getDepartmentIds() == null || 
			userCriteria.getDepartmentIds().length == 0)
		{
			if (userCriteria.isOnlyActiveUsers())
			{
				logger.debug("Finding all active users");
				users = userDAO.findAllActiveUsers();
			}
			else
			{
				
				logger.debug("Finding all users");
				users = userDAO.findAll();
			}
		}
		else
		{
			logger.debug("Finding users for departments");
			users = userDAO.findUsersForDepartments(userCriteria.getUserFilter()
														, userCriteria.getDepartmentIds()
														, userCriteria.isOnlyActiveUsers());
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
	private void syncCriteriaForSingleUser(ReportCriteria reportCriteria)
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
		
		availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry(userId));
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
	 *  
	 *
	 */
	public void setReportAggregatedDAO(ReportAggregatedDAO reportAggregatedDAO)
	{
		this.reportAggregatedDAO = reportAggregatedDAO;
	}
	
}
