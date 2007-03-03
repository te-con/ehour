/**
 * Created on Feb 4, 2007
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

package net.rrm.ehour.report.reports;

/**
 * TODO 
 **/

public class FlatProjectAssignmentAggregate
{
	private	Integer	customerId;
	private	String	customerName;
	private	String	customerCode;
	private	float	totalHours;
	private	float	totalTurnOver;
	private	String	entryDate;
	private	Integer	userId;
	private	String	userLastName;
	private	String	userFirstName;
	private	Integer	projectId;
	private	String	projectName;
	private	Integer	assignmentId;
	private	String	assignmentDesc;
	
	/**
	 * Minimal constructor
	 *
	 */
	public FlatProjectAssignmentAggregate()
	{
		
	}


	/**
	 * @return the customerName
	 */
	public String getCustomerName()
	{
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}
	/**
	 * @return the projectId
	 */
	public Integer getProjectId()
	{
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Integer projectId)
	{
		this.projectId = projectId;
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName()
	{
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}
	/**
	 * @return the totalHours
	 */
	public float getTotalHours()
	{
		return totalHours;
	}
	/**
	 * @param totalHours the totalHours to set
	 */
	public void setTotalHours(float totalHours)
	{
		this.totalHours = totalHours;
	}
	/**
	 * @return the totalTurnOver
	 */
	public float getTotalTurnOver()
	{
		return totalTurnOver;
	}
	/**
	 * @param totalTurnOver the totalTurnOver to set
	 */
	public void setTotalTurnOver(float totalTurnOver)
	{
		this.totalTurnOver = totalTurnOver;
	}
	/**
	 * @return the userFirstName
	 */
	public String getUserFirstName()
	{
		return userFirstName;
	}
	/**
	 * @param userFirstName the userFirstName to set
	 */
	public void setUserFirstName(String userFirstName)
	{
		this.userFirstName = userFirstName;
	}
	/**
	 * @return the userId
	 */
	public Integer getUserId()
	{
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}
	/**
	 * @return the userLastName
	 */
	public String getUserLastName()
	{
		return userLastName;
	}
	/**
	 * @param userLastName the userLastName to set
	 */
	public void setUserLastName(String userLastName)
	{
		this.userLastName = userLastName;
	}
	/**
	 * @return the entryDate
	 */
	public String getEntryDate()
	{
		return entryDate;
	}
	/**
	 * @param entryDate the entryDate to set
	 */
	public void setEntryDate(String weekYear)
	{
		this.entryDate = weekYear;
	}


	/**
	 * @return the customerId
	 */
	public Integer getCustomerId()
	{
		return customerId;
	}


	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId)
	{
		this.customerId = customerId;
	}


	/**
	 * @return the customerCode
	 */
	public String getCustomerCode()
	{
		return customerCode;
	}


	/**
	 * @param customerCode the customerCode to set
	 */
	public void setCustomerCode(String customerCode)
	{
		this.customerCode = customerCode;
	}


	/**
	 * @return the assignmentDesc
	 */
	public String getAssignmentDesc()
	{
		return assignmentDesc;
	}


	/**
	 * @param assignmentDesc the assignmentDesc to set
	 */
	public void setAssignmentDesc(String assignmentDesc)
	{
		this.assignmentDesc = assignmentDesc;
	}


	/**
	 * @return the assignmentId
	 */
	public Integer getAssignmentId()
	{
		return assignmentId;
	}


	/**
	 * @param assignmentId the assignmentId to set
	 */
	public void setAssignmentId(Integer assignmentId)
	{
		this.assignmentId = assignmentId;
	}
}
