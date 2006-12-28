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
	private String HTML_NEW_ROW = "<TR>";
	private String HTML_FIRST_CELL_FIRST_ROW = "<td style='border-left-width: 1px' class='overview_notThisMonthFirst'>";
	private String HTML_FIRST_CELL = "<td style='border-left-width: 1px' class='overview_notThisMonth'>";

	private	Map<Integer, List<TimesheetEntry>>	timesheetEntries;
	private Logger	logger = Logger.getLogger(OverviewCalendarTag.class);
	
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

	    logger.debug("Creating cal");
	    calendar.add(Calendar.DATE, -1 * currentColumn);
	    
	    while ((calendar.get(Calendar.YEAR) == year) && (calendar.get(Calendar.MONTH) <= month) ||
	            calendar.get(Calendar.YEAR) < year)
	    {
	    	// row with day numbers
	    	sb.append(HTML_NEW_ROW);
	    	addWeekCell(sb, calendar);
	    	addDayNumberRow(sb, calendar, month, row);
	    	sb.append(HTML_ROW_CLOSE);
	    	
	    	// row with values
	    	sb.append(HTML_NEW_ROW);
	    	addDayValueRow(sb, calendar, month, monthEntries);
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
								Map<Integer, List<TimesheetEntry>> monthEntries)
	{
		int 					currentColumn;
		List<TimesheetEntry>	timesheetEntries;
		// @todo use a localized version
		DecimalFormat			df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		for (currentColumn = 0;
			 currentColumn < 7;
			 currentColumn++,
			 	calendar.add(Calendar.DATE, 1))
		{
	        if (currentColumn == 0)
	        {
	        	sb.append("<td style='border-left-width: 1px' class='overview_data'>");
	        }
	        else
	        {
	        	sb.append("<td class='overview_data' valign='top'>");
	        }

	        if (calendar.get(Calendar.MONTH) == thisMonth)
	        {
	        	timesheetEntries = monthEntries.get(calendar.get(Calendar.DAY_OF_MONTH));

	            if (timesheetEntries != null
	            		&& timesheetEntries.size() > 0)
	            {
	            	sb.append("<table class='overview_daydata' width='100%' cellpadding='0' cellspacing='0'>");
	            	
	            	for (TimesheetEntry entry : timesheetEntries)
					{
	            		sb.append("<tr id='"
	            					+ entry.getEntryId().getProjectAssignment().getProject().getProjectCode()
	            					+ "'>");
	            		
	            		sb.append("<td title='"
	            					+ entry.getEntryId().getProjectAssignment().getProject().getFullname()
	            					+ "'>");
	            		
	            		sb.append(HTML_NBSP);
	            		
	            		sb.append(entry.getEntryId().getProjectAssignment().getProject().getProjectCode() 
	            					+ ": "
	            					+ HTML_CELL_CLOSE);

	            		sb.append("<td align='right'>");
	            		
	            		sb.append(HTML_NBSP);
	            		
	            		// @todo make ML
	            		sb.append(df.format(entry.getHours()) + "u");
	            		sb.append(HTML_NBSP);
	            		sb.append(HTML_CELL_CLOSE);
	            		sb.append(HTML_ROW_CLOSE);
					}
            		sb.append("</table>");
	            }
	            else
	            {
	            	sb.append(HTML_NBSP);
	            }
	        }
	        else
	        {
	        	sb.append(HTML_NBSP);

	        }

	        sb.append(HTML_CELL_CLOSE);
	    }
	}
	
	/**
	 * Add row with day numbers
	 * @param sb
	 * @param calendar
	 * @param thisMonth
	 * @param row
	 */
	private void addDayNumberRow(StringBuffer sb, Calendar calendar, int thisMonth, int row)
	{
		for (int currentColumn = 0;
			 currentColumn < 7;
			 currentColumn++)
	    {
			// @todo sun & sat are styled as out of scope, make it configurable
	        if (calendar.get(Calendar.MONTH) != thisMonth
	        		|| currentColumn == 0
	        		|| currentColumn == 6)
	        {
            	// sunday needs a border-left
	        	if (currentColumn == 0)
	            {
	        		// and the first row an upper border
	        		// @todo fix this in css
	                if (row == 0)
	                {
	                    sb.append("<td style='border-left-width: 1px' class='overview_notThisMonthFirst'>");
	                }
	                else
	                {
	                	sb.append("<td style='border-left-width: 1px' class='overview_notThisMonth'>");
	                }
	            }
	            else
	            {
	                if (row == 0)
	                {
	                	sb.append("<td class='overview_notThisMonthFirst'>");
	                }
	                else
	                {
	                	sb.append("<td class='overview_notThisMonth'>");
	                }
	            }

	        	sb.append(calendar.get(Calendar.DAY_OF_MONTH) + "</td>");
	        }
	        else
	        {
	            if (row == 0)
	            {
	            	sb.append("<td class='overview_headerFirst'>" + calendar.get(Calendar.DAY_OF_MONTH) + "</td>");
	            }
	            else
	            {
	            	sb.append("<th>" + calendar.get(Calendar.DAY_OF_MONTH) + "</th>");
	            }
	        }

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
		sb.append("<td class='overview_unbordered' rowspan=2 valign='bottom'>"
					+ HTML_NBSP
					+ HTML_NBSP 
				    + "Week "
				    + calendar.get(Calendar.WEEK_OF_YEAR)
				    + HTML_NBSP
				    + HTML_CELL_CLOSE);
	}
	
	/**
	 * Get HTML cell tag
	 * @param currentColumn
	 * @param date
	 * @param row
	 * @return
	 */
	private String getHtmlCell(int currentColumn, Calendar date, int row)
	{
		String	cellTag = null;
		
		if (currentColumn == 0)
		{
			// indeed, I'm not believing anymore that StringBuffer performs better than concatenating seperate
			// string objects
			cellTag = HTML_FIRST_CELL;
			cellTag += "&nbsp;&nbsp;Week " + date.get(Calendar.WEEK_OF_YEAR) + "&nbsp;</td>";
			
			if (row == 0)
			{
				cellTag += HTML_FIRST_CELL_FIRST_ROW;
			}
			else
			{
				cellTag += HTML_FIRST_CELL;
			}
		}
		else
		{
			if (row == 0)
			{
				
			}
		}
		
		return cellTag;
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
