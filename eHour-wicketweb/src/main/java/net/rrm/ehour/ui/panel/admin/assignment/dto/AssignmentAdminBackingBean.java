/**
 * Created on Aug 23, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.admin.assignment.dto;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.ui.model.AdminBackingBean;

/**
 * Backing bean for project assignments
 **/

public class AssignmentAdminBackingBean implements AdminBackingBean, Serializable
{
	private static final long serialVersionUID = 487430742116953930L;
	private	String				serverMessage;
	private	ProjectAssignment	projectAssignment;
	private	List<Project>		projects;
	private	Customer			customer;
	private	boolean				infiniteStartDate;
	private	boolean				infiniteEndDate;
	
	/**
	 * 
	 * @param assignment
	 */
	public AssignmentAdminBackingBean(ProjectAssignment assignment)
	{
		projectAssignment = assignment;
		this.customer = (assignment.getProject() != null) ? assignment.getProject().getCustomer() : null;
		
		infiniteStartDate = assignment.getDateStart() == null;
		infiniteEndDate = assignment.getDateEnd() == null;
	}
	
	/**
	 * Show allotted hours?
	 * @return
	 */
	public boolean isShowAllottedHours()
	{
		return (projectAssignment.getAssignmentType() != null)
					? projectAssignment.getAssignmentType().isAllottedType()
					: false;
	}
	
	/**
	 * Can notify pm be enabled? is a pm assigned?
	 * @return
	 */
	public boolean isNotifyPmEnabled()
	{
		return (projectAssignment.getProject() != null)
				? projectAssignment.getProject().getProjectManager() != null
				: false;
	}
	
	/**
	 * Show overrun hours?
	 * @return
	 */
	public boolean isShowOverrunHours()
	{
		return (projectAssignment.getAssignmentType() != null)
					? projectAssignment.getAssignmentType().isFlexAllottedType()
					: false;
	}	
	
	/**
	 * @return the serverMessage
	 */
	public String getServerMessage()
	{
		return serverMessage;
	}
	/**
	 * @param serverMessage the serverMessage to set
	 */
	public void setServerMessage(String serverMessage)
	{
		this.serverMessage = serverMessage;
	}
	/**
	 * @return the projectAssignment
	 */
	public ProjectAssignment getProjectAssignment()
	{
		return projectAssignment;
	}
	/**
	 * @param projectAssignment the projectAssignment to set
	 */
	public void setProjectAssignment(ProjectAssignment projectAssignment)
	{
		this.projectAssignment = projectAssignment;
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
	 * @return the customer
	 */
	public Customer getCustomer()
	{
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer)
	{
		this.customer = customer;
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
