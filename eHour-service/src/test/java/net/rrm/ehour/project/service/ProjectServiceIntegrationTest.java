/**
 * Created on Aug 21, 2007
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
