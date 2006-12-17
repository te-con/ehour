/**
 * Created on Dec 5, 2006
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

package net.rrm.ehour.web.admin.project.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class ProjectForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7445831308497127835L;
	private	Integer	projectId;
	private	Integer	customerId;
	private	String	name;
	private	String	description;
	private	String	contact;
	private	String	projectCode;
	private	boolean	defaultProject;
	private	boolean	active;
	private	boolean	fromForm;
	private	boolean	hideInactive;
	
	/**
	 * 
	 *
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		defaultProject = false;
		fromForm = false;
		active = false;
		hideInactive = true;
	}

	/**
	 * @return the active
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active)
	{
		this.active = active;
	}

	/**
	 * @return the contact
	 */
	public String getContact()
	{
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact)
	{
		this.contact = contact;
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
	 * @return the defaultProject
	 */
	public boolean isDefaultProject()
	{
		return defaultProject;
	}

	/**
	 * @param defaultProject the defaultProject to set
	 */
	public void setDefaultProject(boolean defaultProject)
	{
		this.defaultProject = defaultProject;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the projectCode
	 */
	public String getProjectCode()
	{
		return projectCode;
	}

	/**
	 * @param projectCode the projectCode to set
	 */
	public void setProjectCode(String projectCode)
	{
		this.projectCode = projectCode;
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
	 * @return the hideInactive
	 */
	public boolean isHideInactive()
	{
		return hideInactive;
	}

	/**
	 * @param hideInactive the hideInactive to set
	 */
	public void setHideInactive(boolean hideInactive)
	{
		this.hideInactive = hideInactive;
	}	
}
