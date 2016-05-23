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

package net.rrm.ehour.timesheet.service;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetCommentDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.util.DateUtil;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scala.collection.immutable.Vector;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings({"deprecation"})
@RunWith(MockitoJUnitRunner.class)
public class TimesheetServiceImplTest {
    private TimesheetServiceImpl timesheetService;

    @Mock
    private TimesheetDao timesheetDAO;

    @Mock
    private TimesheetCommentDao timesheetCommentDAO;

    @Mock
    private EhourConfig config;

    @Mock
    private AggregateReportService aggregateReportService;

    @Mock
    private ProjectAssignmentService projectAssignmentService;

    @Mock
    private TimesheetLockService timesheetLockService;

    @Before
    public void setUp() {
        timesheetService = new TimesheetServiceImpl(timesheetDAO, timesheetCommentDAO, timesheetLockService,
                aggregateReportService, projectAssignmentService, config);
    }

    @Test
    public void should_get_booked_days_for_february() throws Exception {
        Calendar cal = new GregorianCalendar(2006, Calendar.NOVEMBER, 5);

        BookedDay dayA = new BookedDay();
        dayA.setDate(new Date(2006 - 1900, Calendar.NOVEMBER, 1));
        dayA.setHours((float) 6);

        BookedDay dayB = new BookedDay();
        dayB.setDate(new Date(2006 - 1900, Calendar.NOVEMBER, 2));
        dayB.setHours((float) 8);

        when(timesheetDAO.getBookedHoursperDayInRange(1, DateUtil.calendarToMonthRange(cal))).thenReturn(Arrays.asList(dayA, dayB));
        when(config.getCompleteDayHours()).thenReturn(8f);

        List<LocalDate> results = timesheetService.getBookedDaysMonthOverview(1, cal);

        verify(timesheetDAO).getBookedHoursperDayInRange(1, DateUtil.calendarToMonthRange(cal));
        verify(config, times(2)).getCompleteDayHours();

        assertEquals(1, results.size());
        assertEquals(2, results.get(0).getDayOfMonth());
    }

    @Test
    public void should_get_timesheet_overview() throws Exception {
        List<TimesheetEntry> daoResults = new ArrayList<>();
        List<AssignmentAggregateReportElement> reportResults = new ArrayList<>();
        Calendar cal = new GregorianCalendar();

        TimesheetEntry entryA, entryB;
        TimesheetEntryId idA, idB;

        idA = new TimesheetEntryId(new Date(2006 - 1900, Calendar.OCTOBER, 2), null);
        entryA = new TimesheetEntry();
        entryA.setEntryId(idA);
        entryA.setHours((float) 5);
        daoResults.add(entryA);

        idB = new TimesheetEntryId(new Date(2006 - 1900, Calendar.OCTOBER, 6), null);
        entryB = new TimesheetEntry();
        entryB.setEntryId(idB);
        entryB.setHours((float) 3);
        daoResults.add(entryB);

        AssignmentAggregateReportElement agg = new AssignmentAggregateReportElement();
        ProjectAssignment pa = ProjectAssignmentObjectMother.createProjectAssignment(0);
        agg.setProjectAssignment(pa);
        reportResults.add(agg);

        when(timesheetDAO.getTimesheetEntriesInRange(1, DateUtil.calendarToMonthRange(cal))).thenReturn(daoResults);
        when(aggregateReportService.getHoursPerAssignmentInRange(1, DateUtil.calendarToMonthRange(cal))).thenReturn(reportResults);

        timesheetService.getTimesheetOverview(new User(1), cal);

        verify(timesheetDAO).getTimesheetEntriesInRange(1, DateUtil.calendarToMonthRange(cal));
        verify(aggregateReportService).getHoursPerAssignmentInRange(1, DateUtil.calendarToMonthRange(cal));
    }

    @Test
    public void should_get_timesheet_entries() {
        Date da = new Date(2006 - 1900, Calendar.DECEMBER, 31);
        Date db = new Date(2007 - 1900, Calendar.JANUARY, 6);
        DateRange range = new DateRange(da, db);

        DateRange rangeB = new DateRange(new Date(2006 - 1900, Calendar.DECEMBER, 31), new Date(2007 - 1900, Calendar.JANUARY, 6));

        when(timesheetDAO.getTimesheetEntriesInRange(1, range)).thenReturn(new ArrayList<TimesheetEntry>());
        when(timesheetCommentDAO.findById(new TimesheetCommentId(1, range.getDateStart()))).thenReturn(new TimesheetComment());
        when(projectAssignmentService.getProjectAssignmentsForUser(1, rangeB)).thenReturn(new ArrayList<ProjectAssignment>());
        when(config.getFirstDayOfWeek()).thenReturn(1);
        when(timesheetLockService.findLockedDatesInRange(any(Date.class), any(Date.class), any(User.class))).thenReturn(new Vector<Interval>(0, 0, 1));

        timesheetService.getWeekOverview(new User(1), new GregorianCalendar(2007, Calendar.JANUARY, 1));

        verify(timesheetDAO).getTimesheetEntriesInRange(1, range);
        verify(timesheetCommentDAO).findById(new TimesheetCommentId(1, range.getDateStart()));
        verify(projectAssignmentService).getProjectAssignmentsForUser(1, rangeB);
        verify(config).getFirstDayOfWeek();
        verify(timesheetLockService).findLockedDatesInRange(any(Date.class), any(Date.class), any(User.class));
    }
}
