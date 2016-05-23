package net.rrm.ehour.persistence.timesheet.dao;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.timesheet.dto.BookedDay;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 7:36:19 PM
 */
@SuppressWarnings("deprecation")
public class TimesheetDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    public TimesheetDaoHibernateImplTest() {
        super("dataset-timesheet.xml");
    }

    @Test
    public void shouldGetTimesheetEntriesInRangeForUser() {
        Calendar dateStart = new GregorianCalendar(2006, Calendar.OCTOBER, 1);
        Calendar dateEnd = new GregorianCalendar(2006, Calendar.NOVEMBER, 1);
        DateRange dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());

        List<TimesheetEntry> results = timesheetDAO.getTimesheetEntriesInRange(1, dateRange);

        assertEquals(9, results.size());
    }

    @Test
    public void shouldGetTimesheetEntriesInRangeForAssignment() {
        Calendar dateStart = new GregorianCalendar(2006, Calendar.OCTOBER, 1);
        Calendar dateEnd = new GregorianCalendar(2006, Calendar.NOVEMBER, 1);
        DateRange dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());

        List<TimesheetEntry> results = timesheetDAO.getTimesheetEntriesInRange(new ProjectAssignment(2), dateRange);

        assertEquals(2, results.size());
    }

    @Test
    public void shouldGetTimesheetEntriesInRange() {
        Calendar dateStart = new GregorianCalendar(2006, Calendar.OCTOBER, 1);
        Calendar dateEnd = new GregorianCalendar(2006, Calendar.NOVEMBER, 1);
        DateRange dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());

        List<TimesheetEntry> results = timesheetDAO.getTimesheetEntriesInRange(dateRange);

        assertEquals(9, results.size());
    }

    @Test
    public void shouldGetBookedHoursperDayInRange() {
        Calendar dateStart = new GregorianCalendar(2006, Calendar.OCTOBER, 1);
        Calendar dateEnd = new GregorianCalendar(2006, Calendar.NOVEMBER, 1);
        DateRange dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());

        List<BookedDay> results = timesheetDAO.getBookedHoursperDayInRange(1, dateRange);

        assertEquals(6, results.size());

        assertEquals(6.5, results.get(3).getHours().floatValue(), 0.01);

        assertEquals(-1, results.get(2).getHours().floatValue(), 0.01);
    }

    @Test
    public void shouldGetTimesheetEntriesBefore() {
        List<TimesheetEntry> res = timesheetDAO.getTimesheetEntriesBefore(new ProjectAssignment(1), new Date(2006 - 1900, Calendar.OCTOBER, 3));
        assertEquals(1, res.size());
    }

    @Test
    public void shouldGetTimesheetEntriesAfter() {
        List<TimesheetEntry> res = timesheetDAO.getTimesheetEntriesAfter(new ProjectAssignment(1), new Date(2006 - 1900, Calendar.OCTOBER, 4));

        assertEquals(3, res.size());
    }

    @Test
    public void shouldGetLatestTimesheetEntryForAssignment() {
        TimesheetEntry entry = timesheetDAO.getLatestTimesheetEntryForAssignment(1);
        assertEquals(9.2f, entry.getHours(), 0.01f);
    }

    @Test
    public void shouldDeleteTimesheetEntries() {
        List<Integer> ids = new ArrayList<>(Arrays.asList(5));

        int deleted = timesheetDAO.deleteTimesheetEntries(ids);

        assertEquals(2, deleted);
    }

    @Autowired
    private TimesheetDao timesheetDAO;
}
