/**
 * Created on Nov 13, 2006
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

package net.rrm.ehour.web.calendar.tag;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;



/**
 * TODO 
 **/

public class NavCalendarTag extends CalendarTag
{
	private static final long serialVersionUID = 2422922719761190399L;

	// HTML constants
	private String	HTML_NEW_ROW = "<TR onmouseover=\"this.className='cps_tableTROn';\" " +
		 								"onmouseout=\"this.className='cps_tableTROff';\"  onclick=\"\">";

	private String	HTML_FIRST_CELL = "<TD class=\"cps_dayData\" style=\"border-left: 0px\">";
	private String	HTML_CELL_COMPLETE = "<TD class=\"cps_dayData\">";
	private String	HTML_CELL_NOT_COMPLETE = "<TD class=\"cps_dayNoData\">";	
	
	private boolean[] 	bookedDays;
	
	private Logger	logger = Logger.getLogger(NavCalendarTag.class);
	
	/**
	 *
	 * @return int
	 * @throws JspException
	 */
	@Override
	public int doStartTag() throws JspException
	{
		JspWriter 		out;
		StringBuffer	sb;
		
		out = pageContext.getOut();

		try
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Creating nav calendar for " + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1));
			}
			
			sb = createCalendar(calendar);
			
			out.write(sb.toString());
			
		} catch (Throwable t)
		{
			logger.error("Exception while creating calendar for month " + calendar.toString());
	        StringWriter sw = new StringWriter();
	        t.printStackTrace(new PrintWriter(sw));
	        logger.error(t.toString());
			
			// big chance this fails as well but at least we can try
			try
			{
				out.write("<tr><td colspan=3>Something went wrong..</td></tr>");
			} catch (IOException e)
			{
			}
		}
		
		return SKIP_BODY;
	}
	
	/**
	 * Create calendar (protected for junit)
	 * @param calendar
	 * @return
	 */
	
	private StringBuffer createCalendar(Calendar calendar)
	{
		int				currentColumn;
		StringBuffer	sb = new StringBuffer();
		
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		
		currentColumn = prependPreviousMonthDays(calendar, sb);
		currentColumn = writeCalendar(currentColumn, sb);
		appendNextMonthDays(currentColumn, sb);
		
		sb.append(HTML_ROW_CLOSE);
		
		return sb;
	}
	
	/**
	 * 
	 * @param currentColumn
	 * @param out
	 * @throws IOException
	 */
	private void appendNextMonthDays(int currentColumn, StringBuffer out) 
	{
		if (currentColumn > 0)
		{
			for (;
				 currentColumn <= 6;
				 currentColumn++)
			{
				out.append(getHtmlCell(currentColumn, null));
				out.append(HTML_NBSP);
				out.append(HTML_CELL_CLOSE);
			}
		}
		
	}
	/**
	 * 
	 * @param out
	 * @throws IOException
	 */
	private int writeCalendar(int currentColumn, StringBuffer out) 
	{
		int	month = calendar.get(Calendar.MONTH);
		int days = 0;
		int daysInMonth = DateUtil.getDaysInMonth(calendar);
		int row = 0;
		
		while (calendar.get(Calendar.MONTH) == month 
				&& days++ <= daysInMonth)
		{
			// first determine if this calendar entry should be marked as completely booked or not
			if (currentColumn == 0)
			{
				// @todo row might not be closed by prependdays
				out.append(HTML_NEW_ROW);
				
				row++;
			}
			
			out.append(getHtmlCell(currentColumn, calendar));
			
			// write the day of the month
			out.append("" + calendar.get(Calendar.DAY_OF_MONTH));
			out.append(HTML_CELL_CLOSE);
			
			currentColumn++;
			
			if (currentColumn == 7)
			{
				out.append(HTML_ROW_CLOSE);
				currentColumn = 0;
			}
			
			calendar.add(Calendar.HOUR, 24);
		}
		
		return currentColumn;
	}
	
	/**
	 * 
	 * @param out
	 */
	private int prependPreviousMonthDays(Calendar calendar, StringBuffer out) 
	{
		int i;
		int	currentColumn;
		
		currentColumn = calendar.get(Calendar.DAY_OF_WEEK);

		logger.debug("Filling up first week to column " + currentColumn);
		
		if (currentColumn != Calendar.SUNDAY)
		{
			currentColumn--;
			out.append(HTML_NEW_ROW);
			out.append(getHtmlCell(currentColumn, null));
			out.append(HTML_NBSP);
			out.append(HTML_CELL_CLOSE);

			for (i = 1; i < currentColumn; i++)
			{
				out.append(getHtmlCell(currentColumn, null));
				out.append(HTML_NBSP);
				out.append(HTML_CELL_CLOSE);
			}
		}
		else
		{
			// sunday should be 0
			currentColumn --;
		}
		
		return currentColumn;
	}	


	/**
	 * 
	 * @return
	 */
	public boolean[] getBookedDays()
	{
		return bookedDays;
	}
	
	/**
	 * 
	 * @param bookedDays
	 */
	public void setBookedDays(boolean[] bookedDays)
	{
		this.bookedDays = bookedDays;
	}

	/**
	 * Determine what style to apply to a cell
	 * Prepended days are always complete
	 * Sundays & saturdays are styled as complete
	 * Completely booked days are style as complete (doh)
	 * rest is not complete 
	 */
	private String getHtmlCell(int currentColumn, Calendar date)
	{
		String	cellTag;
		
		// sundays are always marked as complete (@todo make it configurable?)
		if (currentColumn == 0)
		{
			cellTag = HTML_FIRST_CELL;
		}
		// saturdays, prepended days and complete days are styled as complete
		else if (currentColumn == 6 || date == null || dayComplete(date.get(Calendar.DAY_OF_MONTH)))
		{
			cellTag = HTML_CELL_COMPLETE;
		}
		else
		{
			cellTag = HTML_CELL_NOT_COMPLETE;
		}

		return cellTag;
	}	

	
	/**
	 * Check if a day is complete
	 */
	private boolean dayComplete(int dayOfMonth)
	{
		return bookedDays != null && bookedDays[dayOfMonth - 1];
	}

}
