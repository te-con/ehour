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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.rrm.ehour.AbstractServiceTest;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO 
 **/
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectServiceIntegrationTest  extends AbstractServiceTest
{
	@Autowired
	private	ProjectService	projectService;

	@Test
	public void testGetProjectNotDeletable() throws ObjectNotFoundException
	{
		Project prj = projectService.getProjectAndCheckDeletability(2);
		
		assertFalse(prj.isDeletable());
	}
	
	@Test
	public void testGetProjectDeletable() throws ObjectNotFoundException
	{
		Project prj = projectService.getProjectAndCheckDeletability(4);
		
		assertTrue(prj.isDeletable());
	}
	
	@Test
	public void testGetProjectManagerProjects()
	{
		User user;
		
		user = new User();
		user.setUserId(2);
		List<Project> prj = projectService.getProjectManagerProjects(user);
		
		assertEquals(1, prj.size());
	}
	
	@Test
	public void testGetProjectsWithFilter()
	{
		List<Project> projects = projectService.getProjects("days", true);
		assertEquals(2, projects.size());
	}
}
