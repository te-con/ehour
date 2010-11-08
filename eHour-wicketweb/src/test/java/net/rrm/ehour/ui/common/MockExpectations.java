/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.notNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.session.EhourWebSession;
/**
 * Mock expectations
 **/

public class MockExpectations
{
	private MockExpectations()
	{
		
	}
	
	/**
	 * Setup expectancy for nav. calendar
	 * @param timesheetService
	 * @param webApp
	 */
	@SuppressWarnings("deprecation")
	public static void navCalendar(TimesheetService timesheetService, TestEhourWebApplication webApp)
	{
		Calendar requestedMonth = new GregorianCalendar(2007, 12 - 1, 10);
		EhourWebSession session = webApp.getSession();
		session.setNavCalendar(requestedMonth);
		
		List<BookedDay> days = new ArrayList<BookedDay>();
		BookedDay day = new BookedDay();
		day.setDate(new Date(2007 - 1900, 12 - 1, 15));
		day.setHours(8);
		days.add(day);
		
		expect(timesheetService.getBookedDaysMonthOverview((Integer)notNull(), (Calendar)notNull()))
				.andReturn(days);		
	}
}
