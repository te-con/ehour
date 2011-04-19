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

import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.project.util.ProjectUtil;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserCriteria;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Report Criteria services
 **/
@NonAuditable
@Service("reportCriteriaService")
public class ReportCriteriaServiceImpl implements ReportCriteriaService
{
	@Autowired
	private	UserDao					userDAO;
	@Autowired
	private	UserDepartmentDao		userDepartmentDAO;
	@Autowired
	private	CustomerDao				customerDAO;
	@Autowired
	private	ProjectDao				projectDAO;
	@Autowired
	private	ProjectAssignmentDao	projectAssignmentDAO;
	@Autowired
	private	ReportAggregatedDao		reportAggregatedDAO;

	private	static final Logger	LOGGER = Logger.getLogger(ReportCriteriaServiceImpl.class);
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.report.service.ReportCriteriaService#syncUserReportCriteria(net.rrm.ehour.persistence.persistence.report.criteria.ReportCriteria)
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
			LOGGER.debug("Finding users for departments with filter '" + userCriteria.getUserFilter() + "'");
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
		
		LOGGER.debug("Finding on billable only: " + userCriteria.isOnlyBillableProjects());
		
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
			LOGGER.debug("Fetching only active projects");

			projects = projectDAO.findAllActive();
		}
		else
		{
			LOGGER.debug("Fetching all projects");
			
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
		Set<Customer>			customers = new HashSet<Customer>();
		Set<Project>			projects = new HashSet<Project>();
		AvailableCriteria		availCriteria = reportCriteria.getAvailableCriteria();
		User					user;
		
		user = reportCriteria.getUserCriteria().getUsers().get(0);
		
		List<ProjectAssignment>	assignments = projectAssignmentDAO.findProjectAssignmentsForUser(user.getUserId(), reportCriteria.getUserCriteria().getReportRange());
		
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
	public void setUserDAO(UserDao userDAO)
	{
		this.userDAO = userDAO;
	}

	/**
	 * @param customerDAO the customerDAO to set
	 */
	public void setCustomerDAO(CustomerDao customerDAO)
	{
		this.customerDAO = customerDAO;
	}

	/**
	 * @param projectDAO the projectDAO to set
	 */
	public void setProjectDAO(ProjectDao projectDAO)
	{
		this.projectDAO = projectDAO;
	}

	/**
	 * @param userDepartmentDAO the userDepartmentDAO to set
	 */
	public void setUserDepartmentDAO(UserDepartmentDao userDepartmentDAO)
	{
		this.userDepartmentDAO = userDepartmentDAO;
	}

	/**
	 * @param projectAssignmentDAO the projectAssignmentDAO to set
	 */
	public void setProjectAssignmentDAO(ProjectAssignmentDao projectAssignmentDAO)
	{
		this.projectAssignmentDAO = projectAssignmentDAO;
	}	
	
	/**
	 *  
	 *
	 */
	public void setReportAggregatedDAO(ReportAggregatedDao reportAggregatedDAO)
	{
		this.reportAggregatedDAO = reportAggregatedDAO;
	}
}
