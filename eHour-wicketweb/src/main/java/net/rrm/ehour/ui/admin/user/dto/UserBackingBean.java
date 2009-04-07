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

package net.rrm.ehour.ui.admin.user.dto;

import java.util.ArrayList;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;
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
	 * @see net.rrm.ehour.ui.common.model.AdminBackingBean#getDomainObject()
	 */
	
	public User getDomainObject()
	{
		return getUser();
	}
}
