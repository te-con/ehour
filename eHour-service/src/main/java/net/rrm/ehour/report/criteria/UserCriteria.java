/**
 * Created on 24-jan-2007
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

package net.rrm.ehour.report.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * User selected criteria 
 **/

public class UserCriteria implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 375613059093184619L;
	private DateRange	reportRange;
	private	boolean		onlyActiveProjects = true;
	private	boolean		onlyActiveCustomers = true;
	private	boolean		onlyActiveUsers = true;
	private	int			userActivityFilter;
	private	String		userFilter;
	private	String		customerFilter;
	private	List<User>	users;
	private	List<Project>		projects;
	private	List<Customer>		customers;
	private	List<UserDepartment>	userDepartments;
	private boolean 	infiniteStartDate;
	private boolean		infiniteEndDate;
	private	boolean		singleUser;

	/**
	 * 
	 *
	 */
	public UserCriteria()
	{
		onlyActiveProjects = true;
		onlyActiveCustomers = true;
		onlyActiveUsers = true;
		
		infiniteStartDate = false;
		infiniteEndDate = false;
		
		reportRange = new DateRange(new Date(), new Date());
	}
	
	/**
	 * 
	 * @param user
	 */
	public void setUser(User user)
	{
		if (users == null)
		{
			users = new ArrayList<User>();
		}
		
		users.add(user);
	}

	
	/**
	 * 
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
			.append("reportRange", reportRange)
			.append("users", users)
			.append("projects", projects)
			.append("customers", customers)			
			.append("departments", userDepartments)
			.toString();
	}
	
	/**
	 * No customers selected ?
	 * @return
	 */
	public boolean isEmptyCustomers()
	{
		return customers == null || customers.size() == 0;
	}
	
	/**
	 * No projects selected ?
	 * @return
	 */

	public boolean isEmptyProjects()
	{
		return projects == null || projects.size() == 0;
	}

	/**
	 * No departments selected ?
	 * @return
	 */

	public boolean isEmptyDepartments()
	{
		return userDepartments == null || userDepartments.size() == 0;
	}	
	
	/**
	 * No users selected ?
	 * @return
	 */
	
	public boolean isEmptyUsers()
	{
		return users == null || users.size() == 0;
	}
	
	/**
	 * @return the onlyActiveCustomers
	 */
	public boolean isOnlyActiveCustomers()
	{
		return onlyActiveCustomers;
	}
	/**
	 * @param onlyActiveCustomers the onlyActiveCustomers to set
	 */
	public void setOnlyActiveCustomers(boolean onlyActiveCustomers)
	{
		this.onlyActiveCustomers = onlyActiveCustomers;
	}
	/**
	 * @return the onlyActiveProjects
	 */
	public boolean isOnlyActiveProjects()
	{
		return onlyActiveProjects;
	}
	/**
	 * @param onlyActiveProjects the onlyActiveProjects to set
	 */
	public void setOnlyActiveProjects(boolean onlyActiveProjects)
	{
		this.onlyActiveProjects = onlyActiveProjects;
	}

	/**
	 * @return the reportRange
	 */
	public DateRange getReportRange()
	{
		return reportRange;
	}
	/**
	 * @param reportRange the reportRange to set
	 */
	public void setReportRange(DateRange reportRange)
	{
		this.reportRange = reportRange;
	}

	/**
	 * @return the singleUser
	 */
	public boolean isSingleUser()
	{
		return singleUser;
	}
	/**
	 * @param singleUser the singleUser to set
	 */
	public void setSingleUser(boolean singleUser)
	{
		this.singleUser = singleUser;
	}
	/**
	 * @return the userActivityFilter
	 */
	public int getUserActivityFilter()
	{
		return userActivityFilter;
	}
	/**
	 * @param userActivityFilter the userActivityFilter to set
	 */
	public void setUserActivityFilter(int userFilter)
	{
		this.userActivityFilter = userFilter;
	}


	/**
	 * @return the userFilter
	 */
	public String getUserFilter()
	{
		return userFilter;
	}

	/**
	 * @param userFilter the userFilter to set
	 */
	public void setUserFilter(String userFilter)
	{
		this.userFilter = userFilter;
	}

	
	public boolean isEmptyUserFilter()
	{
		return userFilter == null || userFilter.trim().length() == 0;
	}

	/**
	 * @return the onlyActiveUsers
	 */
	public boolean isOnlyActiveUsers()
	{
		return onlyActiveUsers;
	}

	/**
	 * @param onlyActiveUsers the onlyActiveUsers to set
	 */
	public void setOnlyActiveUsers(boolean onlyActiveUsers)
	{
		this.onlyActiveUsers = onlyActiveUsers;
	}

	/**
	 * @return the userIds
	 */
	public List<User> getUsers()
	{
		return users;
	}

	/**
	 * @param userIds the userIds to set
	 */
	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	/**
	 * @return the customerIds
	 */
	public List<Customer> getCustomers()
	{
		return customers;
	}

	/**
	 * @param customerIds the customerIds to set
	 */
	public void setCustomers(List<Customer> customers)
	{
		this.customers = customers;
	}

	/**
	 * @return the departmentIds
	 */
	public List<UserDepartment> getDepartments()
	{
		return userDepartments;
	}

	/**
	 * @param departmentIds the departmentIds to set
	 */
	public void setDepartments(List<UserDepartment> departments)
	{
		this.userDepartments = departments;
	}

	/**
	 * @return the infiniteStartDate
	 */
	public boolean isInfiniteStartDate()
	{
		return infiniteStartDate;
	}

	/**
	 * @param infiniteStartDate the infiniteStartDate to set
	 */
	public void setInfiniteStartDate(boolean infiniteStartDate)
	{
		this.infiniteStartDate = infiniteStartDate;
	}

	/**
	 * @return the infiniteEndDate
	 */
	public boolean isInfiniteEndDate()
	{
		return infiniteEndDate;
	}

	/**
	 * @param infiniteEndDate the infiniteEndDate to set
	 */
	public void setInfiniteEndDate(boolean infiniteEndDate)
	{
		this.infiniteEndDate = infiniteEndDate;
	}

	/**
	 * @return the projects
	 */
	public List<Project> getProjects()
	{
		return projects;
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<Project> projects)
	{
		this.projects = projects;
	}

	/**
	 * @return the customerFilter
	 */
	public String getCustomerFilter()
	{
		return customerFilter;
	}

	/**
	 * @param customerFilter the customerFilter to set
	 */
	public void setCustomerFilter(String customerFilter)
	{
		this.customerFilter = customerFilter;
	}
}
