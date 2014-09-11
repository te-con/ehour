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
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

/**
 * @author thies
 */
public class TimesheetPersistanceTest {
    private TimesheetPersistance persister;
    private TimesheetDao timesheetDAO;
    private ProjectManagerNotifierService projectManagerNotifierService;
    private ProjectAssignmentStatusService statusService;
    private ProjectAssignment assignment;
    private List<TimesheetEntry> newEntries;
    private List<TimesheetEntry> existingEntries;

    @Before
    public void setUp() {
        timesheetDAO = createMock(TimesheetDao.class);
        statusService = createMock(ProjectAssignmentStatusService.class);
        projectManagerNotifierService = createMock(ProjectManagerNotifierService.class);
        ApplicationContext context = createMock(ApplicationContext.class);
        TimesheetCommentDao commentDao = createMock(TimesheetCommentDao.class);

        persister = new TimesheetPersistance(timesheetDAO, commentDao, statusService, projectManagerNotifierService, context);

        initData();
    }

    @SuppressWarnings("deprecation") //new dates
    private void initData() {
        assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        assignment.getProject().setProjectManager(UserObjectMother.createUser());
        assignment.setNotifyPm(true);

        assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX));

        newEntries = new ArrayList<TimesheetEntry>();

        Date dateA = new Date(2008 - 1900, 4 - 1, 1);
        Date dateB = new Date(2008 - 1900, 4 - 1, 2);

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

        existingEntries = new ArrayList<TimesheetEntry>();
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
    public void shouldPersistValidatedTimesheet() throws OverBudgetException {
        DateRange dateRange = new DateRange();

        timesheetDAO.delete(isA(TimesheetEntry.class));

        expect(timesheetDAO.merge(isA(TimesheetEntry.class))).andReturn(null);

        expect(timesheetDAO.getTimesheetEntriesInRange(assignment, dateRange)).andReturn(existingEntries);

        expect(statusService.getAssignmentStatus(assignment)).andReturn(new ProjectAssignmentStatus()).times(2);

        replay(statusService);
        replay(timesheetDAO);

        persister.validateAndPersist(assignment, newEntries, dateRange);

        verify(timesheetDAO);
        verify(statusService);
    }

    @Test
    public void testPersistInvalidTimesheet() {
        timesheetDAO.delete(isA(TimesheetEntry.class));

        expect(timesheetDAO.merge(isA(TimesheetEntry.class))).andReturn(null);

        expect(timesheetDAO.getTimesheetEntriesInRange(isA(ProjectAssignment.class), isA(DateRange.class))).andReturn(existingEntries);

        expect(statusService.getAssignmentStatus(assignment)).andReturn(new ProjectAssignmentStatus());

        ProjectAssignmentStatus status = new ProjectAssignmentStatus();
        status.addStatus(Status.OVER_OVERRUN);
        status.setValid(false);

        expect(statusService.getAssignmentStatus(assignment)).andReturn(status);

        replay(statusService);
        replay(timesheetDAO);

        try {
            persister.validateAndPersist(assignment, newEntries, new DateRange());
            fail();
        } catch (OverBudgetException e) {
            verify(timesheetDAO);
            verify(statusService);
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testPersistOverrunDecreasingTimesheet() throws OverBudgetException {
        Date dateC = new Date(2008 - 1900, 4 - 1, 3);

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

        expect(timesheetDAO.merge(isA(TimesheetEntry.class))).andReturn(null);

        expect(timesheetDAO.getTimesheetEntriesInRange(isA(ProjectAssignment.class), isA(DateRange.class))).andReturn(existingEntries);

        ProjectAssignmentStatus beforeStatus = new ProjectAssignmentStatus();
        beforeStatus.addStatus(Status.OVER_OVERRUN);
        beforeStatus.setValid(false);

        expect(statusService.getAssignmentStatus(assignment)).andReturn(beforeStatus);

        ProjectAssignmentStatus status = new ProjectAssignmentStatus();
        status.addStatus(Status.OVER_OVERRUN);
        status.setValid(false);

        expect(statusService.getAssignmentStatus(assignment)).andReturn(status);

        replay(statusService);
        replay(timesheetDAO);

        persister.validateAndPersist(assignment, newEntries, new DateRange());
        verify(timesheetDAO);
        verify(statusService);
    }

    @Test
    public void testPersistOverrunInvalidTimesheet() {
        expect(timesheetDAO.getTimesheetEntriesInRange(isA(ProjectAssignment.class), isA(DateRange.class))).andReturn(existingEntries);

        ProjectAssignmentStatus beforeStatus = new ProjectAssignmentStatus();
        beforeStatus.setValid(false);

        expect(statusService.getAssignmentStatus(assignment)).andReturn(beforeStatus);

        ProjectAssignmentStatus status = new ProjectAssignmentStatus();
        status.addStatus(Status.OVER_OVERRUN);
        status.setValid(false);

        expect(statusService.getAssignmentStatus(assignment)).andReturn(status);

        replay(statusService);
        replay(timesheetDAO);

        try {
            persister.validateAndPersist(assignment, newEntries, new DateRange());
            fail();
        } catch (OverBudgetException ignored) {

        }
        verify(timesheetDAO);
        verify(statusService);
    }

    @Test
    public void testMailStatusChange() throws OverBudgetException {
        timesheetDAO.delete(isA(TimesheetEntry.class));

        expect(timesheetDAO.getLatestTimesheetEntryForAssignment(assignment.getAssignmentId())).andReturn(newEntries.get(0));

        expect(timesheetDAO.merge(isA(TimesheetEntry.class))).andReturn(null);

        expect(timesheetDAO.getTimesheetEntriesInRange(isA(ProjectAssignment.class), isA(DateRange.class))).andReturn(existingEntries);

        ProjectAssignmentStatus beforeStatus = new ProjectAssignmentStatus();
        beforeStatus.addStatus(Status.IN_ALLOTTED);
        beforeStatus.setValid(true);

        ProjectAssignmentStatus afterStatus = new ProjectAssignmentStatus();
        afterStatus.addStatus(Status.IN_OVERRUN);
        afterStatus.setValid(true);
        afterStatus.setAggregate(new AssignmentAggregateReportElement());

        expect(statusService.getAssignmentStatus(assignment)).andReturn(beforeStatus);

        expect(statusService.getAssignmentStatus(assignment)).andReturn(afterStatus);

        projectManagerNotifierService.mailPMFlexAllottedReached(isA(AssignmentAggregateReportElement.class), isA(Date.class), isA(User.class));

        replay(statusService);
        replay(timesheetDAO);
        replay(projectManagerNotifierService);

        persister.validateAndPersist(assignment, newEntries, new DateRange());

        verify(timesheetDAO);
        verify(statusService);
        verify(projectManagerNotifierService);
    }
}

