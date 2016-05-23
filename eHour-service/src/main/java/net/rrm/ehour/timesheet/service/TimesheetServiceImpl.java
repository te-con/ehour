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

import com.google.common.collect.Lists;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetCommentDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.util.DateUtil;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.collection.Seq;

import java.util.*;

/**
 * Provides services for displaying and manipulating timesheets.
 * Methods are organized by their functionality rather than technical impact.
 *
 * @author Thies
 */
@Service("timesheetService")
public class TimesheetServiceImpl implements IOverviewTimesheet {
    private TimesheetDao timesheetDAO;

    private TimesheetCommentDao timesheetCommentDAO;

    private TimesheetLockService timesheetLockService;

    private AggregateReportService aggregateReportService;

    private ProjectAssignmentService projectAssignmentService;

    private EhourConfig configuration;

    @Autowired
    public TimesheetServiceImpl(TimesheetDao timesheetDAO,
                                TimesheetCommentDao timesheetCommentDAO,
                                TimesheetLockService timesheetLockService,
                                AggregateReportService aggregateReportService,
                                ProjectAssignmentService projectAssignmentService,
                                EhourConfig configuration) {
        this.timesheetDAO = timesheetDAO;
        this.timesheetCommentDAO = timesheetCommentDAO;
        this.timesheetLockService = timesheetLockService;
        this.aggregateReportService = aggregateReportService;
        this.projectAssignmentService = projectAssignmentService;
        this.configuration = configuration;
    }

    /**
     * Fetch the timesheet overview for a user. This returns an object containing the project assignments for the
     * requested month and a list with all timesheet entries for that month.
     *
     * @param requestedMonth only the month and year of the calendar is used
     * @return TimesheetOverviewAction
     * @throws ObjectNotFoundException
     */
    public TimesheetOverview getTimesheetOverview(User user, Calendar requestedMonth) {
        TimesheetOverview overview = new TimesheetOverview();

        DateRange monthRange = DateUtil.calendarToMonthRange(requestedMonth);
        overview.setProjectStatus(getProjectStatus(user.getUserId(), monthRange));

        List<TimesheetEntry> timesheetEntries = timesheetDAO.getTimesheetEntriesInRange(user.getUserId(), monthRange);

        Map<Integer, List<TimesheetEntry>> calendarMap = entriesToCalendarMap(timesheetEntries);
        overview.setTimesheetEntries(calendarMap);

        return overview;
    }

    /**
     * Get project status for user
     *
     * @param userId
     * @param monthRange
     * @return
     */
    private SortedSet<UserProjectStatus> getProjectStatus(Integer userId, DateRange monthRange) {
        List<Integer> assignmentIds = Lists.newArrayList();
        SortedSet<UserProjectStatus> userProjectStatus = new TreeSet<>();
        Map<Integer, AssignmentAggregateReportElement> originalAggregates = new HashMap<>();

        List<AssignmentAggregateReportElement> aggregates = aggregateReportService.getHoursPerAssignmentInRange(userId, monthRange);

        // only flex & fixed needed, others can already be added to the returned list
        for (AssignmentAggregateReportElement aggregate : aggregates) {
            if (aggregate.getProjectAssignment().getAssignmentType().isFixedAllottedType() ||
                    aggregate.getProjectAssignment().getAssignmentType().isFlexAllottedType()) {
                Integer assignmentId = aggregate.getProjectAssignment().getAssignmentId();
                assignmentIds.add(assignmentId);
                originalAggregates.put(assignmentId, aggregate);
            } else {
                userProjectStatus.add(new UserProjectStatus(aggregate));
            }
        }

        // fetch total hours for flex/fixed assignments
        if (!assignmentIds.isEmpty()) {
            List<AssignmentAggregateReportElement> timeAllottedAggregates = aggregateReportService.getHoursPerAssignment(assignmentIds);

            for (AssignmentAggregateReportElement aggregate : timeAllottedAggregates) {
                userProjectStatus.add(new UserProjectStatus(originalAggregates.get(aggregate.getProjectAssignment().getAssignmentId()),
                        aggregate.getHours()));
            }
        }

        return userProjectStatus;
    }

    /**
     * Get a list with all day numbers in this month that has complete booked days (config defines the completion
     * level).
     *2
     * @param userId
     * @param requestedMonth
     * @return List with Integers of complete booked days
     */
    public List<LocalDate> getBookedDaysMonthOverview(Integer userId, Calendar requestedMonth) {
        DateRange monthRange = DateUtil.calendarToMonthRange(requestedMonth);

        List<BookedDay> bookedDays = timesheetDAO.getBookedHoursperDayInRange(userId, monthRange);
        List<LocalDate> fullyBookedDays = new ArrayList<>();

        for (BookedDay bookedDay : bookedDays) {
            if (bookedDay.getHours() != null &&
                    bookedDay.getHours().floatValue() >= configuration.getCompleteDayHours()) {
                fullyBookedDays.add(new LocalDate(bookedDay.getDate()));
            }
        }

        Collections.sort(fullyBookedDays);

        return fullyBookedDays;
    }

    /**
     * Put the timesheet entries in a map where the day is the key and
     * the value is a list of timesheet entries filled out for that date
     *
     * @param timesheetEntries
     * @return
     */
    private Map<Integer, List<TimesheetEntry>> entriesToCalendarMap(List<TimesheetEntry> timesheetEntries) {
        Map<Integer, List<TimesheetEntry>> calendarMap;
        Calendar cal;
        Integer dayKey;
        List<TimesheetEntry> dayEntries;

        calendarMap = new HashMap<>();

        for (TimesheetEntry entry : timesheetEntries) {
            if (entry == null) {
                continue;
            }

            cal = new GregorianCalendar();
            cal.setTime(entry.getEntryId().getEntryDate());

            dayKey = cal.get(Calendar.DAY_OF_MONTH);

            if (calendarMap.containsKey(dayKey)) {
                dayEntries = calendarMap.get(dayKey);
            } else {
                dayEntries = new ArrayList<>();
            }

            dayEntries.add(entry);

            calendarMap.put(dayKey, dayEntries);
        }

        return calendarMap;
    }

    /**
     * Get week overview for a date. Week number of supplied requested week is used
     */
    public WeekOverview getWeekOverview(User user, Calendar requestedWeek) {
        Calendar reqWeek = (Calendar) requestedWeek.clone();
        reqWeek.setFirstDayOfWeek(configuration.getFirstDayOfWeek());

        DateRange range = DateUtil.getDateRangeForWeek(reqWeek);

        List<TimesheetEntry> timesheetEntries = timesheetDAO.getTimesheetEntriesInRange(user.getUserId(), range);
        TimesheetComment comment = timesheetCommentDAO.findById(new TimesheetCommentId(user.getUserId(), range.getDateStart()));
        List<ProjectAssignment> assignments = projectAssignmentService.getProjectAssignmentsForUser(user.getUserId(), range);

        Seq<Interval> lockedDatesAsIntervals = timesheetLockService.findLockedDatesInRange(range.getDateStart(), range.getDateEnd(), user);
        List<Date> lockedDates = TimesheetLockService$.MODULE$.intervalToJavaDates(lockedDatesAsIntervals);

        return new WeekOverview(timesheetEntries, comment, assignments, range, user, lockedDates);
    }
}
