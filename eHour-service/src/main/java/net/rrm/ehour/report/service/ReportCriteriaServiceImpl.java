/**
 * Created on 21-feb-2007
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.audit.NonAuditable;
import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdate;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;

import org.apache.log4j.Logger;

/**
 * Report Criteria services
 **/
@NonAuditable
public class ReportCriteriaServiceImpl implements ReportCriteriaService
{
	private	UserDAO					userDAO;
	private	UserDepartmentDAO		userDepartmentDAO;
	private	CustomerDAO				customerDAO;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private	ReportAggregatedDAO		reportAggregatedDAO;
	private	Logger					logger = Logger.getLogger(this.getClass());
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportCriteriaService#syncUserReportCriteria(net.rrm.ehour.report.criteria.ReportCriteria)
	 */
	public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria)
	{
		return syncUserReportCriteria(reportCriteria, null);
	}
	
	/**
	 * Update available report criteria 
	 */
	
	public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria, ReportCriteriaUpdate updateType)
	{
		UserCriteria		userCriteria = reportCriteria.getUserCriteria();
		AvailableCriteria	availCriteria = reportCriteria.getAvailableCriteria();
		
		if (userCriteria.isSingleUser())
		{
			syncCriteriaForSingleUser(reportCriteria);
		}
		else
		{
			if (updateType == ReportCriteriaUpdate.UPDATE_CUSTOMERS ||
				updateType == ReportCriteriaUpdate.UPDATE_ALL)
			{
				availCriteria.setCustomers(getAvailableCustomers(userCriteria));
			}

			if (updateType == ReportCriteriaUpdate.UPDATE_PROJECTS ||
				updateType == ReportCriteriaUpdate.UPDATE_ALL)
			{
				availCriteria.setProjects(getAvailableProjects(userCriteria));
			}
			
			if (updateType == ReportCriteriaUpdate.UPDATE_ALL)
			{
				availCriteria.setUserDepartments(userDepartmentDAO.findAll());
				
				availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry());
			}

			if (updateType == ReportCriteriaUpdate.UPDATE_USERS ||
					updateType == ReportCriteriaUpdate.UPDATE_ALL)
			{
				availCriteria.setUsers(getAvailableUsers(userCriteria));
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

		if (userCriteria.isEmptyDepartments()) 
		{
			users = userDAO.findUsersByNameMatch(userCriteria.getUserFilter(), userCriteria.isOnlyActiveUsers());
		}
		else
		{
			logger.debug("Finding users for departments with filter '" + userCriteria.getUserFilter() + "'");
			users = userDAO.findUsersForDepartments(userCriteria.getUserFilter()
														, userCriteria.getDepartments()
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
		
		if (userCriteria.isEmptyCustomers()) 
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
			
			projects = projectDAO.findProjectForCustomers(userCriteria.getCustomers(), 
															userCriteria.isOnlyActiveProjects());
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
		User					user;
		
		user = reportCriteria.getUserCriteria().getUsers().get(0);
		
		assignments = projectAssignmentDAO.findProjectAssignmentsForUser(user);
		
		for (ProjectAssignment assignment : assignments)
		{
			customers.add(assignment.getProject().getCustomer());
			projects.add(assignment.getProject());
		}
		
		availCriteria.setCustomers(new ArrayList<Customer>(customers));
		availCriteria.setProjects(new ArrayList<Project>(projects));
		
		availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry(user));
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
