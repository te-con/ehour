/**
 * Created on Feb 24, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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
import net.rrm.ehour.ui.model.AdminBackingBean;

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