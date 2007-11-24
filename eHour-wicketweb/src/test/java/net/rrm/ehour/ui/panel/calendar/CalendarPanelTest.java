/**
 * 
 */
package net.rrm.ehour.ui.panel.calendar;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.BaseUITest;
import net.rrm.ehour.user.domain.User;

import org.junit.Before;
import org.junit.Test;

/**
 * @author thies
 *
 */
public class CalendarPanelTest extends BaseUITest
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
	 * Test method for {@link net.rrm.ehour.ui.panel.calendar.CalendarPanel#CalendarPanel(java.lang.String, net.rrm.ehour.user.domain.User)}.
	 */
	@Test
	public void testCalendarPanelStringUser()
	{
		Calendar requestedMonth = GregorianCalendar.getInstance();
		
		expect(timesheetService.getBookedDaysMonthOverview(1, requestedMonth))
				.andReturn(null);					

		
		CalendarPanel panel = new CalendarPanel("id", new User());

		replay(timesheetService);

	}

	/**
	 * Test method for {@link net.rrm.ehour.ui.panel.calendar.CalendarPanel#CalendarPanel(java.lang.String, net.rrm.ehour.user.domain.User, boolean)}.
	 */
	@Test
	public void testCalendarPanelStringUserBoolean()
	{
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link net.rrm.ehour.ui.panel.calendar.CalendarPanel#refreshCalendar(org.apache.wicket.ajax.AjaxRequestTarget)}.
	 */
	@Test
	public void testRefreshCalendar()
	{
		fail("Not yet implemented");
	}

}
