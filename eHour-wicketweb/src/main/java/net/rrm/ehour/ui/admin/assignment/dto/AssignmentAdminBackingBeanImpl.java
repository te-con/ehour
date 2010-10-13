/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.admin.assignment.dto;

import java.util.List;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;

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
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#isShowAllottedHours()
	 */
	public boolean isShowAllottedHours()
	{
		return (projectAssignment.getAssignmentType() != null)
					? projectAssignment.getAssignmentType().isAllottedType()
					: false;
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#isNotifyPmEnabled()
	 */
	public boolean isNotifyPmEnabled()
	{
		return (projectAssignment.getProject() != null)
				? projectAssignment.getProject().getProjectManager() != null
				: false;
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#isShowOverrunHours()
	 */
	public boolean isShowOverrunHours()
	{
		return (projectAssignment.getAssignmentType() != null)
					? projectAssignment.getAssignmentType().isFlexAllottedType()
					: false;
	}	
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#getProjectAssignment()
	 */
	public ProjectAssignment getProjectAssignment()
	{
		return projectAssignment;
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#getProjectAssignmentForSave()
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
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#setProjectAssignment(net.rrm.ehour.domain.ProjectAssignment)
	 */
	public void setProjectAssignment(ProjectAssignment projectAssignment)
	{
		this.projectAssignment = projectAssignment;
	}
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#getProjects()
	 */
	public List<Project> getProjects()
	{
		return projects;
	}
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#setProjects(java.util.List)
	 */
	public void setProjects(List<Project> projects)
	{
		this.projects = projects;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#getCustomer()
	 */
	public Customer getCustomer()
	{
		return customer;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#setCustomer(net.rrm.ehour.domain.Customer)
	 */
	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#isInfiniteStartDate()
	 */
	public boolean isInfiniteStartDate()
	{
		return infiniteStartDate;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#setInfiniteStartDate(boolean)
	 */
	public void setInfiniteStartDate(boolean infiniteStartDate)
	{
		this.infiniteStartDate = infiniteStartDate;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#isInfiniteEndDate()
	 */
	public boolean isInfiniteEndDate()
	{
		return infiniteEndDate;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.assignment.panel.dto.AssignmentAdminBackingBean#setInfiniteEndDate(boolean)
	 */
	public void setInfiniteEndDate(boolean infiniteEndDate)
	{
		this.infiniteEndDate = infiniteEndDate;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.model.AdminBackingBean#getDomainObject()
	 */
	public DomainObject<?, ?> getDomainObject()
	{
		return getProjectAssignment();
	}
}
