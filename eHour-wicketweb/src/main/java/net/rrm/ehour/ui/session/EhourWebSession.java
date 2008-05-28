/**
 * Created on Jun 2, 2007
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

package net.rrm.ehour.ui.session;

import java.util.Calendar;
import java.util.Locale;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.authorization.AuthUser;
import net.rrm.ehour.util.DateUtil;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.log4j.Logger;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Ehour Web session
 **/

public class EhourWebSession extends AuthenticatedWebSession
{
	@SpringBean
	private EhourConfig 	ehourConfig;
	private Calendar 		navCalendar;
	private	UserCriteria	userCriteria;
	private ReportCache		reportCache = new ReportCache();
	
	private	static Logger logger = Logger.getLogger(EhourWebSession.class);
	
	private static final long serialVersionUID = 93189812483240412L;

	/**
	 * 
	 * @param app
	 * @param req
	 */
	public EhourWebSession(Request req)
	{
		super(req);
		
		reloadConfig();
	}
	
	/**
	 * 
	 */
	public void reloadConfig()
	{
		InjectorHolder.getInjector().inject(this);
		
		if (!ehourConfig.isDontForceLanguage())
		{
			logger.debug("Setting locale to " + ehourConfig.getLocale().getDisplayLanguage());

			Locale.setDefault(ehourConfig.getLocale());
			setLocale(ehourConfig.getLocale());
		} else
		{
			logger.debug("Not forcing locale, using browser's locale");
		}
	}

	/**
	 * Get ehour config
	 * 
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
		}

		return (Calendar) navCalendar.clone();
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
	 * @return
	 */
	public AuthUser getUser()
	{
		AuthUser	user = null;

		if (isSignedIn())
		{
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			if (authentication != null)
			{
				user = (AuthUser) authentication.getPrincipal();
			}
		}
		return user;
	}

	/**
	 * Authenticate based on username/pass
	 */
	@Override
	public boolean authenticate(String username, String password)
	{
		String u = username == null ? "" : username;
		String p = password == null ? "" : password;
		
		// Create an Acegi authentication request.
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(u, p);
		
		// Attempt authentication.
		try
		{
			AuthenticationManager authenticationManager = ((EhourWebApplication) getApplication()).getAuthenticationManager();
			
			if (authenticationManager == null)
			{
				throw new AuthenticationServiceException("no authentication manager defined");
			}
			
			Authentication authResult = authenticationManager.authenticate(authRequest);
			setAuthentication(authResult);

			logger.info("Login by user '" + username + "'.");
			return true;

		} catch (BadCredentialsException e)
		{
			logger.info("Failed login by user '" + username + "'.");
			setAuthentication(null);
			return false;

		} catch (AuthenticationException e)
		{
			logger.info("Could not authenticate a user", e);
			setAuthentication(null);
			throw e;

		} catch (RuntimeException e)
		{
			logger.info("Unexpected exception while authenticating a user", e);
			setAuthentication(null);
			throw e;
		}
	}

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
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			
			if (auth != null)
			{
				
				GrantedAuthority[] authorities = auth.getAuthorities();
				
				for (int i = 0; i < authorities.length; i++)
				{
					GrantedAuthority authority = authorities[i];
					roles.add(authority.getAuthority());
				}
				
				if (roles.size() == 0)
				{
					logger.warn("User " + auth.getPrincipal() + " logged in but no roles could be found!");
				}
				
				return roles;
			}
			else
			{
				logger.warn("User is signed in but authentication is not set!");
			}
		}
		return null;
	}

	/**
	 * Invalidate authenticated user
	 */
	public void signOut()
	{
		AuthUser user = getUser();

		if (user != null)
		{
			logger.info("Logout by user '" + user.getUsername() + "'.");
		}
		
		setAuthentication(null);
		invalidate();
		super.signOut();
	}

	/**
	 * Sets the acegi authentication.
	 * @param authentication the authentication or null to clear 
	 */
	private void setAuthentication(Authentication authentication)
	{
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	/**
	 * @return the current session
	 */
	public static EhourWebSession getSession()
	{
		return (EhourWebSession) Session.get();
	}

	/**
	 * @return the userCriteria
	 */
	public UserCriteria getUserCriteria()
	{
		return userCriteria;
	}

	/**
	 * @param userCriteria the userCriteria to set
	 */
	public void setUserCriteria(UserCriteria userCriteria)
	{
		this.userCriteria = userCriteria;
	}

	/**
	 * @return the reportCache
	 */
	public ReportCache getReportCache()
	{
		return reportCache;
	}
}
