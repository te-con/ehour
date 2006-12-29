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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.web.timesheet.form.TimesheetViewForm;
import net.rrm.ehour.web.util.AuthUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 */

public class TimesheetOverviewAction extends BaseTimesheetAction
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		TimesheetViewForm	timesheetViewForm = (TimesheetViewForm)form;
		Calendar			requestedMonth;
		Integer				userId;
		TimesheetOverview	timesheetOverview;
		
		requestedMonth = timesheetViewForm.getCalendar();
		
		// if none supplied, use the current date
		if (requestedMonth == null)
		{
			requestedMonth = new GregorianCalendar();
			requestedMonth.set(Calendar.DAY_OF_MONTH, 1);
		}
		
		userId = AuthUtil.getUserId(request, timesheetViewForm);
		
		timesheetOverview = timesheetService.getTimesheetOverview(userId, requestedMonth);

		request.setAttribute("timesheetOverview", timesheetOverview);
		request.setAttribute("timesheetOverviewMonth", requestedMonth);
		
		return mapping.findForward("success");
	}
}
