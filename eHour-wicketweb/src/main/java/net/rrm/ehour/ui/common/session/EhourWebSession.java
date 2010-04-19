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

package net.rrm.ehour.ui.common.session;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.common.authorization.AuthUser;
import net.rrm.ehour.ui.common.cache.ObjectCache;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Ehour Web session
 **/

public class EhourWebSession extends AuthenticatedWebSession
{
	@SpringBean
	private EhourConfig 	ehourConfig;
	@SpringBean
	private AuditService	auditService;
	private Calendar 		navCalendar;
	private	UserCriteria	userCriteria;
	private ObjectCache		reportCache = new ObjectCache();
	private Boolean			hideInactiveSelections = Boolean.TRUE;
	
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
		CommonWebUtil.springInjection(this);
		
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
	 * @return the hideInactiveSelections
	 */
	public Boolean getHideInactiveSelections()
	{
		return hideInactiveSelections;
	}

	/**
	 * @param hideInactiveSelections the hideInactiveSelections to set
	 */
	public void setHideInactiveSelections(Boolean hideInactiveSelections)
	{
		this.hideInactiveSelections = hideInactiveSelections;
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
			
			User user = ((AuthUser)authResult.getPrincipal()).getUser();
			
			auditService.doAudit(new Audit()
										.setAuditActionType(AuditActionType.LOGIN)
										.setUser(user)
										.setUserFullName(user.getFullName())
										.setDate(new Date())
										.setSuccess(Boolean.TRUE));												
		
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
				Collection<GrantedAuthority> authorities = auth.getAuthorities();

				for (GrantedAuthority grantedAuthority : authorities)
				{
					roles.add(grantedAuthority.getAuthority());
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
		
		auditService.doAudit(new Audit()
			.setAuditActionType(AuditActionType.LOGOUT)
			.setUser(user.getUser())
			.setUserFullName(user.getUser().getFullName())
			.setDate(new Date())
			.setSuccess(Boolean.TRUE));			
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
	public ObjectCache getObjectCache()
	{
		return reportCache;
	}
}
