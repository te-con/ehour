package net.rrm.ehour.project.service;

import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ProjectAssignmentManagementServiceImplTest {
    private ProjectAssignmentManagementServiceImpl service;

    @Mock
    private ProjectAssignmentDao projectAssignmentDao;

    @Mock
    private ProjectDao projectDao;

    @Before
    public void setUp() {
        service = new ProjectAssignmentManagementServiceImpl();

        MockitoAnnotations.initMocks(this);

        service.setProjectAssignmentDAO(projectAssignmentDao);
        service.setProjectDAO(projectDao);
    }

    @Test
    public void shouldAssignUserToProject() throws ProjectAlreadyAssignedException {
        ProjectAssignment projectAssignment = ProjectAssignmentMother.createProjectAssignment(1);
        service.assignUserToProject(projectAssignment);

        verify(projectAssignmentDao).persist(projectAssignment);
    }

    @Test
    public void shouldAssignUserToDefaultProjects() {
        Project project = ProjectMother.createProject(1);

        when(projectDao.findDefaultProjects()).thenReturn(Arrays.asList(project));

        User user = UserMother.createUser();

        service.assignUserToDefaultProjects(user);

        verify(projectAssignmentDao).persist(any(ProjectAssignment.class));


    }
}
