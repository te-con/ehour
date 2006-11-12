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
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import net.rrm.ehour.user.dto.AuthUser;
import net.rrm.ehour.web.util.AuthUtil;
import net.rrm.ehour.web.util.WebConstants;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * TODO 
 **/

public class CalendarFilter extends HttpServlet implements Filter
{
    private Logger                      logger;

    /**
     * 
     */
    
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
        HttpServletRequest  httpRequest;
        HttpSession			session;
        User                user = null;
        ApplicationContext 	ctx;
        CalendarUtil		calendarUtil;
        boolean[]			monthOverview;
        
        user = needToProcessRequest(request);
        
        if (user != null)
        {
        	httpRequest = (HttpServletRequest)request;
        	
        	if (user != null)
        	{
        		session = httpRequest.getSession();
        		ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        		calendarUtil = (CalendarUtil)ctx.getBean("calendarUtil");
        		
        		monthOverview = calendarUtil.getMonthNavCalendar(user.getUserId(), getRequestedMonth(httpRequest));
        		
        		request.setAttribute(WebConstants.REQUEST_NAVCAL_DATA, monthOverview);
        	}
        }
        
        chain.doFilter(request, response);
	}
	
    /**
    * Determine if we need the month stored in the session
    * or if this is a new date
    * @param request HttpServletRequest
    * @return Calendar
    */

   public Calendar getRequestedMonth(HttpServletRequest request)
   {
       HttpSession 	session;
       int 			year;
       int			month;
       String 		cps_year;
       String		cps_month;
       Calendar 	nowCalendar;

       session = request.getSession();

       nowCalendar = new GregorianCalendar();
       cps_year = (String)request.getParameter(WebConstants.CALENDAR_YEAR_KEY);

       if (cps_year != null)
       {
           year = Integer.parseInt(cps_year);
       }
       else if (session.getAttribute(WebConstants.CALENDAR_YEAR_KEY) != null)
       {
           year = ((Integer)session.getAttribute(WebConstants.CALENDAR_YEAR_KEY)).intValue();
       }
       else
       {
           year = nowCalendar.get(Calendar.YEAR);
       }

       cps_month = (String)request.getParameter(WebConstants.CALENDAR_MONTH_KEY);

       if (cps_month != null)
       {
           month = Integer.parseInt(cps_month);
       }
       else if (session.getAttribute(WebConstants.CALENDAR_MONTH_KEY) != null)
       {
           month = ((Integer)session.getAttribute(WebConstants.CALENDAR_MONTH_KEY)).intValue();
       }
       else
       {
           month = nowCalendar.get(Calendar.MONTH);
       }

       return new GregorianCalendar(year, month, 1);
   }	

	/**
	 * Process request? If it's not HTTP (unlikely), the URL is an admin URL or the user
	 * does not have to book hours (not have ROLE_CONSULTANT): ignore it
	 * 
	 * @param request
	 * @return User object or null if the request is to be ignored
	 */
	private User needToProcessRequest(ServletRequest request)
	{
		HttpServletRequest	httpRequest;
		User				user = null;
		Authentication		authUser;
		
		if (request instanceof HttpServletRequest)
		{
			httpRequest = (HttpServletRequest)request;
			
	        // do not for admin urls
			// @todo hardcore URL
	        if (httpRequest.getRequestURI().indexOf("admin/") < 0)
	        {
				authUser = SecurityContextHolder.getContext().getAuthentication();
				
				user = ((AuthUser)authUser.getPrincipal()).getUser();
				
	        	// is user billable? if not, don't bother with the calendar data
	        	if (!AuthUtil.hasRole(WebConstants.ROLE_CONSULTANT, authUser.getAuthorities()))
	        	{
	        		if (logger.isDebugEnabled())
	        		{
	        			logger.debug("User " + user.getUsername() + " is not billable, not fetching calendar data");
	        		}

	        		user = null;
	        	}
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
        logger = Logger.getLogger(this.getClass().getName());
        
        if (logger.isDebugEnabled())
        {
        	logger.debug("CalendarFilter initialized");
        }
	}
}
