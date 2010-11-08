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

package net.rrm.ehour.ui.common.panel.calendar;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventHook;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;

/**
 * @author thies
 *
 */
@SuppressWarnings("deprecation")
public class CalendarPanelTest extends AbstractSpringWebAppTester
{
	private TimesheetService	timesheetService;
	
	@Before
	public void before() throws Exception
	{
		timesheetService = createMock(TimesheetService.class);
		getMockContext().putBean("timesheetService", timesheetService);

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void reproduceIssueEHO131()
	{
		AjaxEventHook hook = new AjaxEventHook();
		EventPublisher.listenerHook = hook;
		
		Calendar requestedMonth = new ComparableGreggieCalendar(2009, 1 - 1, 2);
		EhourWebSession session = getWebApp().getSession();

		session.setNavCalendar(requestedMonth);
		
		List<BookedDay> days = generateBookDays();

		expect(timesheetService.getBookedDaysMonthOverview(1, requestedMonth))
				.andReturn(days);					

		replay(timesheetService);
		
		startPanel();
		
		tester.executeAjaxEvent("panel:calendarFrame:weeks:0", "onclick");
		
		assertEquals(1, hook.events.size());
		
		for (AjaxEvent event : hook.events)
		{
			assertEquals(CalendarAjaxEventType.WEEK_CLICK, event.getEventType());
			
			PayloadAjaxEvent<Calendar> pae = (PayloadAjaxEvent<Calendar>)event;
			
			assertEquals(12 -1, pae.getPayload().get(Calendar.MONTH));
			assertEquals(2008, pae.getPayload().get(Calendar.YEAR));
			assertEquals(28, pae.getPayload().get(Calendar.DAY_OF_MONTH));
		}
		
		
		verify(timesheetService);
	}

	@Test
	public void shouldRender()
	{
		Calendar requestedMonth = new ComparableGreggieCalendar(2009, 10 - 1, 22);
		
		EhourWebSession session = getWebApp().getSession();
		session.setNavCalendar(requestedMonth);
		
		List<BookedDay> days = generateBookDays();

		expect(timesheetService.getBookedDaysMonthOverview(1, requestedMonth))
				.andReturn(days);					

		replay(timesheetService);
		
		startPanel();
		
		verify(timesheetService);
	}

	
	@Test
	public void shouldMoveToNextMonth()
	{
		Calendar requestedMonth = new ComparableGreggieCalendar(2009, 10 - 1, 22);
		Calendar nextMonth = new ComparableGreggieCalendar(2009, 11 - 1, 1);
		
		EhourWebSession session = getWebApp().getSession();
		session.setNavCalendar(requestedMonth);
		
		List<BookedDay> days = generateBookDays();

		expect(timesheetService.getBookedDaysMonthOverview(1, requestedMonth))
				.andReturn(days);					

		expect(timesheetService.getBookedDaysMonthOverview(1, nextMonth))
				.andReturn(days);					

		replay(timesheetService);
		
		startPanel();

		tester.executeAjaxEvent("panel:calendarFrame:nextMonthLink", "onclick");
		
		verify(timesheetService);
	}
	
	private List<BookedDay> generateBookDays()
	{
		List<BookedDay> days = new ArrayList<BookedDay>();
		
		BookedDay day = new BookedDay();
		day.setDate(new Date(2007 - 1900, 12 - 1, 15));
		day.setHours(8);
		days.add(day);
		return days;
	}

	@SuppressWarnings("serial")
	private void startPanel()
	{
		tester.startPanel(new TestPanelSource()
		{
			
			public Panel getTestPanel(String panelId)
			{
				return new CalendarPanel(panelId, new User(1));
			}
		});
	}
	
	@SuppressWarnings("serial")
	class ComparableGreggieCalendar extends GregorianCalendar
	{
		public ComparableGreggieCalendar(int year, int month, int day)
		{
			super(year, month, day);
		}

		@Override
		public boolean equals(Object obj)
		{
			boolean equals = super.equals(obj);
			
			if (equals)
			{
				return equals;
			}
			else
			{
				GregorianCalendar cal = (GregorianCalendar)obj;
				
				equals = true;
				
				equals &= compare(cal, Calendar.DAY_OF_MONTH);
				equals &= compare(cal, Calendar.MONTH);
				equals &= compare(cal, Calendar.YEAR);
				
				return equals;
			}
			
		}
		
		private boolean compare(Calendar other,int property)
		{
			return this.get(property) == other.get(property);
		}
	}
}
