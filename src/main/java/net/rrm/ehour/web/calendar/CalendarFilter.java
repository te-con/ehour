/**
 * Created on Nov 5, 2006
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

package net.rrm.ehour.web.calendar;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.web.util.WebConstants;

import org.apache.log4j.Logger;

/**
 * TODO 
 **/

public class CalendarFilter extends HttpServlet implements Filter
{
    private FilterConfig                filterConfig;
    private Logger                      logger;

    /**
     * 
     */
    
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
        HttpSession         session;
        HttpServletRequest  httpRequest;
        User                user = null;

        user = needToProcessRequest(request);
        
        if (user != null)
        {
        	httpRequest = (HttpServletRequest)request;
        	
        	
        	if (user != null)
        	{
        		
        	}
        }
        
	}

	/**
	 * Process request? If it's not HTTP (unlikely), the URL is an admin URL or the user
	 * does not have to book hours: ignore it
	 * 
	 * @param request
	 * @return User object or null if the request is to be ignored
	 */
	private User needToProcessRequest(ServletRequest request)
	{
		HttpServletRequest	httpRequest;
		HttpSession			session;
		User				user = null;
		
		if (request instanceof HttpServletRequest)
		{
			httpRequest = (HttpServletRequest)request;
			
	        // do not for admin urls
			// @todo hardcore URL
	        if (httpRequest.getRequestURI().indexOf("admin/") < 0)
	        {
	        	session = httpRequest.getSession();
	        	user = (User)session.getAttribute(WebConstants.SESSION_USER);
	        	
	        	// is user billable? if not, don't bother with the calendar data
	        	// @todo hook up with asegi
//	        	if (!AuthUtil.hasAuthorisation(user.getUserTypes(), EhourConstants.USERTYPE_CONSULTANT))
//	        	{
//	        		if (logger.isDebugEnabled())
//	        		{
//	        			logger.debug("User " + user.getUsername() + " is not billable, not fetching calendar data");
//	        		}
//
//	        		user = null;
//	        	}
	        }			
		}
		else
		{
			logger.error("CalendarFilter received a non-HTTP request !?");
		}
		
		return user;
	}

	
	/**
	 * Initialize
	 */
	public void init(FilterConfig filterConfig) throws ServletException
	{
        this.filterConfig = filterConfig;
        logger = Logger.getLogger(this.getClass().getName());
        
        if (logger.isDebugEnabled())
        {
        	logger.debug("CalendarFilter initialized");
        }
	}
}
