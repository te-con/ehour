/**
 * Created on Dec 28, 2006
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.web.timesheet.dto.TimesheetRow;
import net.rrm.ehour.web.timesheet.form.TimesheetForm;
import net.rrm.ehour.web.timesheet.util.TimesheetFormAssembler;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Get timesheet form
 **/

public class GetTimesheetFormAction extends BaseTimesheetAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
									HttpServletRequest request, HttpServletResponse response)
	{
		ActionForward 		fwd = null;
		TimesheetForm 		timesheetForm = (TimesheetForm)form;
		Calendar	  		requestedWeek;
		WeekOverview		weekOverview;
		List<TimesheetRow>	timesheetRows;
		List<Date>			dateSequence;
		TimesheetFormAssembler	timesheetFormAssembler = new TimesheetFormAssembler();
		
		requestedWeek = timesheetForm.getCalendar();
		
		weekOverview = timesheetService.getWeekOverview(timesheetForm.getUserId(), requestedWeek);
		
		dateSequence = createDateSequence(weekOverview);
		timesheetRows = timesheetFormAssembler.createTimesheetForm(weekOverview, dateSequence);
		
		request.setAttribute("timesheetRows", timesheetRows);
		request.setAttribute("dateSeq", createDateSequence(weekOverview));
		request.setAttribute("weekDate", weekOverview.getWeekRange().getDateStart());

		fwd = mapping.findForward("success");
		
		return fwd;
	}

	/**
	 * Create a sequence of dates from the date range
	 * @param weekOverview
	 * @return
	 */
	private List<Date> createDateSequence(WeekOverview weekOverview)
	{
		List<Date>	dateSequence = new ArrayList<Date>();
		DateRange	weekRange;
		Calendar	calendar;
		
		weekRange = weekOverview.getWeekRange();
		
		calendar = new GregorianCalendar();
		calendar.setTime(weekRange.getDateStart());
		
		while (calendar.getTime().before(weekRange.getDateEnd()))
		{
			dateSequence.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return dateSequence;
	}
}
