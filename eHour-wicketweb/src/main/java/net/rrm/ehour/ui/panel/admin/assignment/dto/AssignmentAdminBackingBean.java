/**
 * Created on Aug 23, 2007
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

package net.rrm.ehour.ui.panel.admin.assignment.dto;

import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.ui.model.AdminBackingBean;

/**
 * Backing bean for project assignments
 **/

public class AssignmentAdminBackingBean extends ProjectAssignment implements AdminBackingBean
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
