/**
 * Created on Dec 14, 2006
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

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.web.util.AuthUtil;
import net.rrm.ehour.web.util.WebConstants;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class GetNavCalendarAction extends Action
{
	private	CalendarUtil	calendarUtil;
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward 	fwd = mapping.findForward("success");
		NavCalendarForm	ncForm = (NavCalendarForm)form;
		Integer			userId;
		Calendar		requestedMonth;
		boolean[]		monthOverview;
		
		// get the requested userId
		userId = AuthUtil.getUserId(request, ncForm);
		
		// get requested month, either from session or request
		requestedMonth = getRequestedMonth(request, ncForm);
		
		// get the actual data and put in the request context
		monthOverview = calendarUtil.getMonthNavCalendar(userId, requestedMonth);
		
		request.setAttribute("navCalData", monthOverview);
		request.setAttribute("navCalCurCalMonth", requestedMonth);
		
		// set the navigation urls
		setNavParams(request, requestedMonth, userId);
		
		return fwd;
	}

	
	/**
	 * Determine if we need the month stored in the session or the one in the request
	 * or if this is a new date
	 * @param request HttpServletRequest
	 * @return Calendar
	 */

	public Calendar getRequestedMonth(HttpServletRequest request, NavCalendarForm form)
	{
		HttpSession session;
		int year;
		int month;
		Calendar nowCalendar;

		session = request.getSession();

		nowCalendar = new GregorianCalendar();

		if (form.getYear() != null)
		{
			year = form.getYear().intValue();
		}
		else if (session.getAttribute(WebConstants.SESSION_CALENDAR_YEAR_KEY) != null)
		{
			year = ((Integer) session.getAttribute(WebConstants.SESSION_CALENDAR_YEAR_KEY)).intValue();
		} else
		{
			year = nowCalendar.get(Calendar.YEAR);
		}

		if (form.getMonth() != null)
		{
			month = form.getMonth().intValue();
		}
		else if (session.getAttribute(WebConstants.SESSION_CALENDAR_MONTH_KEY) != null)
		{
			month = ((Integer) session.getAttribute(WebConstants.SESSION_CALENDAR_MONTH_KEY)).intValue();
		} else
		{
			month = nowCalendar.get(Calendar.MONTH);
		}

		return new GregorianCalendar(year, month, 1);
	}	
	
	/**
	 * Set next/previous month parameters in request context
	 * @param request
	 * @param currentMonth
	 */
	private void setNavParams(HttpServletRequest request, Calendar currentMonth, Integer userId)
	{
		Calendar	prevMonth;
		Calendar	nextMonth;
		String		prevMonthParam;
		String		nextMonthParam;

		prevMonth = (Calendar)currentMonth.clone();
		nextMonth = (Calendar)currentMonth.clone();
		
		prevMonth.add(Calendar.MONTH, -1);
		nextMonth.add(Calendar.MONTH, 1);
		
		prevMonthParam = prevMonth.get(Calendar.MONTH) + ", " + prevMonth.get(Calendar.YEAR) + ", " + userId.toString();
		
		request.setAttribute("navCalPrevMonth", prevMonthParam);

		nextMonthParam = nextMonth.get(Calendar.MONTH) + ", " + nextMonth.get(Calendar.YEAR) + ", " + userId.toString();
		
		request.setAttribute("navCalNextMonth", nextMonthParam);
	}	
	
	/**
	 * Set calendar util
	 * @param calUtil
	 */
	
	public void setCalendarUtil(CalendarUtil calUtil)
	{
		this.calendarUtil = calUtil;
	}
	
	
	
	
}
