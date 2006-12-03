/**
 * Created on Nov 26, 2006
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

package net.rrm.ehour.web.admin.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class UserForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -726486176963963693L;
	private	boolean	fromForm = false;
	private	String	filterPattern;
	private	boolean	hideInactive;
	
	private	Integer	userId;
	private	String	username;
	private	String	password;
	private	String	confirmPassword;
	private	String	firstName;
	private	String	lastName;
	private	Integer	departmentId;
	private	boolean	active;
	private	String	email;
	private	String[]	roles;

	/**
	 * 
	 *
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		fromForm = false;
	}
	/**
	 * @return the filterPattern
	 */
	public String getFilterPattern()
	{
		return filterPattern;
	}

	/**
	 * @param filterPattern the filterPattern to set
	 */
	public void setFilterPattern(String filterPattern)
	{
		this.filterPattern = filterPattern;
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
	 * @return the confirmPassword
	 */
	public String getConfirmPassword()
	{
		return confirmPassword;
	}
	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}
	/**
	 * @return the departmentId
	 */
	public Integer getDepartmentId()
	{
		return departmentId;
	}
	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Integer departmentId)
	{
		this.departmentId = departmentId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	/**
	 * @return the userId
	 */
	public Integer getUserId()
	{
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}
	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
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
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	/**
	 * @return the roles
	 */
	public String[] getRoles()
	{
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(String[] roles)
	{
		System.out.println("xx: " + roles);
		this.roles = roles;
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
