/**
 * Created on 26-jan-2007
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

package net.rrm.ehour.web.report.form;

import javax.servlet.http.HttpServletRequest;

import net.rrm.ehour.web.form.UserIdForm;

import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class ReportCriteriaForm extends UserIdForm
{
	private static final long serialVersionUID = -208431579864236722L;
	private	boolean	fromForm;

	private	Integer	projectId[];
	private	Integer	customerId[];
	private	Integer	departmentId[];
	private	Integer	userIds[];
	private	String	dateStart;
	private	String	dateEnd;
	private	boolean	onlyActiveCustomers;
	private	boolean	onlyActiveProjects;
	private	boolean	onlyActiveUsers;
	private	String	userFilter;
	
	private	int		updateType;
	
	/**
	 * 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		fromForm = false;
		onlyActiveCustomers= false;
		onlyActiveProjects = false;
		
		//updateType = ReportCriteria.UPDATE_ALL;
	}

	/**
	 * @return the fromForm
	 */
	public boolean isFromForm()
	{
		return fromForm;
	}

	/**
	 * @param fromForm the fromForm to set
	 */
	public void setFromForm(boolean fromForm)
	{
		this.fromForm = fromForm;
	}

	/**
	 * @return the projectId
	 */
	public Integer[] getProjectId()
	{
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Integer[] projectId)
	{
		this.projectId = projectId;
	}

	/**
	 * @return the dateEnd
	 */
	public String getDateEnd()
	{
		return dateEnd;
	}

	/**
	 * @param dateEnd the dateEnd to set
	 */
	public void setDateEnd(String dateEnd)
	{
		this.dateEnd = dateEnd;
	}

	/**
	 * @return the dateStart
	 */
	public String getDateStart()
	{
		return dateStart;
	}

	/**
	 * @param dateStart the dateStart to set
	 */
	public void setDateStart(String dateStart)
	{
		this.dateStart = dateStart;
	}

	/**
	 * @return the customerId
	 */
	public Integer[] getCustomerId()
	{
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer[] customerId)
	{
		this.customerId = customerId;
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
	 * @return the departmentId
	 */
	public Integer[] getDepartmentId()
	{
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Integer[] departmentId)
	{
		this.departmentId = departmentId;
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
	 * @return the userFilter
	 */
	public String getUserFilter()
	{
		return userFilter;
	}

	/**
	 * @param userFilter the userFilter to set
	 */
	public void setUserFilter(String filterUser)
	{
		this.userFilter = filterUser;
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
	 * @return the updateType
	 */
	public int getUpdateType()
	{
		return updateType;
	}

	/**
	 * @param updateType the updateType to set
	 */
	public void setUpdateType(int event)
	{
		this.updateType = event;
	}
}
