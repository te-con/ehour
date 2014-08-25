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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.activity.status.ActivityStatus;
import net.rrm.ehour.activity.status.ActivityStatusService;
import net.rrm.ehour.activity.status.ActivityStatus.Status;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ActivityMother;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentMother;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryId;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserMother;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author thies
 * 
 */
public class TimesheetPersisterImplTest
{
	private TimesheetPersisterImpl persister;
	private TimesheetDao timesheetDAO;
	private MailService mailService;
	private ActivityStatusService statusService;
	private List<TimesheetEntry> newEntries;
	private List<TimesheetEntry> existingEntries;

	private Activity activity;
	
	@Before
	public void setUp()
	{
		persister = new TimesheetPersisterImpl();

		timesheetDAO = createMock(TimesheetDao.class);
		persister.setTimesheetDAO(timesheetDAO);

		statusService = createMock(ActivityStatusService.class);
		
		persister.setActivityStatusService(statusService);

		mailService = createMock(MailService.class);
		persister.setMailService(mailService);

		initData();
	}

	@SuppressWarnings("deprecation") //new dates
	private void initData()
	{
		activity = ActivityMother.createActivity(1);
		
		activity.getProject().setProjectManager(UserMother.createUser());

		newEntries = new ArrayList<TimesheetEntry>();

		Date dateA = new Date(2008 - 1900, 4 - 1, 1);
		Date dateB = new Date(2008 - 1900, 4 - 1, 2);

		{
			TimesheetEntry entry = new TimesheetEntry();
			TimesheetEntryId id = new TimesheetEntryId();
			id.setActivity(activity);
			id.setEntryDate(dateA);
			entry.setEntryId(id);
			entry.setHours(8f);
			newEntries.add(entry);
		}

		{
			TimesheetEntry entryDel = new TimesheetEntry();
			TimesheetEntryId idDel = new TimesheetEntryId();
			idDel.setActivity(activity);
			idDel.setEntryDate(dateB);
			entryDel.setEntryId(idDel);
			entryDel.setHours(0f);
			newEntries.add(entryDel);
		}

		existingEntries = new ArrayList<TimesheetEntry>();
		{
			TimesheetEntry entry = new TimesheetEntry();
			TimesheetEntryId id = new TimesheetEntryId();
			id.setActivity(activity);
			id.setEntryDate(dateA);
			entry.setEntryId(id);
			entry.setHours(5f);
			existingEntries.add(entry);
		}

		{
			TimesheetEntry entryDel = new TimesheetEntry();
			TimesheetEntryId idDel = new TimesheetEntryId();
			idDel.setActivity(activity);
			idDel.setEntryDate(dateB);
			entryDel.setEntryId(idDel);
			entryDel.setHours(5f);
			existingEntries.add(entryDel);
		}
	}

	@Test
	public void shouldPersistValidatedTimesheet() throws OverBudgetException
	{
		DateRange dateRange = new DateRange();
		
		timesheetDAO.delete(isA(TimesheetEntry.class));

		expect(timesheetDAO.merge(isA(TimesheetEntry.class))).andReturn(null);

		expect(timesheetDAO.getTimesheetEntriesInRange(activity, dateRange)).andReturn(existingEntries);
		
		expect(statusService.getActivityStatus(activity)).andReturn(new ActivityStatus()).times(2);

		replay(statusService);
		replay(timesheetDAO);

		persister.validateAndPersist(activity, newEntries, dateRange);

		verify(timesheetDAO);
		verify(statusService);
	}

	/**
	 * 
	 * @throws OverBudgetException 
	 */
	@Test(expected=OverBudgetException.class)
	public void testPersistInvalidTimesheet() throws OverBudgetException
	{
		timesheetDAO.delete(isA(TimesheetEntry.class));

		expect(timesheetDAO.merge(isA(TimesheetEntry.class))).andReturn(null);

		expect(timesheetDAO.getTimesheetEntriesInRange(isA(Activity.class), isA(DateRange.class))).andReturn(existingEntries);

		expect(statusService.getActivityStatus(activity)).andReturn(new ActivityStatus());

		ActivityStatus inValidStatus = new ActivityStatus();
		inValidStatus.setValid(false);
		
		expect(statusService.getActivityStatus(activity)).andReturn(inValidStatus);

		replay(statusService);
		replay(timesheetDAO);

		persister.validateAndPersist(activity, newEntries, new DateRange());

		verify(timesheetDAO);
		verify(statusService);
	}

