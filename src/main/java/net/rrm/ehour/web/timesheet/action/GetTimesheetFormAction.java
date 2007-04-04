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

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.web.timesheet.dto.Timesheet;
import net.rrm.ehour.web.timesheet.form.TimesheetForm;
import net.rrm.ehour.web.timesheet.util.TimesheetFormAssembler;
import net.rrm.ehour.web.util.AuthUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Get timesheet form
 **/

public class GetTimesheetFormAction extends BaseTimesheetAction
{
	private EhourConfig	config;
	
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
		Integer				userId;
		Timesheet			timesheet;
		
		TimesheetFormAssembler	timesheetFormAssembler = new TimesheetFormAssembler();

		userId = AuthUtil.getUserId(timesheetForm);

		requestedWeek = timesheetForm.getCalendar();
		
		// if accessed directly, display the current week as it's not provided
		if (requestedWeek == null)
		{
			requestedWeek = new GregorianCalendar();
		}
		
		weekOverview = timesheetService.getWeekOverview(userId, requestedWeek);
		
		timesheet = timesheetFormAssembler.createTimesheetForm(weekOverview);

		timesheet.setUserId(userId);
		
		request.setAttribute("timesheet", timesheet);
		
		// needed when ran standalone
		// TODO move wrapped config to session
		if (config != null)
		{
			request.setAttribute("config", config);
		}

		fwd = mapping.findForward("success");
		
		response.setHeader("Cache-Control", "no-cache");
		
		return fwd;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(EhourConfig config)
	{
		this.config = config;
	}

}
