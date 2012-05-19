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
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.user.service.UserService;
import org.junit.Before;
import org.junit.Test;

public class ProjectServiceTest
{
	private	ProjectService	projectService;
	private	ProjectDao				projectDAO;
	private UserService		userService;
	private AggregateReportService aggregateReportService;

    @Before
	public void setUp()
	{
		projectService = new ProjectServiceImpl();

		projectDAO = createMock(ProjectDao.class);
		((ProjectServiceImpl)projectService).setProjectDAO(projectDAO);
		
		userService = createMock(UserService.class);
		((ProjectServiceImpl)projectService).setUserService(userService);
		
		aggregateReportService = createMock(AggregateReportService.class);
		((ProjectServiceImpl)projectService).setAggregateReportService(aggregateReportService);		
	}

    @Test
	public void testGetAllProjects()
	{
		expect(projectDAO.findAllActive())
			.andReturn(new ArrayList<Project>());

		replay(projectDAO);
		
		projectService.getProjects(true);
		
		verify(projectDAO);
	}

    @Test
	public void testGetAllProjectsInactive()
	{
		expect(projectDAO.findAll())
			.andReturn(new ArrayList<Project>());

		replay(projectDAO);
		
		projectService.getProjects(false);
		
		verify(projectDAO);
	}

    @Test
	public void testGetProject() throws ObjectNotFoundException
	{
		expect(projectDAO.findById(1))
			.andReturn(new Project());
		
		replay(projectDAO);
	
		projectService.getProject(1);
	
		verify(projectDAO);
	}

    @Test
	public void testPersistProject()
	{
		Project prj = new Project(1);

		expect(projectDAO.persist(prj))
			.andReturn(prj);
		
		expect(userService.validateProjectManagementRoles(null))
			.andReturn(null);
	
		replay(userService);
		replay(projectDAO);
	
		projectService.persistProject(prj);
	
		verify(userService);
		verify(projectDAO);
	}
}
