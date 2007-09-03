/**
 * Created on Jul 3, 2007
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

package net.rrm.ehour.ui.util;

import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;

/**
 * Common stuff for auth
 **/

public class AuthUtil
{
	/**
	 * Get logged in user
	 * @return
	 */
	public static User getUser()
	{
		EhourWebSession session = EhourWebSession.getSession();
		
		return (session.getUser() != null) ? session.getUser().getUser() : null;
	}
	
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
