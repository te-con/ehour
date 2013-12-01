package net.rrm.ehour.ui.admin.project;

import net.rrm.ehour.domain.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProjectAdminBackingBeanTest {
    @Test
    public void should_remove_duplicate_assignments_on_id_when_they_have_1() {
        Project project = ProjectObjectMother.createProject(1);
        ProjectAdminBackingBean bean = new ProjectAdminBackingBean(project);

        ProjectAssignment projectAssignmentA = ProjectAssignmentObjectMother.createProjectAssignment(1);
        ProjectAssignment projectAssignmentB = ProjectAssignmentObjectMother.createProjectAssignment(1);
        bean.addAssignmentToCommit(projectAssignmentA);
        bean.addAssignmentToCommit(projectAssignmentB);

        assertEquals(1, bean.getAssignmentsToCommit().size());
    }

    @Test
    public void should_remove_duplicate_assignments_on_user_and_project_when_they_dont_have_an_id() {
        Project project = ProjectObjectMother.createProject(1);
        ProjectAdminBackingBean bean = new ProjectAdminBackingBean(project);

        User user = UserObjectMother.createUser();

        ProjectAssignment assignmentA = new ProjectAssignment(user, project);
        ProjectAssignment assignmentB = new ProjectAssignment(user, project);

        bean.addAssignmentToCommit(assignmentA);
        bean.addAssignmentToCommit(assignmentB);

        assertEquals(1, bean.getAssignmentsToCommit().size());
    }
}
