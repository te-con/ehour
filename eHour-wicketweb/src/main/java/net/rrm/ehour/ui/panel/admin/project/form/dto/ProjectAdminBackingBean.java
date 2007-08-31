/**
 * Created on Aug 20, 2007
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

package net.rrm.ehour.ui.panel.admin.project.form.dto;

import java.io.Serializable;

import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.ui.model.AdminBackingBean;

/**
 * Project admin backing bean
 **/

public class ProjectAdminBackingBean implements AdminBackingBean, Serializable
{
	private static final long serialVersionUID = 5862844398838328155L;
	
	private String serverMessage;
	private	Project	project;
	

	public ProjectAdminBackingBean(Project project)
	{
		this.project = project;
	}
	
	/**
	 * 
	 */
	public String getServerMessage()
	{
		return serverMessage;
	}

	/**
	 * 
	 */
	public void setServerMessage(String serverMessage)
	{
		this.serverMessage = serverMessage;
	}

	/**
	 * @return the project
	 */
	public Project getProject()
	{
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project)
	{
		this.project = project;
	}
}
