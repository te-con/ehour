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
	private	boolean		onlyActiveProjects;
	private	boolean		onlyActiveCustomers;
	private	int			userFilter;
	private	Integer[]	userIds;
	private	Integer[]	projectIds;
	private	boolean		singleUser;
	
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
			.toString();
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
	 * @return the userFilter
	 */
	public int getUserFilter()
	{
		return userFilter;
	}
	/**
	 * @param userFilter the userFilter to set
	 */
	public void setUserFilter(int userFilter)
	{
		this.userFilter = userFilter;
	}
}
