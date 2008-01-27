/**
 * 
 */
package net.rrm.ehour.ui.panel.calendar;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.junit.Before;
import org.junit.Test;

/**
 * @author thies
 *
 */
public class CalendarPanelTest extends BaseUIWicketTester
{
	private TimesheetService	timesheetService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		
		timesheetService = createMock(TimesheetService.class);
		mockContext.putBean("timesheetService", timesheetService);

	}

	/**
	 * Test method for {@link net.rrm.ehour.ui.panel.calendar.CalendarPanel#CalendarPanel(java.lang.String, net.rrm.ehour.domain.User)}.
	 */
	@Test
	public void testCalendarPanelStringUser()
	{
		Calendar requestedMonth = new GregorianCalendar(2007, 12 - 1, 10);
		List<BookedDay> days = new ArrayList<BookedDay>();
		BookedDay day = new BookedDay();
		day.setDate(new Date(2007 - 1900, 12 - 1, 15));
		day.setHours(8);
		days.add(day);
		
		expect(timesheetService.getBookedDaysMonthOverview(1, requestedMonth))
				.andReturn(days);					

		replay(timesheetService);

		EhourWebSession session = webapp.getSession();
		session.setNavCalendar(requestedMonth);
		
		CalendarPanel panel = new CalendarPanel("id", new User(1));
	}
//
//	/**
//	 * Test method for {@link net.rrm.ehour.ui.panel.calendar.CalendarPanel#CalendarPanel(java.lang.String, net.rrm.ehour.user.domain.User, boolean)}.
//	 */
//	@Test
//	public void testCalendarPanelStringUserBoolean()
//	{
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link net.rrm.ehour.ui.panel.calendar.CalendarPanel#refreshCalendar(org.apache.wicket.ajax.AjaxRequestTarget)}.
//	 */
//	@Test
//	public void testRefreshCalendar()
//	{
//		fail("Not yet implemented");
//	}

}
