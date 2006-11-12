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
	 * Converts a date to a range covering that month
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
}
