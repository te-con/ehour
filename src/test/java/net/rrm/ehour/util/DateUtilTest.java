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
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import net.rrm.ehour.data.DateRange;

/**
 * TODO 
 **/

public class DateUtilTest extends TestCase
{

	public void testCalendarToMonthRange()
	{
		Calendar cal = new GregorianCalendar(2006, 11 -1 , 4);
		
		DateRange dr = DateUtil.calendarToMonthRange(cal);
		
		assertEquals(106, dr.getDateStart().getYear());
		assertEquals(10, dr.getDateStart().getMonth());
		assertEquals(1, dr.getDateStart().getDate());

		assertEquals(106, dr.getDateStart().getYear());
		assertEquals(10, dr.getDateEnd().getMonth());
		assertEquals(30, dr.getDateEnd().getDate());

		assertEquals(4, cal.get(Calendar.DATE));
	}
	
	public void testGetDaysInMonth()
	{
		Calendar cal = new GregorianCalendar(2006, 11 -1 , 4);
		assertEquals(30, DateUtil.getDaysInMonth(cal));
		
		cal = new GregorianCalendar(2007, 2 - 1, 5);
		assertEquals(28, DateUtil.getDaysInMonth(cal));
	}

}