	/**
	 * 
	 * @throws OverBudgetException
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testPersistOverrunDecreasingTimesheet() throws OverBudgetException
	{
		Date dateC = new Date(2008 - 1900, 4 - 1, 3);

		newEntries.clear();
		existingEntries.clear();

		{
			TimesheetEntry entryDel = new TimesheetEntry();
			TimesheetEntryId idDel = new TimesheetEntryId();
			idDel.setActivity(activity);
			idDel.setEntryDate(dateC);
			entryDel.setEntryId(idDel);
			entryDel.setHours(7f);
			newEntries.add(entryDel);
		}

		{
			TimesheetEntry entryDel = new TimesheetEntry();
			TimesheetEntryId idDel = new TimesheetEntryId();
			idDel.setActivity(activity);
			idDel.setEntryDate(dateC);
			entryDel.setEntryId(idDel);
			entryDel.setHours(8f);
			existingEntries.add(entryDel);
		}

		expect(timesheetDAO.merge(isA(TimesheetEntry.class))).andReturn(null);

		expect(timesheetDAO.getTimesheetEntriesInRange(isA(Activity.class), isA(DateRange.class))).andReturn(existingEntries);

		ActivityStatus inValidStatus = new ActivityStatus();
		inValidStatus.setValid(false);
		
		expect(statusService.getActivityStatus(activity)).andReturn(inValidStatus).times(2);

		replay(statusService);
		replay(timesheetDAO);

		persister.validateAndPersist(activity, newEntries, new DateRange());
		verify(timesheetDAO);
		verify(statusService);
	}

	/**
	 * 
	 * @throws OverBudgetException
	 */
	@Test(expected=OverBudgetException.class)
	public void testPersistOverrunInvalidTimesheet() throws OverBudgetException
	{
		expect(timesheetDAO.getTimesheetEntriesInRange(isA(Activity.class), isA(DateRange.class))).andReturn(existingEntries);

		ActivityStatus inValidStatus = new ActivityStatus();
		inValidStatus.setValid(false);
		
		expect(statusService.getActivityStatus(activity)).andReturn(inValidStatus).times(2);

		replay(statusService);
		replay(timesheetDAO);

		persister.validateAndPersist(activity, newEntries, new DateRange());

		verify(timesheetDAO);
		verify(statusService);
	}

	
	@Test
	@Ignore
	//TODO-NK Mail functionality is commented out for the moment. Need to see how we want to implement the same
	public void testMailStatusChange() throws OverBudgetException
	{
		timesheetDAO.delete(isA(TimesheetEntry.class));

		expect(timesheetDAO.getLatestTimesheetEntryForActivity(activity.getId())).andReturn(newEntries.get(0));

		expect(timesheetDAO.merge(isA(TimesheetEntry.class))).andReturn(null);

		expect(timesheetDAO.getTimesheetEntriesInRange(isA(Activity.class), isA(DateRange.class))).andReturn(existingEntries);

		ActivityStatus beforeStatus = new ActivityStatus();
		beforeStatus.addStatus(Status.IN_ALLOTTED);
		beforeStatus.setValid(true);

		ActivityStatus afterStatus = new ActivityStatus();
		afterStatus.addStatus(Status.OVER_ALLOTTED);
		afterStatus.setValid(true);

		expect(statusService.getActivityStatus(activity)).andReturn(beforeStatus);

		expect(statusService.getActivityStatus(activity)).andReturn(afterStatus);

		mailService.mailPMFlexAllottedReached(isA(AssignmentAggregateReportElement.class), isA(Date.class), isA(User.class));

		replay(statusService);
		replay(timesheetDAO);
		replay(mailService);

		persister.validateAndPersist(activity, newEntries, new DateRange());

		verify(timesheetDAO);
		verify(statusService);
		verify(mailService);
	}

}
