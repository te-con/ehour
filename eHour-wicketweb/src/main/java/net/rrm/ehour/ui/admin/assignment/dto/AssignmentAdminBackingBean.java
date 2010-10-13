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
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.common.model.AdminBackingBean;

/**
 * Assignment admin backing bean
 **/

public interface AssignmentAdminBackingBean extends AdminBackingBean
{

	/**
	 * Show allotted hours?
	 * @return
	 */
	public abstract boolean isShowAllottedHours();

	/**
	 * Can notify pm be enabled? is a pm assigned?
	 * @return
	 */
	public abstract boolean isNotifyPmEnabled();

	/**
	 * Show overrun hours?
	 * @return
	 */
	public abstract boolean isShowOverrunHours();

	/**
	 * @return the projectAssignment
	 */
	public abstract ProjectAssignment getProjectAssignment();

	/**
	 * Get project assignment for saving
	 * @return
	 */
	public abstract ProjectAssignment getProjectAssignmentForSave();

	/**
	 * @param projectAssignment the projectAssignment to set
	 */
	public abstract void setProjectAssignment(ProjectAssignment projectAssignment);

	/**
	 * @return the projects
	 */
	public abstract List<Project> getProjects();

	/**
	 * @param projects the projects to set
	 */
	public abstract void setProjects(List<Project> projects);

	/**
	 * @return the customer
	 */
	public abstract Customer getCustomer();

	/**
	 * @param customer the customer to set
	 */
	public abstract void setCustomer(Customer customer);

	/**
	 * @return the infiniteStartDate
	 */
	public abstract boolean isInfiniteStartDate();

	/**
	 * @param infiniteStartDate the infiniteStartDate to set
	 */
	public abstract void setInfiniteStartDate(boolean infiniteStartDate);

	/**
	 * @return the infiniteEndDate
	 */
	public abstract boolean isInfiniteEndDate();

	/**
	 * @param infiniteEndDate the infiniteEndDate to set
	 */
	public abstract void setInfiniteEndDate(boolean infiniteEndDate);

}