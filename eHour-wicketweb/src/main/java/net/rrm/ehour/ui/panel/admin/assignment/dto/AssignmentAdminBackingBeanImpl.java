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

import java.util.List;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.model.AdminBackingBeanImpl;

/**
 * Backing bean for project assignments
 **/

public class AssignmentAdminBackingBeanImpl extends AdminBackingBeanImpl implements AssignmentAdminBackingBean
{
	private static final long serialVersionUID = 487430742116953930L;
	private	ProjectAssignment	projectAssignment;
	private	List<Project>		projects;
	private	Customer			customer;
	private	boolean				infiniteStartDate;
	private	boolean				infiniteEndDate;

	/**
	 * 
	 */
	public AssignmentAdminBackingBeanImpl()
	{
	}
	
	/**
	 * 
	 * @param assignment
	 */
	public AssignmentAdminBackingBeanImpl(ProjectAssignment assignment)
	{
		projectAssignment = assignment;
		this.customer = (assignment.getProject() != null) ? assignment.getProject().getCustomer() : null;
		
		infiniteStartDate = assignment.getDateStart() == null;
		infiniteEndDate = assignment.getDateEnd() == null;
	}
	
	/**
	 * Factory method
	 * @param user
	 * @return
	 */
	public static AssignmentAdminBackingBean createAssignmentAdminBackingBean(User user)
	{
		ProjectAssignment			projectAssignment;
		
		projectAssignment = new ProjectAssignment();
		projectAssignment.setUser(user);
		projectAssignment.setActive(true);
		
		return new AssignmentAdminBackingBeanImpl(projectAssignment);
		
	}	
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#isShowAllottedHours()
	 */
	public boolean isShowAllottedHours()
	{
		return (projectAssignment.getAssignmentType() != null)
					? projectAssignment.getAssignmentType().isAllottedType()
					: false;
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#isNotifyPmEnabled()
	 */
	public boolean isNotifyPmEnabled()
	{
		return (projectAssignment.getProject() != null)
				? projectAssignment.getProject().getProjectManager() != null
				: false;
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#isShowOverrunHours()
	 */
	public boolean isShowOverrunHours()
	{
		return (projectAssignment.getAssignmentType() != null)
					? projectAssignment.getAssignmentType().isFlexAllottedType()
					: false;
	}	
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#getProjectAssignment()
	 */
	public ProjectAssignment getProjectAssignment()
	{
		return projectAssignment;
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#getProjectAssignmentForSave()
	 */
	public ProjectAssignment getProjectAssignmentForSave()
	{
		if (isInfiniteStartDate())
		{
			projectAssignment.setDateStart(null);
		}
		

		if (isInfiniteEndDate())
		{
			projectAssignment.setDateEnd(null);
		}
		
		return projectAssignment;
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#setProjectAssignment(net.rrm.ehour.domain.ProjectAssignment)
	 */
	public void setProjectAssignment(ProjectAssignment projectAssignment)
	{
		this.projectAssignment = projectAssignment;
	}
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#getProjects()
	 */
	public List<Project> getProjects()
	{
		return projects;
	}
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#setProjects(java.util.List)
	 */
	public void setProjects(List<Project> projects)
	{
		this.projects = projects;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#getCustomer()
	 */
	public Customer getCustomer()
	{
		return customer;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#setCustomer(net.rrm.ehour.domain.Customer)
	 */
	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#isInfiniteStartDate()
	 */
	public boolean isInfiniteStartDate()
	{
		return infiniteStartDate;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#setInfiniteStartDate(boolean)
	 */
	public void setInfiniteStartDate(boolean infiniteStartDate)
	{
		this.infiniteStartDate = infiniteStartDate;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#isInfiniteEndDate()
	 */
	public boolean isInfiniteEndDate()
	{
		return infiniteEndDate;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean#setInfiniteEndDate(boolean)
	 */
	public void setInfiniteEndDate(boolean infiniteEndDate)
	{
		this.infiniteEndDate = infiniteEndDate;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.model.AdminBackingBean#getDomainObject()
	 */
	public ProjectAssignment getDomainObject()
	{
		return getProjectAssignment();
	}
}
