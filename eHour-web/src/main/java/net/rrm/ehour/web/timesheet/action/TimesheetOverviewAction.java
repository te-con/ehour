/**
 * Created on Nov 9, 2006
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

package net.rrm.ehour.web.timesheet.action;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.web.calendar.CalendarUtil;
import net.rrm.ehour.web.calendar.form.NavCalendarForm;
import net.rrm.ehour.web.sort.ProjectAssignmentAggregateComparator;
import net.rrm.ehour.web.util.AuthUtil;
import net.rrm.ehour.web.util.WebConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 */

public class TimesheetOverviewAction extends BaseTimesheetAction
{
	private EhourConfig	config;
	private	CalendarUtil	calendarUtil;
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		NavCalendarForm		calendarForm = (NavCalendarForm)form;
		Calendar			requestedMonth;
		Integer				userId;
		TimesheetOverview	timesheetOverview;
		
		userId = AuthUtil.getUserId(calendarForm);
		requestedMonth = calendarUtil.getRequestedMonth(request, calendarForm);
		
		timesheetOverview = timesheetService.getTimesheetOverview(userId, requestedMonth);
		
		reSortTimesheetOverview(timesheetOverview);

		request.setAttribute("timesheetOverview", timesheetOverview);
		request.setAttribute("timesheetOverviewMonth", requestedMonth);
		request.setAttribute("config", config);
		
		setWeekdays(request);
		
		request.setAttribute("currencySymbol", WebConstants.getCurrencies().get(config.getCurrency()));
		
		return mapping.findForward("success");
	}
	
	/**
	 * Resort timesheet overview
	 * @param overview
	 */
	private void reSortTimesheetOverview(TimesheetOverview overview)
	{
		SortedSet<UserProjectStatus>	sortedSet;
		
		sortedSet = new TreeSet<UserProjectStatus>(new ProjectAssignmentAggregateComparator(ProjectAssignmentAggregateComparator.SORT_ON_CUSTOMER));
		sortedSet.addAll(overview.getProjectStatus());
		
		overview.setProjectStatus(sortedSet);
	}

	/**
	 * Set weekdays in request
	 * @param request
	 */
	private void setWeekdays(HttpServletRequest request)
	{
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		
		for (int i = 0; i < 7; i++)
		{
			request.setAttribute("day_" + i, cal.getTime());
			cal.add(Calendar.DAY_OF_WEEK, 1);
		}
	}
	
	/**
	 * @return the config
	 */
	public EhourConfig getConfig()
	{
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(EhourConfig config)
	{
		this.config = config;
	}

	/**
	 * @param calendarUtil the calendarUtil to set
	 */
	public void setCalendarUtil(CalendarUtil calendarUtil)
	{
		this.calendarUtil = calendarUtil;
	}
	
	
}
