/**
 * Created on 24-jan-2007
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

package net.rrm.ehour.report.criteria;

import net.rrm.ehour.data.DateRange;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO 
 **/

public class UserCriteria
{
	public final static int	USER_ACTIVE = 1;
	public final static int	USER_ALL = 2;
	public final static int	USER_SINGLE = 3;
	
	private DateRange	reportRange;
	private	boolean		onlyActiveProjects = true;
	private	boolean		onlyActiveCustomers = true;
	private	int			userActivityFilter;
	private	String		userFilter;
	private	Integer[]	userIds;
	private	Integer[]	projectIds;
	private	Integer[]	customerIds;
	private	Integer[]	departmentIds;
	private	boolean		singleUser;

	/**
	 * 
	 *
	 */
	public UserCriteria()
	{
		onlyActiveProjects = true;
		onlyActiveCustomers = true;
		userActivityFilter = USER_ACTIVE;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
			.append("reportRange", reportRange)
			.append("userIds", userIds)
			.append("projectIds", projectIds)
			.append("customerIds", customerIds)			
			.toString();
	}
	
	/**
	 * No customers selected ?
	 * @return
	 */
	public boolean isEmptyCustomers()
	{
		return customerIds == null || customerIds.length == 0;
	}
	
	/**
	 * No projects selected ?
	 * @return
	 */

	public boolean isEmptyProjects()
	{
		return projectIds == null || projectIds.length == 0;
	}

	/**
	 * No departments selected ?
	 * @return
	 */

	public boolean isEmptyDepartments()
	{
		return departmentIds == null || departmentIds.length == 0;
	}	
	
	/**
	 * No users selected ?
	 * @return
	 */
	
	public boolean isEmptyUsers()
	{
		return userIds == null || userIds.length == 0;
	}
	
	/**
	 * Get customers as string (needed for JSTL, should not be here..)
	 * @return
	 */
	public String getCustomersAsString()
	{
		return getIntegerArrayAsString(customerIds);
	}
	
	/**
	 * Get projects as string (needed for JSTL)
	 * @return
	 */
	public String getProjectsAsString()
	{
		return getIntegerArrayAsString(projectIds);
	}
	
	/**
	 * Get departments as string (JSTL)
	 * @return
	 */
	public String getDepartmentsAsString()
	{
		return getIntegerArrayAsString(departmentIds);
	}

	/**
	 * Get users as string (JSTL)
	 * @return
	 */
	public String getUsersAsString()
	{
		return getIntegerArrayAsString(userIds);
	}
	
	/**
	 * Get Integer array as string
	 * @param ints
	 * @return
	 */
	private String getIntegerArrayAsString(Integer[] ints)
	{
		StringBuffer	ids = new StringBuffer();
		int				i;
		
		if (ints != null)
		{
			for (i = 0;
				 i < ints.length;
				 i++)
			{
				ids.append("-" + ints[i].toString() + "-");
			}
		}
		
		return ids.toString();		
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
	 * @return the projectIds
	 */
	public Integer[] getProjectIds()
	{
		return projectIds;
	}
	/**
	 * @param projectIds the projectIds to set
	 */
	public void setProjectIds(Integer[] projectIds)
	{
		this.projectIds = projectIds;
	}
	/**
	 * @return the userIds
	 */
	public Integer[] getUserIds()
	{
		return userIds;
	}
	/**
	 * @param userIds the userIds to set
	 */
	public void setUserIds(Integer[] userIds)
	{
		this.userIds = userIds;
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
	 * @return the customerIds
	 */
	public Integer[] getCustomerIds()
	{
		return customerIds;
	}

	/**
	 * @param customerIds the customerIds to set
	 */
	public void setCustomerIds(Integer[] customerIds)
	{
		this.customerIds = customerIds;
	}

	/**
	 * @return the departmentIds
	 */
	public Integer[] getDepartmentIds()
	{
		return departmentIds;
	}

	/**
	 * @param departmentIds the departmentIds to set
	 */
	public void setDepartmentIds(Integer[] departmentIds)
	{
		this.departmentIds = departmentIds;
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
	
	public boolean isOnlyActiveUsers()
	{
		return userActivityFilter == UserCriteria.USER_ACTIVE;
	}
	
	public boolean isEmptyUserFilter()
	{
		return userFilter == null || userFilter.trim().length() == 0;
	}
}
