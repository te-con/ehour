/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.rrm.ehour.data.DateRange;

/**
 * Various static methods for date manipulation
 **/

public class DateUtil
{
	/**
	 * Do not instantiate
	 *
	 */
	private DateUtil()
	{
	}
	
	/**
	 * Get days in month
	 * @param calendar
	 * @return
	 */
	public static int getDaysInMonth(Calendar calendar)
	{
		Calendar	cal;
		
		cal = (Calendar)calendar.clone();
		
		cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        
        return cal.get(Calendar.DATE);
		
	}
	
	/**
	 * Converts a calendar to a range covering that month
	 * @param date
	 * @return first Calendar object is start of the month, last Calendar object is end of the month 
	 */
	public static DateRange calendarToMonthRange(Calendar calendar)
	{
		DateRange	dateRange = new DateRange();
		Calendar	cal;
		
		cal = (Calendar)calendar.clone();
		
        cal.set(Calendar.DATE, 1);
        dateRange.setDateStart(cal.getTime());

        
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        dateRange.setDateEnd(cal.getTime());		
		
        return dateRange;
	}
	
	/**
	 * Check whether a date is within range
	 * @param the date
	 * @param the range the date must be in
	 * @return
	 */
	
	public static boolean isDateWithinRange(Date date, DateRange dateRange)
	{
		boolean	withinRange = false;
		
		withinRange =  !(dateRange.getDateStart().after(date) ||
						 dateRange.getDateEnd().before(date));
		
		return withinRange;
	}
	
	/**
	 * Check whether two dateranges overlap eachother
	 * @param rangeA
	 * @param rangeB
	 * @return
	 */
	public static boolean isDateRangeOverlaps(DateRange rangeA, DateRange rangeB)
	{
		boolean	overlaps = false;
		
		overlaps = rangeA.getDateEnd().after(rangeB.getDateStart()) &&
				   rangeA.getDateStart().before(rangeB.getDateEnd());
		
		return overlaps;
	}
	
	/**
	 * Get a date range covering the week the supplied calendar is in
	 * @param calendar
	 * @return
	 */
	public static DateRange getDateRangeForWeek(Calendar calendar)
	{
		DateRange	weekRange = new DateRange();
		
		Calendar	calClone = (Calendar)calendar.clone();
		
		// @todo assuming the week starts on sunday. configurable?
		calClone.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - calClone.get(Calendar.DAY_OF_WEEK));
		weekRange.setDateStart(calClone.getTime());
		
		calClone.add(Calendar.DAY_OF_MONTH, +6);
		weekRange.setDateEnd(calClone.getTime());
		
		return weekRange;
	}
	
	/**
	 * Set the time of a date to 00:00.00 (up to the ms)
	 * @param date
	 * @return
	 */
	
	public static Date nullifyTime(Date date)
	{
		Calendar	cal = new GregorianCalendar();
		cal.setTime(date);
		
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}
}
