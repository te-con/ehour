/**
 * Created on Nov 25, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.project.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.user.service.UserService;

/**
 *  
 **/

public class ProjectServiceTest extends TestCase
{
	private	ProjectService	projectService;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private ProjectAssignmentService	projectAssignmentService;
	private UserService		userService;
	/**
	 * 
	 */
	protected void setUp()
	{
		projectService = new ProjectServiceImpl();

		projectDAO = createMock(ProjectDAO.class);
		((ProjectServiceImpl)projectService).setProjectDAO(projectDAO);
		
		projectAssignmentDAO = createMock(ProjectAssignmentDAO.class);
		((ProjectServiceImpl)projectService).setProjectAssignmentDAO(projectAssignmentDAO);
		
		projectAssignmentService = createMock(ProjectAssignmentService.class);
		((ProjectServiceImpl)projectService).setProjectAssignmentService(projectAssignmentService);

		userService = createMock(UserService.class);
		((ProjectServiceImpl)projectService).setUserService(userService);

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
	public void testDeleteProjectConstraint()
	{
		Project prj = new Project();
		ProjectAssignment pa = new ProjectAssignment();
		Set<ProjectAssignment> pas = new HashSet();
		pas.add(pa);
		prj.setProjectAssignments(pas);
		
		expect(projectDAO.findById(new Integer(1)))
			.andReturn(prj);

		replay(projectDAO);
		
		try
		{
			projectService.deleteProject(1);
			fail("No constraint thrown");
		} catch (ParentChildConstraintException e)
		{
			verify(projectDAO);
			// ok
		}
	}
	
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
