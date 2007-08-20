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

package net.rrm.ehour.ui.panel.admin.department.form.dto;

import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.user.domain.UserDepartment;

/**
 * Department backing bean 
 **/

public class DepartmentAdminBackingBean extends UserDepartment implements AdminBackingBean
{
	private static final long serialVersionUID = -4095608816724112187L;
	private String	serverMessage;
	private UserDepartment	department;
	
	public DepartmentAdminBackingBean(UserDepartment department)
	{
		this.department = department;
	}
	
	public String getServerMessage()
	{
		return serverMessage;
	}

	public void setServerMessage(String serverMessage)
	{
		this.serverMessage = serverMessage;
	}

	/**
	 * @return the department
	 */
	public UserDepartment getDepartment()
	{
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(UserDepartment department)
	{
		this.department = department;
	}
}
