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

import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventHook;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author thies
 */
@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class CalendarPanelTest extends BaseSpringWebAppTester {
    @Mock
    private IOverviewTimesheet overviewTimesheet;

    @Before
    public void before() throws Exception {
        getMockContext().putBean(overviewTimesheet);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void reproduceIssueEHO131() {
        AjaxEventHook hook = new AjaxEventHook();
        EventPublisher.listenerHook = hook;

        Calendar requestedMonth = new ComparableGregorianCalendar(2009, Calendar.JANUARY, 2);
        EhourWebSession session = getWebApp().getSession();
        requestedMonth.setFirstDayOfWeek(requestedMonth.getFirstDayOfWeek());

        session.setNavCalendar(requestedMonth);

        when(overviewTimesheet.getBookedDaysMonthOverview(1, requestedMonth)).thenReturn(generateBookDays());

        startPanel();

        tester.executeAjaxEvent("panel:calendarFrame:weeks:0", "onclick");

        assertEquals(1, hook.events.size());

        for (AjaxEvent event : hook.events) {
            assertEquals(CalendarAjaxEventType.WEEK_CLICK, event.getEventType());

            PayloadAjaxEvent<Calendar> pae = (PayloadAjaxEvent<Calendar>) event;

            assertEquals(12 - 1, pae.getPayload().get(Calendar.MONTH));
            assertEquals(2008, pae.getPayload().get(Calendar.YEAR));
            assertEquals(28, pae.getPayload().get(Calendar.DAY_OF_MONTH));
        }
    }

    @Test
    public void shouldRender() {
        Calendar requestedMonth = new ComparableGregorianCalendar(2009, 10 - 1, 22);

        EhourWebSession session = getWebApp().getSession();
        session.setNavCalendar(requestedMonth);

        when(overviewTimesheet.getBookedDaysMonthOverview(1, requestedMonth)).thenReturn(generateBookDays());

        startPanel();

        tester.assertNoErrorMessage();
        tester.assertNoInfoMessage();
    }

    @Test
    public void shouldMoveToNextMonth() {
        Calendar requestedMonth = new ComparableGregorianCalendar(2009, 10 - 1, 22);
        Calendar nextMonth = new ComparableGregorianCalendar(2009, 11 - 1, 1);

        EhourWebSession session = getWebApp().getSession();
        session.setNavCalendar(requestedMonth);

        List<LocalDate> bookedDays = generateBookDays();

        when(overviewTimesheet.getBookedDaysMonthOverview(1, requestedMonth))
                .thenReturn(bookedDays);

        when(overviewTimesheet.getBookedDaysMonthOverview(1, nextMonth))
                .thenReturn(bookedDays);

        startPanel();

        tester.executeAjaxEvent("panel:calendarFrame:nextMonthLink", "onclick");

        tester.assertNoErrorMessage();
        tester.assertNoInfoMessage();

        assertEquals(nextMonth, session.getNavCalendar());
    }

    private List<LocalDate> generateBookDays() {
        LocalDate bookedDay = new LocalDate(2007, DateTimeConstants.DECEMBER, 15);

        return Arrays.asList(bookedDay);
    }

    private void startPanel() {
        tester.startComponentInPage(new CalendarPanel("panel", new User(1)));
    }

    @SuppressWarnings("serial")
    class ComparableGregorianCalendar extends GregorianCalendar {
        public ComparableGregorianCalendar(int year, int month, int day) {
            super(year, month, day);
        }

        @Override
        public boolean equals(Object obj) {
            boolean equals = super.equals(obj);

            if (equals) {
                return true;
            } else {
                GregorianCalendar cal = (GregorianCalendar) obj;

                equals = compare(cal, Calendar.DAY_OF_MONTH);
                equals &= compare(cal, Calendar.MONTH);
                equals &= compare(cal, Calendar.YEAR);

                return equals;
            }

        }

        private boolean compare(Calendar other, int property) {
            return this.get(property) == other.get(property);
        }
    }
}
