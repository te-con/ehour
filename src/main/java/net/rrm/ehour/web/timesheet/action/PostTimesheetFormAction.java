/**
 * Created on Jan 2, 2007
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.timesheet.domain.TimesheetComment;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.web.timesheet.dto.Timesheet;
import net.rrm.ehour.web.timesheet.form.TimesheetForm;
import net.rrm.ehour.web.timesheet.util.TimesheetFormAssembler;
import net.rrm.ehour.web.util.AuthUtil;
import net.rrm.ehour.web.util.DomainAssembler;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class PostTimesheetFormAction extends BaseTimesheetAction
{
	private Logger	logger = Logger.getLogger(PostTimesheetFormAction.class);
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
									HttpServletRequest request, HttpServletResponse response)
	{
		TimesheetForm			timesheetForm = (TimesheetForm)form;
		Integer					userId;
		SortedSet<TimesheetEntry>	entries;
		WeekOverview			weekOverview;
		Timesheet				timesheet;
		TimesheetFormAssembler	timesheetFormAssembler = new TimesheetFormAssembler();
		Calendar				requestedWeek;
		TimesheetComment		comment;
		
		userId = AuthUtil.getUserId(timesheetForm);
		
		// first persist entries
		comment = DomainAssembler.getTimesheetComment(userId, timesheetForm);
		
		if (comment == null || comment.getComment().length() <= 1023)
		{
			entries = getTimesheetEntries(request);
			timesheetService.persistTimesheet(entries, comment);
			
			// second return the next week
			requestedWeek = new GregorianCalendar();
			
			if (!entries.isEmpty())
			{
				requestedWeek.setTime(entries.first().getEntryId().getEntryDate());
				requestedWeek.add(Calendar.DAY_OF_MONTH, 7);
			}
			else
			{
				logger.debug("Submitted but no entries given");
				// no entries given, use the form date
				requestedWeek = new GregorianCalendar(timesheetForm.getSheetYear(), 	
														timesheetForm.getSheetMonth() - 1,
														timesheetForm.getSheetDay());
				requestedWeek.add(Calendar.DAY_OF_MONTH, 7);
			}
		}
		else
		{
			logger.warn("Timesheet comments too long (should be checked by required javascript??. UserID " + userId);

			requestedWeek = new GregorianCalendar(timesheetForm.getSheetYear(), 	
								timesheetForm.getSheetMonth() - 1,
								timesheetForm.getSheetDay());

			request.setAttribute("errorCommentTooLong", true);
		}
		
		logger.debug("Next week is " + requestedWeek.getTime());

		weekOverview = timesheetService.getWeekOverview(userId, requestedWeek);
		
		timesheet = timesheetFormAssembler.createTimesheetForm(weekOverview);
		timesheet.setUserId(userId);
		
		request.setAttribute("timesheet", timesheet);
		
		response.setHeader("Cache-Control", "no-cache");
		
		return mapping.findForward("success");
	}
	
	/**
	 * Parse timesheet entries out of the request
	 * @param userId
	 * @param request
	 */
	private SortedSet<TimesheetEntry> getTimesheetEntries(HttpServletRequest request)
	{
		SortedSet<TimesheetEntry>	timesheetEntries = new TreeSet<TimesheetEntry>();
		TimesheetEntry			timesheetEntry;
		Float					hours;
		String[]				decomposedKey;
		Integer					assignmentId;
		SimpleDateFormat		dateFormatter = new SimpleDateFormat("yyyyMMdd");
		Date					entryDate;
		Enumeration				paramEnum;
		String					key;
		String					paramValue = null;
		
		paramEnum = request.getParameterNames();
		
		while (paramEnum.hasMoreElements())
		{
			key = (String)paramEnum.nextElement();
			paramValue = null;
			
		
			if (key.startsWith("ehts_"))
			{
				try
				{
					paramValue = request.getParameter(key);
					
					if (paramValue == null || "".equals(paramValue))
					{
						continue;
					}
					
					hours = new Float(paramValue);
					
					decomposedKey = key.split("_");
					
					assignmentId = Integer.parseInt(decomposedKey[1]);
					
					entryDate = dateFormatter.parse(decomposedKey[2]);
					entryDate = DateUtil.nullifyTime(entryDate);
					
					timesheetEntry = DomainAssembler.getTimesheetEntry(assignmentId, entryDate, hours);
					
					timesheetEntries.add(timesheetEntry);
				}
				catch (NumberFormatException nfe)
				{
					logger.error("while parsing timesheet hour " + paramValue + " a " + nfe.getMessage() + " occured on key " + key);
					continue;
				}
				catch (ParseException pe)
				{
					logger.error("while parsing timesheet hour " + paramValue + " a " + pe.getMessage() + " occured on key " + key);
					continue;
					
				}
			}
		}
		
		return timesheetEntries;
	}
}
