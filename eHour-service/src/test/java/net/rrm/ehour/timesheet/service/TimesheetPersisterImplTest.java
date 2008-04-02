/**
 * 
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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryId;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.project.status.ProjectAssignmentStatus.Status;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;

import org.junit.Before;
import org.junit.Test;

/**
 * @author thies
 *
 */
public class TimesheetPersisterImplTest {
	private TimesheetPersisterImpl persister;
	private TimesheetDAO			timesheetDAO;
	private MailService				mailService;
	private ProjectAssignmentStatusService statusService;
	private ProjectAssignment assignment;
	private List<TimesheetEntry> newEntries;
	private List<TimesheetEntry> existingEntries;
	
	
	@Before
	public void setUp()
	{
		persister = new TimesheetPersisterImpl();
		
		timesheetDAO = createMock(TimesheetDAO.class);
		persister.setTimesheetDAO(timesheetDAO);

		statusService = createMock(ProjectAssignmentStatusService.class);
		persister.setProjectAssignmentStatusService(statusService);
		
		mailService = createMock(MailService.class);
		persister.setMailService(mailService);
		
		initData();
	}
	
	/**
	 * 
	 */
	private void initData()
	{
		assignment = new ProjectAssignment(1);
		assignment.setUser(new User(1));
		
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
			entryDel.setHours(0f);
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
	

	/**
	 * Test method for {@link net.rrm.ehour.timesheet.service.TimesheetPersisterImpl#validateAndPersist(net.rrm.ehour.domain.ProjectAssignment, java.util.List)}.
	 * @throws OverBudgetException 
	 */
	@Test
	public void testPersistValidatedTimesheet() throws OverBudgetException {
		timesheetDAO.delete(isA(TimesheetEntry.class));
		
		expect(timesheetDAO.merge(isA(TimesheetEntry.class)))
			.andReturn(null);

		expect(timesheetDAO.getTimesheetEntriesInRange(isA(Integer.class), isA(DateRange.class)))
			.andReturn(existingEntries);
		
		expect(statusService.getAssignmentStatus(assignment))
			.andReturn(new ProjectAssignmentStatus())
			.times(2);
		
		replay(statusService);
		replay(timesheetDAO);
		
		persister.validateAndPersist(assignment, newEntries, new DateRange());
		
		verify(timesheetDAO);
		verify(statusService);
	}
	
	/**
	 * 
	 * @throws OverBudgetException
	 */
	@Test
	public void testPersistInvalidTimesheet() {
		timesheetDAO.delete(isA(TimesheetEntry.class));
		
		expect(timesheetDAO.merge(isA(TimesheetEntry.class)))
			.andReturn(null);
		
		expect(timesheetDAO.getTimesheetEntriesInRange(isA(Integer.class), isA(DateRange.class)))
			.andReturn(existingEntries);
		
		expect(statusService.getAssignmentStatus(assignment))
			.andReturn(new ProjectAssignmentStatus());

		ProjectAssignmentStatus status = new ProjectAssignmentStatus();
		status.addStatus(Status.OVER_OVERRUN);
		status.setValid(false);
		
		expect(statusService.getAssignmentStatus(assignment))
			.andReturn(status);
		
		replay(statusService);
		replay(timesheetDAO);
		
		try
		{
			persister.validateAndPersist(assignment, newEntries, new DateRange());
			fail();
		} catch (OverBudgetException e)
		{
			verify(timesheetDAO);
			verify(statusService);
		}
	}
	
	/**
	 * 
	 * @throws OverBudgetException 
	 * @throws OverBudgetException
	 */
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

		expect(timesheetDAO.merge(isA(TimesheetEntry.class)))
			.andReturn(null);
		
		expect(timesheetDAO.getTimesheetEntriesInRange(isA(Integer.class), isA(DateRange.class)))
			.andReturn(existingEntries);

		ProjectAssignmentStatus beforeStatus = new ProjectAssignmentStatus();
		beforeStatus.setValid(false);
		
		expect(statusService.getAssignmentStatus(assignment))
			.andReturn(beforeStatus);

		ProjectAssignmentStatus status = new ProjectAssignmentStatus();
		status.addStatus(Status.OVER_OVERRUN);
		status.setValid(false);
		
		expect(statusService.getAssignmentStatus(assignment))
			.andReturn(status);
		
		replay(statusService);
		replay(timesheetDAO);
		
		persister.validateAndPersist(assignment, newEntries, new DateRange());
		verify(timesheetDAO);
		verify(statusService);
	}	

}
