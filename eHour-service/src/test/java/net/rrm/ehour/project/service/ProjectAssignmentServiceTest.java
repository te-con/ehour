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

package net.rrm.ehour.project.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import org.junit.Before;
import org.junit.Test;


/**
 * TODO 
 **/

public class ProjectAssignmentServiceTest
{
	private	ProjectAssignmentService	projectAssignmentService;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private ReportAggregatedDAO		reportAggregatedDAO;
	private ProjectAssignmentStatusService	statusService;
	
	/**
	 * 
	 */
	@Before
	public void setUp()
	{
		projectAssignmentService = new ProjectAssignmentServiceImpl();

		projectDAO = createMock(ProjectDAO.class);
		((ProjectAssignmentServiceImpl)projectAssignmentService).setProjectDAO(projectDAO);
		
		projectAssignmentDAO = createMock(ProjectAssignmentDAO.class);
		((ProjectAssignmentServiceImpl)projectAssignmentService).setProjectAssignmentDAO(projectAssignmentDAO);
		
		reportAggregatedDAO = createMock(ReportAggregatedDAO.class);
		((ProjectAssignmentServiceImpl)projectAssignmentService).setReportAggregatedDAO(reportAggregatedDAO);
		
		statusService = createMock(ProjectAssignmentStatusService.class);
		((ProjectAssignmentServiceImpl)projectAssignmentService).setProjectAssignmentStatusService(statusService);
	}
	
	/**
	 * @throws ObjectNotFoundException 
	 * 
	 *
	 */
	@Test
	public void testGetProjectAssignment() throws ObjectNotFoundException
	{
		ProjectAssignment pa = new ProjectAssignment();
		
		expect(projectAssignmentDAO.findById(new Integer(1)))
			.andReturn(pa);

		List<Serializable>	ids = new ArrayList<Serializable>();
		ids.add(1);

		expect(reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(ids))
				.andReturn(new ArrayList<AssignmentAggregateReportElement>());

		replay(projectAssignmentDAO);
		replay(reportAggregatedDAO);
		
		projectAssignmentService.getProjectAssignment(1);
		
		verify(projectAssignmentDAO);
		verify(reportAggregatedDAO);
	}	
	
//	@Test
//	public void testCheckAndNotifyDateNotOk() throws OverBudgetException
//	{
//		ProjectAssignment assignment = new ProjectAssignment(1);
//		assignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_DATE);
//
//		expect(projectAssignmentDAO.findById(new Integer(1)))
//			.andReturn(assignment);
//		replay(projectAssignmentDAO);
//
//		ProjectAssignmentStatus status = new ProjectAssignmentStatus();
//		status.addStatus(ProjectAssignmentStatus.Status.AFTER_DEADLINE);
//		
//		expect(statusService.getAssignmentStatus(assignment))
//			.andReturn(status);
//		replay(statusService);
//		
//		try
//		{
//			projectAssignmentService.checkAndNotify(assignment);
//			fail();
//		}
//		catch (OverBudgetException obe)
//		{
//			verify(projectAssignmentDAO);
//			verify(statusService);
//		}
//	}
}
