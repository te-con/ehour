package net.rrm.ehour.ui.common.panel.calendar;

import net.rrm.ehour.timesheet.service.TimesheetService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CalendarWeekFactoryTest {

    @Mock
    private TimesheetService timesheetService;

    private CalendarWeekFactory factory;

    @Before
    public void init_subject() {
        MockitoAnnotations.initMocks(this);

        factory = new CalendarWeekFactory(timesheetService);
    }

    @Test
    public void should_mark_first_and_last_rows() {
        List<CalendarWeek> weeks = factory.createWeeks(1, 1, GregorianCalendar.getInstance());

        assertEquals(ElementLocation.FIRST, weeks.get(0).getLocation());
        assertEquals(ElementLocation.MIDDLE, weeks.get(1).getLocation());
        assertEquals(ElementLocation.LAST, weeks.get(weeks.size() - 1).getLocation());
    }
}
