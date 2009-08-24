/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.report.service;

import java.util.ArrayList;
import java.util.Collections;
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
import net.rrm.ehour.project.util.ProjectUtil;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;

import org.apache.commons.collections.CollectionUtils;
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
	
	public ReportCriteria syncUserReportCriteria(ReportCriteria reportCriteria, ReportCriteriaUpdateType updateType)
	{
		UserCriteria		userCriteria = reportCriteria.getUserCriteria();
		AvailableCriteria	availCriteria = reportCriteria.getAvailableCriteria();
		
		if (userCriteria.isSingleUser())
		{
			syncCriteriaForSingleUser(reportCriteria);
		}
		else
		{
			if (updateType == ReportCriteriaUpdateType.UPDATE_CUSTOMERS ||
				updateType == ReportCriteriaUpdateType.UPDATE_ALL)
			{
				availCriteria.setCustomers(getAvailableCustomers(userCriteria));
			}

			if (updateType == ReportCriteriaUpdateType.UPDATE_PROJECTS ||
				updateType == ReportCriteriaUpdateType.UPDATE_ALL)
			{
				availCriteria.setProjects(getAvailableProjects(userCriteria));
			}
			
			if (updateType == ReportCriteriaUpdateType.UPDATE_ALL)
			{
				availCriteria.setUserDepartments(userDepartmentDAO.findAll());
				
				availCriteria.setReportRange(reportAggregatedDAO.getMinMaxDateTimesheetEntry());
			}

			if (updateType == ReportCriteriaUpdateType.UPDATE_USERS ||
					updateType == ReportCriteriaUpdateType.UPDATE_ALL)
			{
				availCriteria.setUsers(getAvailableUsers(userCriteria));
			}
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
		
		customers = fetchCustomers(userCriteria);
		
		List<Customer> billableCustomers = checkForOnlyBillableCustomers(userCriteria, customers);
		
		Collections.sort(billableCustomers);
		
		return billableCustomers;
	}

	private List<Customer> checkForOnlyBillableCustomers(UserCriteria userCriteria, List<Customer> customers)
	{
		List<Customer> billableCustomers = new ArrayList<Customer>();
		
		if (userCriteria.isOnlyBillableProjects())
		{
			for (Customer customer : customers)
			{
				List<Project> billableProjects;
				
				if (userCriteria.isOnlyActiveProjects())
				{
					 billableProjects = ProjectUtil.getBillableProjects(customer.getActiveProjects());
				}
				else
				{
					 billableProjects = ProjectUtil.getBillableProjects(customer.getProjects());
				}
				
				if (!CollectionUtils.isEmpty(billableProjects))
				{
					billableCustomers.add(customer);
				}
			}
		}
		else
		{
			return customers;
		}
		
		return billableCustomers;
	}

	private List<Customer> fetchCustomers(UserCriteria userCriteria)
	{
		List<Customer> customers;
		if (userCriteria.isOnlyActiveCustomers())
		{
			customers = customerDAO.findAllActive();
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
			projects = fetchProjects(userCriteria);
		}
		else
		{
			projects = projectDAO.findProjectForCustomers(userCriteria.getCustomers(), 
															userCriteria.isOnlyActiveProjects());
		}
		
		projects = checkForOnlyBillableProjects(userCriteria, projects);
		
		return projects;
	}

	private List<Project> checkForOnlyBillableProjects(UserCriteria userCriteria, List<Project> projects)
	{
		if (userCriteria.isOnlyBillableProjects())
		{
			projects = ProjectUtil.getBillableProjects(projects);
		}
		return projects;
	}

	private List<Project> fetchProjects(UserCriteria userCriteria)
	{
		List<Project> projects;
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
		
		assignments = projectAssignmentDAO.findProjectAssignmentsForUser(user.getUserId(), reportCriteria.getUserCriteria().getReportRange());
		
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
