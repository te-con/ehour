/**
 * Created on May 5, 2007
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

package net.rrm.ehour.web.report.action;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.web.report.form.QuickDateForm;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Determine report range for quick date 
 **/

public class QuickDateAction extends Action
{
	private Logger		logger = Logger.getLogger(this.getClass());
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		QuickDateForm 	quickDateForm = (QuickDateForm)form;
		DateRange		dateRange= null;
		
		if (quickDateForm.getQuickDateType().equalsIgnoreCase("week"))
		{
			dateRange = getWeek(quickDateForm);
		}
		else if (quickDateForm.getQuickDateType().equalsIgnoreCase("month"))
		{
			dateRange = getMonth(quickDateForm);
		}
		else if (quickDateForm.getQuickDateType().equalsIgnoreCase("quarter"))
		{
			dateRange = getQuarter(quickDateForm);
		}
		
		request.setAttribute("range", dateRange);
		
		return mapping.findForward("success");
	}
	
	/**
	 * Week calculation
	 * @param form
	 * @return
	 */
	private DateRange getWeek(QuickDateForm form)
	{
		DateRange	range = new DateRange();
		Calendar	cal;
		
		if (form.getRelativity() != null)
		{
			logger.debug("Quick date for relative week, relativity: " + form.getRelativity());
			
			cal = new GregorianCalendar();
			cal.add(Calendar.DAY_OF_MONTH, form.getRelativity().intValue() * 7);
			range = DateUtil.getDateRangeForWeek(cal);
		}
		
		return range;
	}
	
	/**
	 * Month calculation
	 * @param form
	 * @return
	 */
	private DateRange getMonth(QuickDateForm form)
	{
		DateRange	range = new DateRange();
		Calendar	cal;
		
		if (form.getRelativity() != null)
		{
			logger.debug("Quick date for relative month, relativity: " + form.getRelativity());
			
			cal = new GregorianCalendar();
			cal.add(Calendar.MONTH, form.getRelativity().intValue());
			range = DateUtil.getDateRangeForMonth(cal);
		}
		
		return range;
	}
	
	/**
	 * Quarter calculation
	 * @param form
	 * @return
	 */
	private DateRange getQuarter(QuickDateForm form)
	{
		DateRange	range = new DateRange();
		Calendar	cal;
		
		if (form.getRelativity() != null)
		{
			logger.debug("Quick date for relative quarter, relativity: " + form.getRelativity());
			
			cal = new GregorianCalendar();
			cal = DateUtil.addQuarter(cal, form.getRelativity().intValue());
			range = DateUtil.getDateRangeForQuarter(cal);
		}
		
		return range;
	}	
}
