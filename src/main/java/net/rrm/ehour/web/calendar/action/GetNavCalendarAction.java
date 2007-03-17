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

package net.rrm.ehour.web.calendar.action;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.web.calendar.CalendarUtil;
import net.rrm.ehour.web.calendar.form.NavCalendarForm;
import net.rrm.ehour.web.util.AuthUtil;
import net.rrm.ehour.web.util.WebConstants;

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
		HttpSession		session;
		
		// get the requested userId
		userId = AuthUtil.getUserId(ncForm);
		
		// get requested month, either from session or request
		requestedMonth = calendarUtil.getRequestedMonth(request, ncForm);
		
		// get the actual data and put in the request context
		monthOverview = calendarUtil.getMonthNavCalendar(userId, requestedMonth);
		
		request.setAttribute("navCalData", monthOverview);
		request.setAttribute("navCalCurCalMonth", requestedMonth);
		request.setAttribute("navCalUserId", userId);
		
		// set the navigation urls
		setNavParams(request, requestedMonth, userId);
		
		// store current date in session
		session = request.getSession();
		session.setAttribute(WebConstants.SESSION_CALENDAR_MONTH_KEY, requestedMonth.get(Calendar.MONTH));
		session.setAttribute(WebConstants.SESSION_CALENDAR_YEAR_KEY, requestedMonth.get(Calendar.YEAR));
		
		response.setHeader("Cache-Control", "no-cache");
		
		return fwd;
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
