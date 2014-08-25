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
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class ProjectAssignmentServiceTest
{
	private	ProjectAssignmentService	projectAssignmentService;
	private	ProjectAssignmentDao	projectAssignmentDAO;
	private ReportAggregatedDao		reportAggregatedDAO;
	private ProjectAssignmentStatusService	statusService;
	
	/**
	 * 
	 */
	@Before
	public void setUp()
	{
		projectAssignmentService = new ProjectAssignmentServiceImpl();

		projectAssignmentDAO = createMock(ProjectAssignmentDao.class);
		((ProjectAssignmentServiceImpl)projectAssignmentService).setProjectAssignmentDAO(projectAssignmentDAO);
		
		reportAggregatedDAO = createMock(ReportAggregatedDao.class);
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
	@Ignore
	public void testGetProjectAssignment() throws ObjectNotFoundException
	{
		ProjectAssignment pa = new ProjectAssignment();
		
		expect(projectAssignmentDAO.findById(new Integer(1)))
			.andReturn(pa);

		List<Serializable>	ids = new ArrayList<Serializable>();
		ids.add(1);

//		expect(reportAggregatedDAO.getCumulatedHoursPerActivityForActivities(ids))
//.andReturn(new ArrayList<AssignmentAggregateReportElement>());

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
