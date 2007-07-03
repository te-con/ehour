/**
 * Created on Jul 3, 2007
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

package net.rrm.ehour.ui.util;

import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;

/**
 * Common stuff for auth
 **/

public class AuthUtil
{
	
	/**
	 * Check if the logged in user has the specified role
	 * @param role
	 * @return
	 */
	public static boolean hasRole(String role)
	{
		Roles roles = getRoles();
		
		return (roles != null) ? roles.contains(role) : false;
	}
	
	/**
	 * Get the roles of the logged in user
	 * @return
	 */
	public static Roles getRoles()
	{
		EhourWebSession session = EhourWebSession.getSession();
		
		Roles roles = session.getRoles();
		
		return roles;
	}
	
	/**
	 * Is the user authorized for the page?
	 * @param pageClass
	 * @return
	 */
	public static boolean userAuthorizedForPage(Class<? extends WebPage> pageClass)
	{
		AuthorizeInstantiation	authorizeAnnotation;
		Roles					userRoles;
		boolean					authorized = false;
		
		if (pageClass.isAnnotationPresent(AuthorizeInstantiation.class))
		{
			userRoles = getRoles();
			
			if (userRoles != null)
			{
				authorizeAnnotation = pageClass.getAnnotation(AuthorizeInstantiation.class);
	
				OUTER: for (String role : userRoles)
				{
					for (int i = 0; 
							i < authorizeAnnotation.value().length;
							i++)
					{
						if (authorizeAnnotation.value()[i].equalsIgnoreCase(role))
						{
							authorized = true;
							break OUTER;
						}
					}
				}
			}
		}
		else
		{
			// no authorize annotation available
			authorized = true;
		}
		
		return authorized;
	}
}
