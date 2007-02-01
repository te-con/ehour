/**
 * Created on Dec 27, 2006
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
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;

import net.rrm.ehour.timesheet.domain.TimesheetEntry;

/**
 * TODO 
 **/

public class OverviewCalendarTag extends CalendarTag
{
	private static final long serialVersionUID = 3917279643583195331L;
	// HTML constants
	private String HTML_NEW_ROW_DATES = "<tr class='dateRow'>";
	private String HTML_NEW_ROW_HOURS = "<tr class='hourRow'>";

	private	Map<Integer, List<TimesheetEntry>>	timesheetEntries;
	private transient Logger	logger = Logger.getLogger(OverviewCalendarTag.class);
	
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
		
		sb = writeCalendar(calendar, timesheetEntries);
		
		try
		{
			out.write(sb.toString());
		} catch (IOException e)
		{
			logger.error("Exception while creating calendar for month overview " + calendar.toString());
	        StringWriter sw = new StringWriter();
	        e.printStackTrace(new PrintWriter(sw));
	        logger.error(e.toString());
			
			// big chance this fails as well but at least we can try
			try
			{
				out.write("<tr><td colspan=7>Something went wrong..</td></tr>");
			} catch (IOException te)
			{
			}
		}		
		
		return SKIP_BODY;
	}
	
	/**
	 * Write the calendar
	 * @param calendar
	 * @return
	 */
	private StringBuffer writeCalendar(Calendar calendar, Map<Integer, List<TimesheetEntry>> monthEntries)
	{
		StringBuffer sb = new StringBuffer();
		
	    int month = calendar.get(Calendar.MONTH);
	    int year = calendar.get(Calendar.YEAR);
	    int currentColumn = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	    int row = 0;

	    logger.debug("Creating month overview calendar for " + month + "/" + year);
	    calendar.add(Calendar.DATE, -1 * currentColumn);
	    
	    while ((calendar.get(Calendar.YEAR) == year) &&
	    		(calendar.get(Calendar.MONTH) <= month) || calendar.get(Calendar.YEAR) < year)
	    {
	    	// row with day numbers
	    	sb.append(HTML_NEW_ROW_DATES);
	    	addWeekCell(sb, null);
	    	addDayNumberRow(sb, calendar, month, year);
	    	sb.append(HTML_ROW_CLOSE);
	    	
	    	// row with values
	    	sb.append(HTML_NEW_ROW_HOURS);
	    	addWeekCell(sb, calendar);
	    	addDayValueRow(sb, calendar, month, year, monthEntries);
	    	sb.append(HTML_ROW_CLOSE);
	    	row++;
	    }
	    
	    return sb;
	}
	
	/**
	 * Add the values row, booked hours per project per date
	 * @param sb
	 * @param calendar
	 * @param thisMonth
	 * @param timesheetEntries
	 */
	private void addDayValueRow(StringBuffer sb,
								Calendar calendar,
								int thisMonth,
								int thisYear,
								Map<Integer, List<TimesheetEntry>> monthEntries)
	{
		int 					currentColumn;
		List<TimesheetEntry>	timesheetEntries;
		// @todo use a localized version
		DecimalFormat			df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		sb.append("<!-- hours -->");
		
		for (currentColumn = 0;
			 currentColumn < 7;
			 currentColumn++,
			 	calendar.add(Calendar.DATE, 1))
		{
	        if (calendar.get(Calendar.MONTH) == thisMonth)
	        {
				switch (currentColumn)
				{
					case 0:
						sb.append("<td class='sunday'>");
						break;
					case 6:
						sb.append("<td class='saturday'>");
						break;
					default:
						sb.append(HTML_CELL_OPEN);
					break;
				}

	        	timesheetEntries = monthEntries.get(calendar.get(Calendar.DAY_OF_MONTH));

	            if (timesheetEntries != null
	            		&& timesheetEntries.size() > 0)
	            {
	            	sb.append("<div class='bookedHours'>");
	            	
	            	for (TimesheetEntry entry : timesheetEntries)
					{
	            		// @todo cut-off at fixed length with <br>'s to prevent layout mayhem
//	            		sb.append("<tr id='"
//	            					+ entry.getEntryId().getProjectAssignment().getProject().getProjectCode()
//	            					+ "'>");
//	            		
//	            		sb.append("<td title='"
//	            					+ entry.getEntryId().getProjectAssignment().getProject().getFullname()
//	            					+ "'>");
	            		
	            		if (entry.getHours().intValue() != 0)
	            		{
		            		sb.append(entry.getEntryId().getProjectAssignment().getProject().getName() 
		            					+ ":");
	
		            		sb.append("<div class='bookedHourValue'>");
		            		
		            		// @todo make ML
		            		sb.append(df.format(entry.getHours()) + "hr");
		            		sb.append(HTML_DIV_CLOSE);
		            		sb.append(HTML_BR);
	            		}
					}
            		sb.append(HTML_DIV_CLOSE);
	            }
	            else
	            {
	            	sb.append(HTML_NBSP);
	            }
	        }
	        else
	        {
            	if (monthIsBeforeCurrent(calendar, thisMonth, thisYear))
            	{
            		sb.append("<td class='noMonthBefore'>" );
            	}
            	else
            	{
            		sb.append("<td class='noMonthAfter'>" );
            	}
            	
            	sb.append(HTML_NBSP + HTML_CELL_CLOSE);
	        }
	    }
	}
	
	/**
	 * Add row with day numbers
	 * @param sb
	 * @param calendar
	 * @param thisMonth
	 */
	private void addDayNumberRow(StringBuffer sb, Calendar calendar, int thisMonth, int thisYear)
	{
		for (int currentColumn = 0;
			 currentColumn < 7;
			 currentColumn++)
	    {
			if (calendar.get(Calendar.MONTH) == thisMonth)
			{
				if (currentColumn == 6)
				{
					sb.append("<td class='lastChild'>");
				}
	            else
	            {
	            	sb.append(HTML_CELL_OPEN);
	            }
	
				if (calendar.get(Calendar.MONTH) == thisMonth)
				{
					sb.append(calendar.get(Calendar.DAY_OF_MONTH));
				}
				else
				{
					sb.append(HTML_NBSP);
				}
			}
			else
			{
				if (monthIsBeforeCurrent(calendar, thisMonth, thisYear))
				{
					sb.append(HTML_CELL_OPEN);
				}
				else
				{
					sb.append("<td class='noMonth'>");
				}
				
				sb.append(HTML_NBSP);
			}
        	
        	sb.append(HTML_CELL_CLOSE);

	        calendar.add(Calendar.DATE, 1);
	    }
		
		// reset the abused calendar
		calendar.add(Calendar.DATE, -7);
	}
	
	/**
	 * Add week cell
	 * @param sb
	 * @param calendar
	 */
	private void addWeekCell(StringBuffer sb, Calendar calendar)
	{
		// @todo use i18n
		sb.append("<td class='weekNumber'>");
		
		if (calendar == null)
		{
			sb.append(HTML_NBSP);
		}
		else
		{
			sb.append("Week " + calendar.get(Calendar.WEEK_OF_YEAR));
		}
		
		sb.append(HTML_CELL_CLOSE);
	}
	
	/**
	 * 
	 * @param calendar
	 * @param thisMonth
	 * @param thisYear
	 * @return
	 */
	private boolean monthIsBeforeCurrent(Calendar calendar, int thisMonth, int thisYear)
	{
		int	year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		
		return month < thisMonth && year == thisYear ||
			   year < thisYear;
	}

	/**
	 * @return the timesheetEntries
	 */
	public Map<Integer, List<TimesheetEntry>> getTimesheetEntries()
	{
		return timesheetEntries;
	}

	/**
	 * @param timesheetEntries the timesheetEntries to set
	 */
	public void setTimesheetEntries(Map<Integer, List<TimesheetEntry>> monthEntries)
	{
		this.timesheetEntries = monthEntries;
	}

}
