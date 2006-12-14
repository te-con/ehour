/**
 * Created on Nov 25, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
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
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.user.domain.User;

/**
 *  
 **/

public class ProjectServiceTest extends TestCase
{
	private	ProjectService	projectService;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	
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
	}

	
	//
	public void testGetAllProjects()
	{
		expect(projectDAO.findAll(true))
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
	 */
	public void testGetProject()
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
		Project prj = new Project();
		
		expect(projectDAO.persist(prj))
			.andReturn(prj);
	
		replay(projectDAO);
	
		projectService.persistProject(prj);
	
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
	
	public void testGetProjectAssignment()
	{
		expect(projectAssignmentDAO.findById(new Integer(1)))
			.andReturn(null);

		replay(projectAssignmentDAO);
		
		projectService.getProjectAssignment(1);
		
		verify(projectAssignmentDAO);
	}	
}
