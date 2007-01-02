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
import java.util.Date;
import java.util.Enumeration;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.web.timesheet.form.TimesheetForm;
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
		
		userId = AuthUtil.getUserId(request, timesheetForm);
		
		entries = getTimesheetEntries(request);
		
		timesheetService.persistTimesheetEntries(entries);
		
		return null;
	}
	
	/**
	 * Parse timesheet entries out of the request
	 * @param userId
	 * @param request
	 */
	@SuppressWarnings("unchecked")
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
