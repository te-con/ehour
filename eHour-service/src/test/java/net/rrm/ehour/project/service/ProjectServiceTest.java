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

import java.util.ArrayList;

import junit.framework.TestCase;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.project.dao.ProjectDao;
import net.rrm.ehour.service.exception.ObjectNotFoundException;
import net.rrm.ehour.service.project.service.ProjectAssignmentService;
import net.rrm.ehour.service.project.service.ProjectService;
import net.rrm.ehour.service.project.service.ProjectServiceImpl;
import net.rrm.ehour.service.report.service.AggregateReportService;
import net.rrm.ehour.service.user.service.UserService;

/**
 *  
 **/

public class ProjectServiceTest extends TestCase
{
	private	ProjectService	projectService;
	private	ProjectDao				projectDAO;
	private ProjectAssignmentService	projectAssignmentService;
	private UserService		userService;
	private AggregateReportService aggregateReportService;
	/**
	 * 
	 */
	protected void setUp()
	{
		projectService = new ProjectServiceImpl();

		projectDAO = createMock(ProjectDao.class);
		((ProjectServiceImpl)projectService).setProjectDAO(projectDAO);
		
		projectAssignmentService = createMock(ProjectAssignmentService.class);
		((ProjectServiceImpl)projectService).setProjectAssignmentService(projectAssignmentService);

		userService = createMock(UserService.class);
		((ProjectServiceImpl)projectService).setUserService(userService);
		
		aggregateReportService = createMock(AggregateReportService.class);
		((ProjectServiceImpl)projectService).setAggregateReportService(aggregateReportService);		
	}

	
	//
	public void testGetAllProjects()
	{
		expect(projectDAO.findAllActive())
			.andReturn(new ArrayList<Project>());

		replay(projectDAO);
		
		projectService.getAllProjects(true);
		
		verify(projectDAO);
	}
	
	
	public void testGetAllProjectsInactive()
	{
		expect(projectDAO.findAll())
			.andReturn(new ArrayList<Project>());

		replay(projectDAO);
		
		projectService.getAllProjects(false);
		
		verify(projectDAO);
	}	
	
	/**
	 * Get project
	 * @param projectId
	 * @return
	 * @throws ObjectNotFoundException 
	 */
	public void testGetProject() throws ObjectNotFoundException
	{
		expect(projectDAO.findById(new Integer(1)))
			.andReturn(new Project());
		
		replay(projectDAO);
	
		projectService.getProject(1);
	
		verify(projectDAO);
	}
	
	/**
	 * Persist the project
	 * @param project
	 * @return
	 */
	public void testPersistProject()
	{
		Project prj = new Project(1);

		expect(projectDAO.persist(prj))
			.andReturn(prj);
		
		expect(userService.addAndcheckProjectManagementRoles(null))
			.andReturn(null);
	
		replay(userService);
		replay(projectDAO);
	
		projectService.persistProject(prj);
	
		verify(userService);
		verify(projectDAO);
	}
	
	/**
	 * 
	 *
	 */
//	@SuppressWarnings("unchecked")
//	public void testDeleteProjectConstraint()
//	{
//		Project prj = new Project();
//		ProjectAssignment pa = new ProjectAssignment();
//		Set<ProjectAssignment> pas = new HashSet();
//		pas.add(pa);
//		prj.setProjectAssignments(pas);
//		
//		expect(projectDAO.findById(new Integer(1)))
//			.andReturn(prj);
//
//		replay(projectDAO);
//		
////		expect()
//		
//		try
//		{
//			projectService.deleteProject(1);
//			fail("No constraint thrown");
//		} catch (ParentChildConstraintException e)
//		{
//			verify(projectDAO);
//			// ok
//		}
//	}
	
	// FIXME
//	public void testGetProjectsForUser()
//	{
//		DateRange dr = new DateRange(new Date(2007 - 1900, 1, 1), new Date(2007 - 1900, 2, 1));
//		ProjectAssignment pag1, pag2, pag3, pag4;
//		List<ProjectAssignment> pags1 = new ArrayList<ProjectAssignment>();
//		List<ProjectAssignment> pags2 = new ArrayList<ProjectAssignment>();
//		pag1 = DummyDataGenerator.getProjectAssignment(1, 1, 1, 1, 1);
//		pags1.add(pag1);
//		pag2 = DummyDataGenerator.getProjectAssignment(1, 1, 1, 2, 2);
//		pags1.add(pag2);
//		pag3 = DummyDataGenerator.getProjectAssignment(1, 1, 1, 3, 3);
//		pags1.add(pag3);
//		pag4 = DummyDataGenerator.getProjectAssignment(1, 1, 1, 1, 4);
//		pags1.add(pag4);
//		Integer userId = 1;
//		
//		pag1 = DummyDataGenerator.getProjectAssignment(1, 1, 1, 1, 1);
//		pags2.add(pag1);
//		pag2 = DummyDataGenerator.getProjectAssignment(1, 1, 1, 2, 2);
//		pags2.add(pag2);
//		pag3 = DummyDataGenerator.getProjectAssignment(1, 1, 1, 3, 3);
//		pags2.add(pag3);
//		pag4 = DummyDataGenerator.getProjectAssignment(1, 1, 1, 4, 5);
//		pags2.add(pag4);
//		
////		expect(timesheetDAO.getBookedProjectAssignmentsInRange(userId, dr)).andReturn(pags2);
//		
//		expect(projectAssignmentService.getProjectAssignmentsForUser(userId, dr))
//					.andReturn(new ArrayList<ProjectAssignment>());
//		
//		replay(projectAssignmentDAO);
//		replay(timesheetDAO);
//		replay(projectAssignmentService);
//		
//		Set<ProjectAssignment> res = projectService.getProjectsForUser(1, dr);
//		
//		verify(timesheetDAO);
//		verify(projectAssignmentService);
//		assertEquals(4, res.size());
//	}
}
