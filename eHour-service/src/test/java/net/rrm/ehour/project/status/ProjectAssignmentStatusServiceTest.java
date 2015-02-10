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

package net.rrm.ehour.project.status;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentObjectMother;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.util.EhourConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProjectAssignmentStatusServiceTest
{
	private	ProjectAssignmentStatusServiceImpl util;
	private	ReportAggregatedDao	raDAO;
	private TimesheetDao timesheetDAO;
	
	@Before
	public void setUp() throws Exception
	{
		util = new ProjectAssignmentStatusServiceImpl();

		raDAO = mock(ReportAggregatedDao.class);
		timesheetDAO = mock(TimesheetDao.class);
		util.setTimesheetDAO(timesheetDAO);
		util.setReportAggregatedDAO(raDAO);
	}

	@Test
	public final void testGetAssignmentStatusDateIn()
	{
		ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
		
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_DATE);
		assignment.setAssignmentType(type);
		
		Calendar startCal = new GregorianCalendar();
		startCal.add(Calendar.DAY_OF_YEAR, -5);
		Date startDate = startCal.getTime();
		
		assignment.setDateStart(startDate);

		Calendar endCal = new GregorianCalendar();
		endCal.add(Calendar.DAY_OF_YEAR, 2);
		Date endDate = endCal.getTime();
		assignment.setDateEnd(endDate);

		when(timesheetDAO.getTimesheetEntriesBefore(assignment, startDate))
			.thenReturn(new ArrayList<TimesheetEntry>());

		when(timesheetDAO.getTimesheetEntriesAfter(assignment, endDate))
			.thenReturn(new ArrayList<TimesheetEntry>());
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);

		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(1, status.getStatusses().size());
	}	
	
	@Test
	public final void testGetAssignmentStatusDateOut()
	{
		ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);

		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_DATE);
		assignment.setAssignmentType(type);
		
		Calendar startCal = new GregorianCalendar();
		startCal.add(Calendar.DAY_OF_YEAR, -5);
		assignment.setDateStart(startCal.getTime());
		Date start = startCal.getTime();

		Calendar endCal = new GregorianCalendar();
		endCal.add(Calendar.DAY_OF_YEAR, -2);
		assignment.setDateEnd(endCal.getTime());
		Date end = endCal.getTime();
		
		when(timesheetDAO.getTimesheetEntriesBefore(assignment, start))
		.thenReturn(new ArrayList<TimesheetEntry>());

		List<TimesheetEntry> entry = new ArrayList<TimesheetEntry>();
		entry.add(new TimesheetEntry());
		when(timesheetDAO.getTimesheetEntriesAfter(assignment, end))
			.thenReturn(entry);

		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);

		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.AFTER_DEADLINE));
		assertEquals(1, status.getStatusses().size());
	}	
	
	@Test
	public final void testGetAssignmentStatusFixed()
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
		pag.setHours((double) 25);
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(250f);
		
		when(raDAO.getCumulatedHoursForAssignment(assignment))
				.thenReturn(pag);
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);
		
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.IN_ALLOTTED));
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(2, status.getStatusses().size());
	}
	
	@Test
	public final void testGetAssignmentStatusFixedOverAllotted()
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
		pag.setHours((double) 260);
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(250f);
		
		when(raDAO.getCumulatedHoursForAssignment(assignment))
				.thenReturn(pag);
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);
		
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.OVER_ALLOTTED));
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(2, status.getStatusses().size());
	}	
	
	@Test
	public final void testGetAssignmentStatusFlex()
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
		pag.setHours((double) 25);
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(250f);
		assignment.setAllowedOverrun(200f);
		
		when(raDAO.getCumulatedHoursForAssignment(assignment))
				.thenReturn(pag);
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);
		
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.IN_ALLOTTED));
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(2, status.getStatusses().size());
	}
	
	@Test
	public final void testGetAssignmentStatusFlexOverrun()
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
		pag.setHours((double) 260);
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(250f);
		assignment.setAllowedOverrun(200f);
		
		when(raDAO.getCumulatedHoursForAssignment(assignment))
				.thenReturn(pag);
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);
		
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.IN_OVERRUN));
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(2, status.getStatusses().size());
	}	
	
	@Test
	public final void testGetAssignmentStatusFlexOverOverrun()
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
		pag.setHours((double) 560);
		
		ProjectAssignment assignment = new ProjectAssignment();
		ProjectAssignmentType type = new ProjectAssignmentType();
		type.setAssignmentTypeId(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX);
		assignment.setAssignmentType(type);
		assignment.setAllottedHours(250f);
		assignment.setAllowedOverrun(200f);
		
		when(raDAO.getCumulatedHoursForAssignment(assignment))
				.thenReturn(pag);
		
		ProjectAssignmentStatus status = util.getAssignmentStatus(assignment);
		
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.OVER_OVERRUN));
		assertTrue(status.getStatusses().contains(ProjectAssignmentStatus.Status.RUNNING));
		assertEquals(2, status.getStatusses().size());
	}	
}
