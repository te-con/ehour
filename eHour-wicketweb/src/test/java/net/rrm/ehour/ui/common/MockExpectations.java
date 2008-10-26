/**
 * Created on Oct 26, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
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
import net.rrm.ehour.ui.session.EhourWebSession;
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
