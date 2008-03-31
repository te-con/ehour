/**
 * 
 */
package net.rrm.ehour.timesheet.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryId;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
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
	}

	/**
	 * Test method for {@link net.rrm.ehour.timesheet.service.TimesheetPersisterImpl#validateAndPersist(net.rrm.ehour.domain.ProjectAssignment, java.util.List)}.
	 * @throws OverBudgetException 
	 */
	@Test
	public void testPersistValidatedTimesheet() throws OverBudgetException {
		ProjectAssignment assignment = new ProjectAssignment(1);
		
		List<TimesheetEntry> entries = new ArrayList<TimesheetEntry>();
		
		TimesheetEntry entry = new TimesheetEntry();
		TimesheetEntryId id = new TimesheetEntryId();
		id.setProjectAssignment(assignment);
		entry.setEntryId(id);
		entry.setHours(2f);
		entries.add(entry);
		
		TimesheetEntry entryDel = new TimesheetEntry();
		TimesheetEntryId idDel = new TimesheetEntryId();
		idDel.setProjectAssignment(assignment);
		entryDel.setEntryId(id);
		entryDel.setHours(0f);
		entries.add(entryDel);
		
		timesheetDAO.delete(entry);
		
		expect(timesheetDAO.persist(entry))
			.andReturn(entry);
		
		expect(statusService.getAssignmentStatus(assignment))
			.andReturn(new ProjectAssignmentStatus())
			.times(2);
		
		replay(statusService);
		replay(timesheetDAO);
		
		persister.validateAndPersist(assignment, entries, new DateRange());
		
		verify(timesheetDAO);
		verify(statusService);
	}

}
