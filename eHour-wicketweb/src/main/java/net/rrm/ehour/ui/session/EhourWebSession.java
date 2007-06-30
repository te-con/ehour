/**
 * Created on Jun 2, 2007
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

package net.rrm.ehour.ui.session;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * TODO 
 **/

public class EhourWebSession extends AuthenticatedWebSession 
{
	@SpringBean
	private EhourConfig	ehourConfig;
	
	private	User		user;
	private	Calendar	navCalendar;
	private static final long serialVersionUID = 93189812483240412L;

	/**
	 * 
	 * @param app
	 * @param req
	 */
	public EhourWebSession(final AuthenticatedWebApplication application, Request req)
	{
		super(application, req);
		
		InjectorHolder.getInjector().inject(this);
	}
	
	/**
	 * Get ehour config
	 * @return
	 */
	public EhourConfig getEhourConfig()
	{
		return ehourConfig;
	}

	/**
	 * @return the navCalendar
	 */
	public Calendar getNavCalendar()
	{
		if (navCalendar == null)
		{
			navCalendar = DateUtil.getCalendar(ehourConfig);
			navCalendar = new GregorianCalendar();
			navCalendar.add(Calendar.MONTH, -2);
		}
		
		return (Calendar)navCalendar.clone();
	}

	/**
	 * @param navCalendar the navCalendar to set
	 */
	public void setNavCalendar(Calendar navCalendar)
	{
		this.navCalendar = navCalendar;
	}
	
	/**
	 * Get logged in user id
	 * TODO auth
	 * @return
	 */
	public User getUser()
	{
		User user = null;
		
		if (isSignedIn())
		{
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			user = (User) authentication.getPrincipal();
		}
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	@Override
	public boolean authenticate(String username, String password)
	{
        String u = username == null ? "" : username;
        String p = password == null ? "" : password;

        // Create an Acegi authentication request.
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(u, p);

        // Attempt authentication.
        try {
            AuthenticationManager authenticationManager = ((EhourWebApplication) getApplication()).getAuthenticationManager();
            Authentication authResult = authenticationManager.authenticate(authRequest);
            setAuthentication(authResult);

            // TODO
            System.out.println("Login by user '" + username + "'.");
            return true;

        } catch (BadCredentialsException e) {
        	System.out.println("Failed login by user '" + username + "'.");
            setAuthentication(null);
            return false;

        } catch (AuthenticationException e) {
        	e.printStackTrace();
        	System.out.println("Could not authenticate a user");
            setAuthentication(null);
            throw e;

        } catch (RuntimeException e) {
        	e.printStackTrace();
        	System.out.println("Unexpected exception while authenticating a user");
            setAuthentication(null);
            throw e;
        }	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
	 */
	@Override
    public Roles getRoles()
	{
		if (isSignedIn())
		{
			Roles roles = new Roles();
			// Retrieve the granted authorities from the current authentication. These correspond one on
			// one with user roles.
			GrantedAuthority[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			for (int i = 0; i < authorities.length; i++)
			{
				GrantedAuthority authority = authorities[i];
				roles.add(authority.getAuthority());
			}
			return roles;
		}
		return null;
	}
	
    public void signout() {
        User user = getUser();
        if (user != null) {
        	// TODO logg
            System.out.println("Logout by user '" + user.getUsername() + "'.");
        }
        setAuthentication(null);
        invalidate();
    }	
    
    /**
     * Sets the acegi authentication.
     * @param authentication the authentication or null to clear 
     */
    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    /**
	 * @return the current YourApp session
	 */
	public static EhourWebSession getYourAppSession()
	{
		return (EhourWebSession) Session.get();
	}    
}

