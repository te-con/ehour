/**
 * Created on Nov 12, 2006
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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;

/**
 * TODO 
 **/

public class CalendarUtilTest extends TestCase
{
	public void testGetMonthNavCalendar() throws Exception
	{
		TimesheetService timesheetService;
		CalendarUtil	calendarUtil;
		List			results = new ArrayList();
		BookedDay		dayA, dayB;
		boolean[]		monthOverview;
		Calendar		cal;
		
		dayA = new BookedDay(new Date(2006, 11 - 1, 5), new Integer(8));
		dayB = new BookedDay(new Date(2006, 11 - 1, 30), new Integer(8));
		
		results.add(dayA);
		results.add(dayB);
		
		timesheetService = createMock(TimesheetService.class);
		
		calendarUtil = new CalendarUtil();
		calendarUtil.setTimesheetService((TimesheetService) timesheetService);
		
		cal = new GregorianCalendar(2006, 11 - 1, 12);

		expect(timesheetService.getBookedDaysMonthOverview(1, cal))
				.andReturn(results);
		
		replay(timesheetService);
		
		
		monthOverview = calendarUtil.getMonthNavCalendar(new Integer(1), cal);
		
		verify(timesheetService);
		
		assertTrue(monthOverview[4]);
		assertTrue(monthOverview[29]);
		
	}
}
