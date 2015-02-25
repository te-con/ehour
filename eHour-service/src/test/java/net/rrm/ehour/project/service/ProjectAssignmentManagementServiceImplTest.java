package net.rrm.ehour.project.service;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectAssignmentManagementServiceImplTest {
    private ProjectAssignmentManagementServiceImpl service;

    @Mock
    private ProjectAssignmentDao projectAssignmentDao;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TimesheetDao timesheetDao;

    @Before
    public void setUp() {
        service = new ProjectAssignmentManagementServiceImpl(userDao, projectDao, projectAssignmentDao, timesheetDao);
    }

    @Test
    public void should_assign_user_to_project() throws ProjectAlreadyAssignedException {
        ProjectAssignment projectAssignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        service.persistNewProjectAssignment(projectAssignment);

        verify(projectAssignmentDao).persist(projectAssignment);
    }

    @Test
    public void should_assign_user_to_default_projects() {
        Project project = ProjectObjectMother.createProject(1);

        when(projectDao.findDefaultProjects()).thenReturn(Arrays.asList(project));

        User user = UserObjectMother.createUser();

        service.assignUserToDefaultProjects(user);

        verify(projectAssignmentDao).persist(any(ProjectAssignment.class));
    }

    @Test
    public void should_assign_users_to_project_using_template_assignment() {
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

    @Test
    public void should_not_update_assignment_when_there_is_data_before_the_start() {
        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        Date dateStart = new Date();
        assignment.setDateStart(dateStart);

        when(timesheetDao.getTimesheetEntriesBefore(assignment, dateStart)).thenReturn(Lists.newArrayList(TimesheetEntryObjectMother.createTimesheetEntry(1, new Date(), 5f)));

        try {
            service.persistUpdatedProjectAssignment(assignment);
            fail();
        } catch (ProjectAssignmentValidationException e) {
            List<ProjectAssignmentValidationException.Issue> issues = e.getIssues();
            assertThat(issues, contains(ProjectAssignmentValidationException.Issue.EXISTING_DATA_BEFORE_START));
        }
    }

    @Test
    public void should_not_update_assignment_when_there_is_data_after_the_end() {
        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        Date date = new Date();
        assignment.setDateEnd(date);

        when(timesheetDao.getTimesheetEntriesAfter(assignment, date)).thenReturn(Lists.newArrayList(TimesheetEntryObjectMother.createTimesheetEntry(1, new Date(), 5f)));

        try {
            service.persistUpdatedProjectAssignment(assignment);
            fail();
        } catch (ProjectAssignmentValidationException e) {
            List<ProjectAssignmentValidationException.Issue> issues = e.getIssues();
            assertThat(issues, contains(ProjectAssignmentValidationException.Issue.EXISTING_DATA_AFTER_END));
        }
    }

}
