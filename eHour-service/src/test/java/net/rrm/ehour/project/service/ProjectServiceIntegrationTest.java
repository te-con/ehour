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
	
	@Test
	public void shouldAddProjectManager() throws ObjectNotFoundException
	{
		User user = new User(5);
		Project project = projectService.getProject(4);
		project.setProjectManager(user);
		projectService.persistProject(project);
		
	}
}
