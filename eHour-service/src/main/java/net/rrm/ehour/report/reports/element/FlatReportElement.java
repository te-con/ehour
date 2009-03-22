/**
 * Created on Feb 4, 2007
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

package net.rrm.ehour.report.reports.element;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Report element for trend reports (more data so each element is flattened)
 **/

public class FlatReportElement implements ReportElement
{
	private static final long serialVersionUID = -2146747873763924275L;
	private	Integer	customerId;
	private	String	customerName;
	private	String	customerCode;
	private	Number	totalHours;
	private	Number	totalTurnOver;
	private	String	entryDate;
	private	Integer	userId;
	private	String	userLastName;
	private	String	userFirstName;
	private	Integer	projectId;
	private	String	projectName;
	private	Integer	assignmentId;
	private	String	assignmentDesc;
	private Date	dayDate;
	private String	comment;
	private Integer	displayOrder;
	private Number 	hours;
	
	/**
	 * @return the hours
	 */
	public Number getHours()
	{
		return hours;
	}




	/**
	 * @param hours the hours to set
	 */
	public void setHours(Number hours)
	{
		this.hours = hours;
	}




	/**
	 * Minimal constructor
	 *
	 */
	public FlatReportElement()
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
	public Number getTotalHours()
	{
		return totalHours;
	}
	/**
	 * @param totalHours the totalHours to set
	 */
	public void setTotalHours(Number totalHours)
	{
		this.totalHours = totalHours;
	}
	/**
	 * @return the totalTurnOver
	 */
	public Number getTotalTurnOver()
	{
		return totalTurnOver;
	}
	/**
	 * @param totalTurnOver the totalTurnOver to set
	 */
	public void setTotalTurnOver(Number totalTurnOver)
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

	/**
	 * @return the dayDate
	 */
	public Date getDayDate()
	{
		return dayDate;
	}

	/**
	 * @param dayDate the dayDate to set
	 */
	public void setDayDate(Date dayDate)
	{
		this.dayDate = dayDate;
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof FlatReportElement))
		{
			return false;
		}
		
		FlatReportElement rhs = (FlatReportElement) object;
		return new EqualsBuilder()
			.append(this.assignmentId, rhs.getAssignmentId())
			.append(this.dayDate, rhs.getDayDate())
			.append(this.entryDate, rhs.getEntryDate())
			.append(this.totalHours, rhs.getTotalHours())
			.append(this.totalTurnOver, rhs.getTotalTurnOver())
			.append(this.displayOrder, rhs.getDisplayOrder())
			.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(this.assignmentId)
				.append(this.dayDate)
				.append(this.entryDate)
				.append(this.totalHours)
				.append(this.totalTurnOver)
				.append(this.displayOrder)
				.toHashCode();
	}

	/**
	 * @return the comment
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder()
	{
		return displayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder)
	{
		this.displayOrder = displayOrder;
	}
}
