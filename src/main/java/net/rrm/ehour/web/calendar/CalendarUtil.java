/**
 * Created on Nov 10, 2006
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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.web.calendar.form.NavCalendarForm;
import net.rrm.ehour.web.util.WebConstants;

/**
 *  
 **/

public class CalendarUtil
{
	private TimesheetService timesheetService;

	/**
	 * Set timesheet service
	 * @param ts
	 */
	public void setTimesheetService(TimesheetService ts)
	{
		timesheetService = ts;
	}

	/**
	 * Get the month overview for the nav calendar
	 * @param userId
	 * @param requestedMonth
	 * @return array of booleans each representing a day in the month. 
	 * true = booked, false = not everything booked
	 */

	public boolean[] getMonthNavCalendar(Integer userId, Calendar requestedMonth)
	{
		List<BookedDay> bookedDays;
		boolean[] 		monthOverview;
		Calendar 		cal;

		bookedDays = timesheetService.getBookedDaysMonthOverview(userId, requestedMonth);

		monthOverview = new boolean[DateUtil.getDaysInMonth(requestedMonth)];

		for (BookedDay day : bookedDays)
		{
			cal = new GregorianCalendar();
			cal.setTime(day.getDate());

			// just in case.. it shouldn't happen that the returned month
			// is longer than the reserverd space for this month
			if (cal.get(Calendar.DAY_OF_MONTH) <= monthOverview.length)
			{
				monthOverview[cal.get(Calendar.DAY_OF_MONTH) - 1] = true;
			}
		}

		return monthOverview;
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
			
			if (!form.isZeroBased())
			{
				month--;
			}
					
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
}
