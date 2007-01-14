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
import java.util.List;

import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.dao.ReportDAO;
import net.rrm.ehour.report.dto.AvailReportCriteria;
import net.rrm.ehour.report.dto.ProjectReport;
import net.rrm.ehour.report.dto.ReportCriteria;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;

/**
 * Provides reporting services on timesheets.
 * 
 * @author Thies
 *
 */

public class ReportServiceImpl implements ReportService
{
	private	ReportDAO			reportDAO;
	private	UserDAO				userDAO;
	private	UserDepartmentDAO	userDepartmentDAO;
	private	CustomerDAO			customerDAO;
	private	ProjectDAO			projectDAO;
	
	/**
	 * Get the booked hours per project assignment for a month
	 * @param userId
	 * @param calendar
	 * @return SortedMap with ProjectAssignment on key and a Float representation of the booked hours as value
	 */	
	
	public List<ProjectReport> getHoursPerAssignmentInMonth(Integer userId, Calendar requestedDate)
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
	public List<ProjectReport> getHoursPerAssignmentInRange(Integer userId, DateRange dateRange)
	{
		List<ProjectReport>	projectReports;

		projectReports = reportDAO.getCumulatedHoursPerAssignmentForUser(userId, dateRange);

		return projectReports;
	}	
	

	/**
	 * Get available report criteria
	 */
	
	public AvailReportCriteria getAvailableReportCriteria(ReportCriteria reportCriteria)
	{
		AvailReportCriteria availCriteria = new AvailReportCriteria();
		List<User>			users;
		List<Customer>		customers;
		List<Project>		projects;
		DateRange			reportRange;
		
		// determine users
		if (reportCriteria.isOnlyActiveUsers())
		{
			users = userDAO.findAllActiveUsers();
		}
		else
		{
			users = userDAO.findAll();
		}
		
		availCriteria.setUsers(users);
		
		// user departments
		availCriteria.setUserDepartments(userDepartmentDAO.findAll());
		
		// customers
		if (reportCriteria.isOnlyActiveCustomers())
		{
			customers = customerDAO.findAll(true);
		}
		else
		{
			customers = customerDAO.findAll();
		}
		
		availCriteria.setCustomers(customers);
		
		// projects
		if (reportCriteria.isOnlyActiveProjects())
		{
			projects = projectDAO.findAllActive();
		}
		else
		{
			projects = projectDAO.findAll();
		}
		
		availCriteria.setProjects(projects);
		
//		availCriteria.s
		
		return availCriteria;
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


}
