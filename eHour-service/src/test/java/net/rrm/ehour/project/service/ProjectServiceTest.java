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

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentObjectMother;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.user.service.UserService;
import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class ProjectServiceTest {
    private ProjectServiceImpl projectService;
    private ProjectDao projectDao;
    private UserService userService;
    private AggregateReportService aggregateReportService;
    private ProjectAssignmentManagementService projectAssignmentManagementService;

    @Before
    public void setUp() {
        projectDao = createMock(ProjectDao.class);
        userService = createMock(UserService.class);
        aggregateReportService = createMock(AggregateReportService.class);
        projectAssignmentManagementService = createMock(ProjectAssignmentManagementService.class);

        projectService = new ProjectServiceImpl(projectDao, projectAssignmentManagementService, aggregateReportService, userService);
    }

    @Test
    public void should_get_all_active_projects() {
        expect(projectDao.findAllActive()).andReturn(new ArrayList<Project>());

        replay(projectDao);

        projectService.getProjects(true);

        verify(projectDao);
    }

    @Test
    public void should_get_all_projects() {
        expect(projectDao.findAll()).andReturn(new ArrayList<Project>());

        replay(projectDao);

        projectService.getProjects(false);

        verify(projectDao);
    }

    @Test
    public void should_get_project_on_id() throws ObjectNotFoundException {
        expect(projectDao.findById(1)).andReturn(new Project());

        replay(projectDao);

        projectService.getProject(1);

        verify(projectDao);
    }

    @Test
    public void should_create_project_and_make_assignments() {
        Project project = new Project(1);

        expect(projectDao.persist(project)).andReturn(project);
        expect(userService.validateProjectManagementRoles(null)).andReturn(null);

        Capture<ProjectAssignment> captureArgument = new Capture<ProjectAssignment>();
        projectAssignmentManagementService.updateProjectAssignment(capture(captureArgument));

        replay(userService, projectDao, projectAssignmentManagementService);

        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        assignment.setProject(null);

        projectService.createProject(project, Arrays.asList(assignment));

        verify(userService);
        verify(projectDao);
        verify(projectAssignmentManagementService);

        ProjectAssignment projectAssignment = captureArgument.getValue();
        assertEquals(project, projectAssignment.getProject());
    }

    @Test
    public void should_update_project_and_make_assignments() {
        Project project = new Project(1);

        expect(projectDao.persist(project)).andReturn(project);
        expect(userService.validateProjectManagementRoles(null)).andReturn(null);

        Capture<ProjectAssignment> captureArgument = new Capture<ProjectAssignment>();
        projectAssignmentManagementService.updateProjectAssignment(capture(captureArgument));

        replay(userService, projectDao, projectAssignmentManagementService);

        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        assignment.setProject(project);

        projectService.createProject(project, Arrays.asList(assignment));

        verify(userService);
        verify(projectDao);
        verify(projectAssignmentManagementService);
    }

}
