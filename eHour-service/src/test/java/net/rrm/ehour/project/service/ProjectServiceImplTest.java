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

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectObjectMother;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserObjectMother;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceImplTest {
    private ProjectServiceImpl projectService;
    
    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserService userService;

    @Mock
    private AggregateReportService aggregateReportService;

    @Mock
    private ProjectAssignmentManagementService projectAssignmentManagementService;

    @Before
    public void setUp() {
        projectService = new ProjectServiceImpl(projectDao, projectAssignmentManagementService, aggregateReportService, userService);
    }

    @Test
    public void should_get_all_active_projects() {
        when(projectDao.findAllActive()).thenReturn(Lists.newArrayList(ProjectObjectMother.createProject(1)));

        List<Project> activeProjects = projectService.getActiveProjects();

        assertEquals(1, activeProjects.size());
    }

    @Test
    public void should_get_all_projects() {
        when(projectDao.findAll()).thenReturn(Lists.newArrayList(ProjectObjectMother.createProject(1)));

        List<Project> projects = projectService.getProjects();

        assertEquals(1, projects.size());
    }


    @Test
    public void should_get_project_on_id() throws ObjectNotFoundException {
        Project project = ProjectObjectMother.createProject(1);
        when(projectDao.findById(1)).thenReturn(project);

        assertEquals(project, projectService.getProject(1));
    }

    @Test
    public void should_create_or_update_project() {
        Project project = ProjectObjectMother.createProject(1);
        User pm = UserObjectMother.createUser();
        project.setProjectManager(pm);

        projectService.createProject(project);

        verify(projectDao).persist(project);
        verify(userService).validateProjectManagementRoles(pm.getPK());
    }

    @Test
    public void should_create_project_and_assign_to_default_project() {
        Project project = ProjectObjectMother.createProject(1);
        User pm = UserObjectMother.createUser();
        project.setProjectManager(pm);
        project.setDefaultProject(true);

        projectService.createProject(project);

        verify(projectDao).persist(project);
        verify(projectAssignmentManagementService).assignAllUsersToProject(project);
    }
}
