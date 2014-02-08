package net.rrm.ehour.project.service;

import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


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
        ProjectAssignment projectAssignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        service.persist(projectAssignment);

        verify(projectAssignmentDao).persist(projectAssignment);
    }

    @Test
    public void shouldAssignUserToDefaultProjects() {
        Project project = ProjectObjectMother.createProject(1);

        when(projectDao.findDefaultProjects()).thenReturn(Arrays.asList(project));

        User user = UserObjectMother.createUser();

        service.assignUserToDefaultProjects(user);

        verify(projectAssignmentDao).persist(any(ProjectAssignment.class));
    }

    @Test
    public void shouldAssignUsersToProjectUsingTemplateAssignment() {
        ProjectAssignment templateAssignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        templateAssignment.setAllottedHours(5f);

        User userA = UserObjectMother.createUser();
        userA.setLastName("A");
        User userB = UserObjectMother.createUser();
        userB.setLastName("B");
        userB.setUserId(2);

        List<User> users = Arrays.asList(userA, userB);

        service.assignUsersToProjects(users, templateAssignment);

        ArgumentCaptor<ProjectAssignment> captor = ArgumentCaptor.forClass(ProjectAssignment.class);

        verify(projectAssignmentDao, times(2)).persist(captor.capture());

        List<ProjectAssignment> assignments = captor.getAllValues();

        ProjectAssignment assignment = assignments.get(0);
        assertEquals(userA, assignment.getUser());
        assertEquals(5f, assignment.getAllottedHours(), 0);
    }
}
