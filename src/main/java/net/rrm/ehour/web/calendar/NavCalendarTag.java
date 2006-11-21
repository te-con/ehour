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

package net.rrm.ehour.web.calendar;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

/**
 * TODO 
 **/

public class NavCalendarTag extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2422922719761190399L;
	private Calendar 	calendar;
	private boolean[] bookedDays;

	// HTML constants
	private	final	String	HTML_FIRST_CELL = "<TD class=\"cps_dayData\" style=\"border-left: 0px\">";
	private	final	String	HTML_CELL = "<TD class=\"cps_dayData\">";
	private	final	String	HTML_FIRST_CELL_COMPLETE = "<TD class=\"cps_dayData\" style=\"border-left: 0px\">";
	private	final	String	HTML_CELL_COMPLETE = "<TD class=\"cps_dayData\">";
	private	final	String	HTML_CELL_NOT_COMPLETE = "<TD class=\"cps_dayNoData\">";	
	private	final	String	HTML_NEW_ROW = "<TR onmouseover=\"this.className='cps_tableTROn';\" " +
	   								 "onmouseout=\"this.className='cps_tableTROff';\"  onclick=\"\">";
	private	final	String	HTML_NBSP = "&nbsp;";
	private	final	String	HTML_CELL_CLOSE = "</TD>";
	private	final	String	HTML_ROW_CLOSE = "</TR>";
	
	private Logger	logger = Logger.getLogger(NavCalendarTag.class);
	
	/**
	 *
	 * @return int
	 * @throws JspException
	 */

	public int doStartTag() throws JspException
	{
		JspWriter 		out;
		int				currentColumn;
		StringBuffer	sb = new StringBuffer();

		out = pageContext.getOut();

		try
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Writing nav calendar for " + calendar.toString());
			}
			
			calendar.setFirstDayOfWeek(Calendar.SUNDAY);
			
			currentColumn = prependPreviousMonthDays(sb);
			currentColumn = writeCalendar(currentColumn, sb);
			appendNextMonthDays(currentColumn, sb);
			
			sb.append(HTML_ROW_CLOSE);
			
			out.write(sb.toString());
			
		} catch (Throwable t)
		{
			logger.error("Exception while creating nav calendar for month " + calendar.toString());
			logger.error(t);
			
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
				out.append(HTML_CELL_COMPLETE);
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
		int year = calendar.get(Calendar.YEAR);
		int days = 0;
		
		while (calendar.get(Calendar.MONTH) == month 
				&& days++ < 31)
		{
			// first determine if this calendar entry should be marked as completely booked or not
			if (currentColumn == 0)
			{
				// @todo row might not be closed by prependdays
				out.append(HTML_NEW_ROW);
				
				// sundays are always marked as complete
				out.append(HTML_FIRST_CELL_COMPLETE);
			}
			else if (currentColumn == 6 ||
					(bookedDays != null && bookedDays[calendar.get(Calendar.DAY_OF_MONTH)]))
			{
				out.append(HTML_CELL_COMPLETE);
			}
			else
			{
				out.append(HTML_CELL_NOT_COMPLETE);
			}
			
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
	private int prependPreviousMonthDays(StringBuffer out) 
	{
		int i;
		int	currentColumn;
		
		currentColumn = calendar.get(Calendar.DAY_OF_WEEK);

		logger.debug("Filling up first week to column " + currentColumn);
		
		if (currentColumn != Calendar.SUNDAY)
		{
			currentColumn--;
			out.append(HTML_NEW_ROW);
			out.append(HTML_FIRST_CELL);
			out.append(HTML_NBSP);
			out.append(HTML_CELL_CLOSE);

			for (i = 1; i < currentColumn; i++)
			{
				out.append(HTML_CELL);
				out.append(HTML_NBSP);
				out.append(HTML_CELL_CLOSE);
			}
		}
		
		return currentColumn;
	}
	
	/**
	 * 
	 * @return int
	 * @throws JspException
	 */
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}

	public boolean[] getBookedDays()
	{
		return bookedDays;
	}

	public void setBookedDays(boolean[] bookedDays)
	{
		this.bookedDays = bookedDays;
	}

	public Calendar getCalendar()
	{
		return calendar;
	}

	public void setCalendar(Calendar calendar)
	{
		this.calendar = calendar;
	}
}
