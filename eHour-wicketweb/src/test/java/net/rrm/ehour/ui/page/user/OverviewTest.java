/**
 * Created on Sep 7, 2007
 * Created by Thies Edeling
 * Copyright (C) 2007 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.page.user;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.notNull;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.session.EhourWebSession;


/**
 * Overview page test
 **/

public class OverviewTest extends BaseUIWicketTester
{
	public void testOverviewPageRender()
	{
		TimesheetService timesheetService = createMock(TimesheetService.class);
		mockContext.putBean("timesheetService", timesheetService);
		
		Calendar requestedMonth = new GregorianCalendar(2007, 12 - 1, 10);
		
		List<BookedDay> days = new ArrayList<BookedDay>();
		BookedDay day = new BookedDay();
		day.setDate(new Date(2007 - 1900, 12 - 1, 15));
		day.setHours(8);
		days.add(day);
		
		expect(timesheetService.getBookedDaysMonthOverview((Integer)notNull(), (Calendar)notNull()))
				.andReturn(days);					

		TimesheetOverview overview = new TimesheetOverview();
		
		expect(timesheetService.getTimesheetOverview((User)notNull(), (Calendar)notNull()))
				.andReturn(overview);					
		
		
		EhourWebSession session = webapp.getSession();
		session.setNavCalendar(requestedMonth);
		
		replay(timesheetService);
		
		tester.startPage(Overview.class);
		tester.assertRenderedPage(Overview.class);
		tester.assertNoErrorMessage();
		
//		verify(timesheetService);
	}
}
