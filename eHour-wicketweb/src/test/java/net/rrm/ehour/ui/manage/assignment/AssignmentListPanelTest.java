package net.rrm.ehour.ui.manage.assignment;

import net.rrm.ehour.domain.ProjectAssignmentObjectMother;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserObjectMother;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AssignmentListPanelTest extends BaseSpringWebAppTester {
    private ProjectAssignmentService assignmentService;

    @Before
    public void set_up() {
        assignmentService = mock(ProjectAssignmentService.class);
        getMockContext().putBean(assignmentService);
    }

    @Test
    public void should_render() {
        User user = UserObjectMother.createUser();
        when(assignmentService.getProjectAssignmentsForUser(user)).thenReturn(Arrays.asList(ProjectAssignmentObjectMother.createProjectAssignment(1)));

        tester.startComponentInPage(new AssignmentListPanel("id", user));

        tester.assertNoErrorMessage();
    }
}
