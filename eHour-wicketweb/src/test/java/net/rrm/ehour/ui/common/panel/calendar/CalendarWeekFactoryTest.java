package net.rrm.ehour.ui.common.panel.calendar;

import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class CalendarWeekFactoryTest {

    @Mock
    private IOverviewTimesheet overviewTimesheet;

    private CalendarWeekFactory factory;

    @Before
    public void init_subject() {
        MockitoAnnotations.initMocks(this);

        factory = new CalendarWeekFactory(overviewTimesheet);
    }

    @Test
    public void should_mark_first_and_last_rows() {
        List<CalendarWeek> weeks = factory.createWeeks(Calendar.SUNDAY, 1, GregorianCalendar.getInstance());

        assertEquals(ElementLocation.FIRST, weeks.get(0).getLocation());
        assertEquals(ElementLocation.MIDDLE, weeks.get(1).getLocation());
        assertEquals(ElementLocation.LAST, weeks.get(weeks.size() - 1).getLocation());
    }

    @Test
    public void should_create_weeks_for_january_2012() {
        List<CalendarWeek> weeks = factory.createWeeks(Calendar.SUNDAY, 1, new GregorianCalendar(2012, Calendar.JANUARY, 1));

        CalendarWeek calendarWeek = weeks.get(0);
        assertEquals(1, calendarWeek.getWeek());
        assertEquals(1, calendarWeek.getWeekStart().get(Calendar.DAY_OF_MONTH));
        assertEquals(5, weeks.size());

    }

    @Test
    public void should_create_weeks_for_february_2013() {
        GregorianCalendar requestedMonth = new GregorianCalendar(2013, Calendar.FEBRUARY, 1);
        LocalDate bookedDay = new LocalDate(2013, DateTimeConstants.FEBRUARY, 3);
        when(overviewTimesheet.getBookedDaysMonthOverview(1, requestedMonth)).thenReturn(Arrays.asList(bookedDay));

        List<CalendarWeek> weeks = factory.createWeeks(Calendar.MONDAY, 1, requestedMonth);

        CalendarWeek calendarWeek = weeks.get(0);
        assertEquals(5, calendarWeek.getWeek());
        assertEquals(28, calendarWeek.getWeekStart().get(Calendar.DAY_OF_MONTH));
        assertEquals(5, weeks.size());

        assertEquals(3, calendarWeek.getDay(Calendar.SUNDAY).getMonthDay());
        assertTrue(calendarWeek.getDay(Calendar.SUNDAY).isBooked());
    }

}
