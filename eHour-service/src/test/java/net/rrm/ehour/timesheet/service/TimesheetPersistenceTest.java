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
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.mail.service.ProjectManagerNotifierService;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetCommentDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.project.status.ProjectAssignmentStatus.Status;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.util.EhourConstants;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import scala.collection.JavaConversions;
import scala.collection.mutable.Buffer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TimesheetPersistenceTest {
    private TimesheetPersistence persister;

    @Mock
    private TimesheetDao timesheetDAO;

    @Mock
    private ProjectManagerNotifierService projectManagerNotifierService;

    @Mock
    private ProjectAssignmentStatusService statusService;

    @Mock
    private TimesheetLockService timesheetLockService;

    @Mock
    private TimesheetCommentDao commentDao;

    @Mock
    private ApplicationContext context;

    private ProjectAssignment assignment;
    private List<TimesheetEntry> newEntries;
    private List<TimesheetEntry> existingEntries;

    private DateTime baseDate = DateTime.now().withTimeAtStartOfDay();

    @Before
    public void setUp() {
        persister = new TimesheetPersistence(timesheetDAO, commentDao, statusService, projectManagerNotifierService, timesheetLockService, context);

        initData();
    }

    @SuppressWarnings("deprecation") //new dates
    private void initData() {
        assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        assignment.getProject().setProjectManager(UserObjectMother.createUser());
        assignment.setNotifyPm(true);

        assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX));

        newEntries = new ArrayList<>();

        Date dateA = baseDate.toDate();
        Date dateB = baseDate.plusDays(1).toDate();

        {
            TimesheetEntry entry = new TimesheetEntry();
            TimesheetEntryId id = new TimesheetEntryId();
            id.setProjectAssignment(assignment);
            id.setEntryDate(dateA);
            entry.setEntryId(id);
            entry.setHours(8f);
            newEntries.add(entry);
        }

        {
            TimesheetEntry entryDel = new TimesheetEntry();
            TimesheetEntryId idDel = new TimesheetEntryId();
            idDel.setProjectAssignment(assignment);
            idDel.setEntryDate(dateB);
            entryDel.setEntryId(idDel);
            entryDel.setHours(null);
            newEntries.add(entryDel);
        }

        existingEntries = new ArrayList<>();
        {
            TimesheetEntry entry = new TimesheetEntry();
            TimesheetEntryId id = new TimesheetEntryId();
            id.setProjectAssignment(assignment);
            id.setEntryDate(dateA);
            entry.setEntryId(id);
            entry.setHours(5f);
            existingEntries.add(entry);
        }

        {
            TimesheetEntry entryDel = new TimesheetEntry();
            TimesheetEntryId idDel = new TimesheetEntryId();
            idDel.setProjectAssignment(assignment);
            idDel.setEntryDate(dateB);
            entryDel.setEntryId(idDel);
            entryDel.setHours(5f);
            existingEntries.add(entryDel);
        }
    }

    @Test
    public void should_persist_new_timesheet() throws OverBudgetException {
        DateRange dateRange = new DateRange();

        okStatus();

        persister.validateAndPersist(assignment, newEntries, dateRange, Lists.<Date>newArrayList());

        verify(statusService, times(2)).getAssignmentStatus(assignment);
        verify(timesheetDAO).persist(any(TimesheetEntry.class));
        verify(timesheetDAO).getTimesheetEntriesInRange(assignment, dateRange);
    }

    @Test
    public void should_update_existing_timesheet() throws OverBudgetException {
        DateRange dateRange = new DateRange();

        withExistingEntries(dateRange);
        okStatus();

        persister.validateAndPersist(assignment, newEntries, dateRange, Lists.<Date>newArrayList());

        verify(statusService, times(2)).getAssignmentStatus(assignment);
        verify(timesheetDAO).delete(any(TimesheetEntry.class));
        verify(timesheetDAO).merge(any(TimesheetEntry.class));
    }

    @Test
    public void should_not_persist_an_timesheet_that_went_overbudget() {
        DateRange dateRange = new DateRange();

        withExistingEntries(dateRange);

        // before persist
        ProjectAssignmentStatus validStatus = new ProjectAssignmentStatus();
        when(statusService.getAssignmentStatus(assignment)).thenReturn(validStatus);

        // after persist
        ProjectAssignmentStatus invalidStatus = new ProjectAssignmentStatus();
        invalidStatus.addStatus(Status.OVER_OVERRUN);
        invalidStatus.setValid(false);

        when(statusService.getAssignmentStatus(assignment)).thenReturn(validStatus, invalidStatus);

        try {
            persister.validateAndPersist(assignment, newEntries, dateRange, Lists.<Date>newArrayList());
            fail();
        } catch (OverBudgetException e) {
            verify(timesheetDAO).merge(any(TimesheetEntry.class));
            verify(timesheetDAO).delete(any(TimesheetEntry.class));
        }
    }

    private void withExistingEntries(DateRange dateRange) {
        when(timesheetDAO.getTimesheetEntriesInRange(any(ProjectAssignment.class), eq(dateRange))).thenReturn(existingEntries);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void should_allow_to_decrease_existing_hours_even_when_project_is_over_budget() throws OverBudgetException {
        Date dateC = new Date(2008 - 1900, Calendar.APRIL, 3);

        newEntries.clear();
        existingEntries.clear();

        {
            TimesheetEntry entryDel = new TimesheetEntry();
            TimesheetEntryId idDel = new TimesheetEntryId();
            idDel.setProjectAssignment(assignment);
            idDel.setEntryDate(dateC);
            entryDel.setEntryId(idDel);
            entryDel.setHours(7f);
            newEntries.add(entryDel);
        }

        {
            TimesheetEntry entryDel = new TimesheetEntry();
            TimesheetEntryId idDel = new TimesheetEntryId();
            idDel.setProjectAssignment(assignment);
            idDel.setEntryDate(dateC);
            entryDel.setEntryId(idDel);
            entryDel.setHours(8f);
            existingEntries.add(entryDel);
        }

        when(timesheetDAO.getTimesheetEntriesInRange(any(ProjectAssignment.class), any(DateRange.class))).thenReturn(existingEntries);

        ProjectAssignmentStatus beforeStatus = new ProjectAssignmentStatus();
        beforeStatus.addStatus(Status.OVER_OVERRUN);
        beforeStatus.setValid(false);

        ProjectAssignmentStatus afterStatus = new ProjectAssignmentStatus();
        afterStatus.addStatus(Status.OVER_OVERRUN);
        afterStatus.setValid(false);

        when(statusService.getAssignmentStatus(assignment)).thenReturn(beforeStatus, afterStatus);

        persister.validateAndPersist(assignment, newEntries, new DateRange(), Lists.<Date>newArrayList());

        verify(timesheetDAO).merge(any(TimesheetEntry.class));
    }

    @Test
    public void should_not_allow_to_book_more_hours_when_the_project_is_overbudget() {
        when(timesheetDAO.getTimesheetEntriesInRange(any(ProjectAssignment.class), any(DateRange.class))).thenReturn(existingEntries);

        ProjectAssignmentStatus beforeStatus = new ProjectAssignmentStatus();
        beforeStatus.setValid(false);

        ProjectAssignmentStatus afterStatus = new ProjectAssignmentStatus();
        afterStatus.addStatus(Status.OVER_OVERRUN);
        afterStatus.setValid(false);

        when(statusService.getAssignmentStatus(assignment)).thenReturn(beforeStatus, afterStatus);

        try {
            persister.validateAndPersist(assignment, newEntries, new DateRange(), Lists.<Date>newArrayList());
            fail();
        } catch (OverBudgetException ignored) {

        }
    }

    @Test
    public void should_mail_pm_when_status_of_project_changes() throws OverBudgetException {
        when(timesheetDAO.getLatestTimesheetEntryForAssignment(assignment.getAssignmentId())).thenReturn(newEntries.get(0));
        when(timesheetDAO.getTimesheetEntriesInRange(any(ProjectAssignment.class), any(DateRange.class))).thenReturn(existingEntries);

        ProjectAssignmentStatus beforeStatus = new ProjectAssignmentStatus();
        beforeStatus.addStatus(Status.IN_ALLOTTED);
        beforeStatus.setValid(true);

        ProjectAssignmentStatus afterStatus = new ProjectAssignmentStatus();
        afterStatus.addStatus(Status.IN_OVERRUN);
        afterStatus.setValid(true);
        afterStatus.setAggregate(new AssignmentAggregateReportElement());

        when(statusService.getAssignmentStatus(assignment)).thenReturn(beforeStatus);

        when(statusService.getAssignmentStatus(assignment)).thenReturn(beforeStatus, afterStatus);

        persister.validateAndPersist(assignment, newEntries, new DateRange(), Lists.<Date>newArrayList());

        verify(timesheetDAO).delete(any(TimesheetEntry.class));
        verify(timesheetDAO).merge(any(TimesheetEntry.class));
        verify(projectManagerNotifierService).mailPMFlexAllottedReached(any(AssignmentAggregateReportElement.class), any(Date.class), eq(assignment.getProject().getProjectManager()));
    }

    @Test
    public void should_persist_individual_timesheet_entries_for_a_week() {
        TimesheetCommentId commentId = new TimesheetCommentId(1, new Date());
        TimesheetComment comment = new TimesheetComment(commentId, "comment");

        when(context.getBean(IPersistTimesheet.class)).thenReturn(persister); // through Spring for new TX per entr=y

        okStatus();

        noLocks();

        persister.persistTimesheetWeek(newEntries, comment, new DateRange(), UserObjectMother.createUser());

        verify(commentDao).persist(comment);
    }

    @Test
    public void should_not_persist_when_whole_week_is_locked() throws OverBudgetException {
        okStatus();

        TimesheetCommentId commentId = new TimesheetCommentId(1, new Date());
        TimesheetComment comment = new TimesheetComment(commentId, "comment");

        Date s = baseDate.toDate();
        DateTime end = baseDate.plusWeeks(1);
        Date e = end.toDate();


        User user = UserObjectMother.createUser();

        when(context.getBean(IPersistTimesheet.class)).thenReturn(persister);

        withLock(new Interval(baseDate, end));

        persister.persistTimesheetWeek(newEntries, comment, new DateRange(s, e), user);

        verify(timesheetDAO, never()).persist((any(TimesheetEntry.class)));
    }

    @Test
    public void should_not_persist_for_locked_day() throws OverBudgetException {
        okStatus();

        TimesheetCommentId commentId = new TimesheetCommentId(1, new Date());
        TimesheetComment comment = new TimesheetComment(commentId, "comment");

        Date s = baseDate.toDate();
        DateTime end = baseDate.plusWeeks(1);
        Date e = end.toDate();

        User user = UserObjectMother.createUser();

        when(context.getBean(IPersistTimesheet.class)).thenReturn(persister);

        TimesheetEntry entry = new TimesheetEntry();
        TimesheetEntryId id = new TimesheetEntryId();
        id.setProjectAssignment(assignment);
        id.setEntryDate(baseDate.plusDays(2).toDate());
        entry.setEntryId(id);
        entry.setHours(5f);
        newEntries.add(entry);

        withLock(new Interval(baseDate.plusDays(2), end));// cancelling out the just created entry
        persister.persistTimesheetWeek(newEntries, comment, new DateRange(s, e), user);

        verify(timesheetDAO, times(1)).persist((any(TimesheetEntry.class)));
        verify(timesheetDAO, never()).delete((any(TimesheetEntry.class)));
        verify(commentDao, times(1)).persist(any(TimesheetComment.class));
    }

    @Test
    public void should_not_persist_comment_when_whole_week_is_locked() throws OverBudgetException {
        okStatus();

        TimesheetCommentId commentId = new TimesheetCommentId(1, new Date());
        TimesheetComment comment = new TimesheetComment(commentId, "comment");

        Date s = baseDate.toDate();
        DateTime end = baseDate.plusWeeks(1);
        Date e = end.toDate();


        User user = UserObjectMother.createUser();

        when(context.getBean(IPersistTimesheet.class)).thenReturn(persister);

        withLock(new Interval(baseDate, end));

        persister.persistTimesheetWeek(newEntries, comment, new DateRange(s, e), user);

        verify(timesheetDAO, never()).persist((any(TimesheetEntry.class)));
        verify(commentDao, never()).persist(any(TimesheetComment.class));
    }

    @Test
    public void should_delete_entries_that_are_not_resubmitted() throws OverBudgetException {
        DateRange dateRange = new DateRange();

        withExistingEntries(dateRange);
        okStatus();

        persister.validateAndPersist(assignment, Lists.newArrayList(newEntries.get(0)), dateRange, Lists.<Date>newArrayList());

        verify(statusService, times(2)).getAssignmentStatus(assignment);
        verify(timesheetDAO).delete(existingEntries.get(1));
        verify(timesheetDAO).merge(existingEntries.get(0));
    }

    private void okStatus() {
        when(statusService.getAssignmentStatus(assignment)).thenReturn(new ProjectAssignmentStatus());
    }

    private void withLock(Interval... lockedRange) {
        Buffer<Interval> scalaBuffer = JavaConversions.asScalaBuffer(Lists.newArrayList(lockedRange));
        when(timesheetLockService.findLockedDatesInRange(any(Date.class), any(Date.class), any(User.class))).thenReturn(scalaBuffer.toList());
    }

    private void noLocks() {
        Buffer<Interval> scalaBuffer = JavaConversions.asScalaBuffer(Lists.<Interval>newArrayList());
        scala.collection.immutable.List<Interval> intervals = scalaBuffer.toList();

        when(timesheetLockService.findLockedDatesInRange(any(Date.class), any(Date.class), any(User.class))).thenReturn(intervals);
    }
}
