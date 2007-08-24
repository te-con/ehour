/**
 * Created on Aug 21, 2007
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

import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.user.domain.User;

/**
 * TODO 
 **/

public class ProjectServiceIntegrationTest  extends BaseDAOTest
{
	private	ProjectService	projectService;

	/**
	 * @param projectService the projectService to set
	 */
	public void setProjectService(ProjectService projectService)
	{
		this.projectService = projectService;
	}
	
	public void testGetProjectNotDeletable()
	{
		Project prj = projectService.getProjectAndCheckDeletability(2);
		
		assertFalse(prj.isDeletable());
	}
	
	public void testGetProjectDeletable()
	{
		Project prj = projectService.getProjectAndCheckDeletability(4);
		
		assertTrue(prj.isDeletable());
	}
	
	public void testGetProjectManagerProjects()
	{
		User user;
		
		user = new User();
		user.setUserId(2);
		List<Project> prj = projectService.getProjectManagerProjects(user);
		
		assertEquals(1, prj.size());
	}
	
	public void testGetProjectsWithFilter()
	{
		List<Project> projects = projectService.getProjects("days", true);
		assertEquals(2, projects.size());
	}
	
	
	
	
	public void testGetAllProjectsForUser()
	{
		List<ProjectAssignment> pas = projectService.getAllProjectsForUser(1);
		assertEquals(7, pas.size());
	}
	
	protected String[] getConfigLocations()
	{
		return new String[] { "classpath:/applicationContext-datasource.xml",
							  "classpath:/applicationContext-dao.xml",
							  "classpath:/applicationContext-mail.xml", 
							  "classpath:/applicationContext-service.xml"};	
	}	
	
}
