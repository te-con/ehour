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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.util.ReportUtil;
import net.rrm.ehour.user.domain.User;

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
	private	List<Integer>		userIds;
	private	List<Integer>		projectIds;
	private	List<Integer>		customerIds;
	private	List<Integer>		departmentIds;
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
		if (userIds == null)
		{
			userIds = new ArrayList<Integer>();
		}
		
		userIds.add(user.getUserId());
	}
	
	/**
	 * Adds project to the list
	 * @param project
	 */
	public void setProject(Project project)
	{
		if (projectIds == null)
		{
			projectIds = new ArrayList<Integer>();
		}
		
		projectIds.add(project.getProjectId());
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
		return customerIds == null || customerIds.size() == 0;
	}
	
	/**
	 * No projects selected ?
	 * @return
	 */

	public boolean isEmptyProjects()
	{
		return projectIds == null || projectIds.size() == 0;
	}

	/**
	 * No departments selected ?
	 * @return
	 */

	public boolean isEmptyDepartments()
	{
		return departmentIds == null || departmentIds.size() == 0;
	}	
	
	/**
	 * No users selected ?
	 * @return
	 */
	
	public boolean isEmptyUsers()
	{
		return userIds == null || userIds.size() == 0;
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
	public List<Integer> getUserIds()
	{
		return userIds;
	}

	/**
	 * @param userIds the userIds to set
	 */
	public void setUserIds(List<Integer> userIds)
	{
		this.userIds = userIds;
	}

	/**
	 * @return the projectIds
	 */
	public List<Integer> getProjectIds()
	{
		return projectIds;
	}

	public void setProjects(List<Project> projects)
	{
		setProjectIds(ReportUtil.getPKsFromDomainObjects(projects));
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Project> getProjects()
	{
		List<Project>	projects = new ArrayList<Project>();
	
		if (projectIds != null)
		{
			for (Integer  id : projectIds)
			{
				projects.add(new Project(id));
			}
		}
		
		return projects;
	}
	
	/**
	 * @param projectIds the projectIds to set
	 */
	@SuppressWarnings("unchecked")
	public void setProjectIds(List projectIds)
	{
		this.projectIds = projectIds;
	}

	/**
	 * @return the customerIds
	 */
	public List<Integer> getCustomerIds()
	{
		return customerIds;
	}

	/**
	 * @param customerIds the customerIds to set
	 */
	public void setCustomerIds(List<Integer> customerIds)
	{
		this.customerIds = customerIds;
	}

	/**
	 * @return the departmentIds
	 */
	public List<Integer> getDepartmentIds()
	{
		return departmentIds;
	}

	/**
	 * @param departmentIds the departmentIds to set
	 */
	public void setDepartmentIds(List<Integer> departmentIds)
	{
		this.departmentIds = departmentIds;
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
}
