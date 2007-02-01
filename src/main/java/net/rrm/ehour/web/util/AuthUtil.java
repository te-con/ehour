/**
 * Created on Nov 12, 2006
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

package net.rrm.ehour.web.util;

import javax.servlet.http.HttpServletRequest;

import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.dto.AuthUser;
import net.rrm.ehour.web.form.UserIdForm;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.log4j.Logger;

/**
 * TODO 
 **/

public class AuthUtil
{
	public static transient Logger logger = Logger.getLogger(AuthUtil.class);

	/**
	 * Check whether a role is in the array of roles 
	 * @param role
	 * @param roles
	 * @return
	 */
	public static boolean hasRole(String role, GrantedAuthority[] roles)
	{
		boolean	hasRole = false;
		int		i;
		
		for (i = 0;
			 i < roles.length;
			 i++)
		{
			if (roles[i].getAuthority().equals(role))
			{
				hasRole = true;
				break;
			}
		}
		
		return hasRole;
	}
	
	/**
	 * Get the user Id either from the supplied form if the authenticated user is authorized to
	 * operate on other users or return the user Id from the logged in user
	 * @param request
	 * @param form
	 * @return
	 */
	public static Integer getUserId(HttpServletRequest request, UserIdForm form)
	{
		Integer				userId;
		Authentication		authUser;
		User				user;
		
		authUser = SecurityContextHolder.getContext().getAuthentication();
		user = ((AuthUser)authUser.getPrincipal()).getUser();
		
		if (form.getUserId() == null)
		{
			userId = user.getUserId();
		}
		else
		{
			if (AuthUtil.hasRole(WebConstants.ROLE_ADMIN, authUser.getAuthorities()))	
			{
				userId = form.getUserId();	
			}
			else
			{
				if (!user.getUserId().equals(form.getUserId()))
				{
					logger.warn("User " + user.getUsername() + " tried to access someone else's data without appropiate rights");
				}
				
				userId = user.getUserId();
			}
		}
		
		return userId;		
	}
}
