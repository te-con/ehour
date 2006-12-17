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

package net.rrm.ehour.timesheet.dao;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.timesheet.dto.BookedDay;

public class TimesheetDAOTest  extends BaseDAOTest 
{
	private	TimesheetDAO	dao;

	
	public void setTimesheetDAO(TimesheetDAO dao)
	{
		this.dao = dao;
	}
	
	/**
	 * 
	 *
	 */
	public void testGetTimesheetEntriesInRange()
	{
		Calendar 	dateStart = new GregorianCalendar(2006, 10 - 1, 1);
		Calendar 	dateEnd = new GregorianCalendar(2006, 11 - 1, 1);
		DateRange	dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());
		List		results;
		
		results = dao.getTimesheetEntriesInRange(new Integer(1), dateRange);
		
		assertEquals(7, results.size());
	}
	
	/**
	 * 
	 *
	 */
	public void testGetBookedHoursperDayInRange()
	{
		Calendar 	dateStart = new GregorianCalendar(2006, 10 - 1, 1);
		Calendar 	dateEnd = new GregorianCalendar(2006, 11 - 1, 1);
		DateRange	dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());
		List		results;
		BookedDay	bookedDay;
		
		results = dao.getBookedHoursperDayInRange(new Integer(1), dateRange);
		
		assertEquals(5, results.size());
		
		bookedDay = (BookedDay)results.get(3);
		assertEquals(6.5, bookedDay.getHours().doubleValue(), 0.01);

		bookedDay = (BookedDay)results.get(2);
		assertEquals(5, bookedDay.getHours().doubleValue(), 0.01);
	}
	
	public void testGetTimesheetEntryCountForAssignment()
	{
		int count = dao.getTimesheetEntryCountForAssignment(1);
		
		assertEquals(5, count);
	}
}
