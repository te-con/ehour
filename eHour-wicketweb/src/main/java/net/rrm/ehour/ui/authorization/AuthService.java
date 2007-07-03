/**
 * Created on May 29, 2007
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

package net.rrm.ehour.ui.authorization;

import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.service.UserService;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

/**
 * Provides userDetails 
 **/

public class AuthService implements UserDetailsService
{
	private	UserService	userService;
	private	Logger		logger = Logger.getLogger(AuthService.class);	

	/**
	 * Get user by username (acegi)
	 * @param username
	 */
	/**
	 * 
	 */
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException
	{
		User		user = null;
		AuthUser	authUser;
		
		user = userService.getUser(username);
		
		if (user == null || !user.isActive())
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Load user by username for " + username + " but user unknown or inactive");
			}
			
			throw new UsernameNotFoundException("User unknown");
		}
		else
		{
			authUser = new AuthUser(user);
		}
		
		return authUser;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}	
}
