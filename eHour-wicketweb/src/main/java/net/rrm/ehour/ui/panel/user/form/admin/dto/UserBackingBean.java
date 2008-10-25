/**
 * Created on Aug 14, 2007
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

package net.rrm.ehour.ui.panel.user.form.admin.dto;

import java.util.ArrayList;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.model.AdminBackingBeanImpl;
import net.rrm.ehour.util.EhourConstants;

/**
 * Backing bean for users
 **/

public class UserBackingBean extends AdminBackingBeanImpl
{
	private static final long serialVersionUID = 2781902854421696575L;
	private User	user;
	private	String	confirmPassword;
	private	String	originalUsername;
	private	String	originalPassword;
	private	boolean	isPm;
	
	public UserBackingBean(User user)
	{
		this.user = user;
		
		if (user != null)
		{
			this.originalUsername = user.getUsername();
			this.originalPassword = user.getPassword();
			
			// barfff
			isPm = new ArrayList<UserRole>(user.getUserRoles()).contains(new UserRole(EhourConstants.ROLE_PROJECTMANAGER));
		}
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public String getConfirmPassword()
	{
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}

	public String getOriginalUsername()
	{
		return originalUsername;
	}

	public void setOriginalUsername(String originalUsername)
	{
		this.originalUsername = originalUsername;
	}

	public String getOriginalPassword()
	{
		return originalPassword;
	}

	public void setOriginalPassword(String originalPassword)
	{
		this.originalPassword = originalPassword;
	}

	public boolean isPm()
	{
		return isPm;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.model.AdminBackingBean#getDomainObject()
	 */
	
	public User getDomainObject()
	{
		return getUser();
	}
}
