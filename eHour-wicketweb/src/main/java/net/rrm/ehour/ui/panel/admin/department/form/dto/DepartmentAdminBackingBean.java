/**
 * Created on Aug 20, 2007
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

package net.rrm.ehour.ui.panel.admin.department.form.dto;

import java.io.Serializable;

import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.user.domain.UserDepartment;

/**
 * Department backing bean 
 **/

public class DepartmentAdminBackingBean implements AdminBackingBean, Serializable
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
