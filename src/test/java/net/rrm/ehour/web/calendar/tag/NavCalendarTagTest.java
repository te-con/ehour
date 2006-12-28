/**
 * Created on Dec 17, 2006
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

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.rrm.ehour.web.calendar.tag.NavCalendarTag;

import junit.framework.TestCase;

/**
 * TODO 
 **/

public class NavCalendarTagTest extends TestCase
{
	private NavCalendarTag	tag;
	private	StringBuffer	sb;
	private	Calendar		cal31;
	private	Calendar		cal29;
	private	Calendar		cal30;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		tag = new NavCalendarTag();
		sb = new StringBuffer();

		// october 2006, 1 oct is a sunday, 31 days length
		cal31 = new GregorianCalendar(2006, 9, 1);
		cal31.setFirstDayOfWeek(Calendar.SUNDAY);
		
		// feb 2008, 1 feb is a friday, 29 days length
		cal29 = new GregorianCalendar(2008, 1, 1);
		cal29.setFirstDayOfWeek(Calendar.SUNDAY);
		
		// nov 2006, 1 jan is monday, 30 days length
		cal30 = new GregorianCalendar(2006, 10, 1);
		cal30.setFirstDayOfWeek(Calendar.SUNDAY);
	
	}

//
//	/**
//	 * Test method for {@link net.rrm.ehour.web.calendar.NavCalendarTag#getBookedDays()}.
//	 */
//	public void testGetBookedDays()
//	{
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link net.rrm.ehour.web.calendar.NavCalendarTag#setBookedDays(boolean[])}.
//	 */
//	public void testSetBookedDays()
//	{
//		fail("Not yet implemented");
//	}
//	
//	public void testAppendNextMonthDays()
//	{
////		tag.appendNextMonthDays(currentColumn, out)
//	}
	
	public void testPrependPreviousMonthDays()
	{
		assertEquals(0, tag.prependPreviousMonthDays(cal31, sb));
		assertEquals(5, tag.prependPreviousMonthDays(cal29, sb));
		assertEquals(3, tag.prependPreviousMonthDays(cal30, sb));
	}
	

}
