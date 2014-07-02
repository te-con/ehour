package net.rrm.ehour.ui.manage.assignment;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentObjectMother;
import net.rrm.ehour.domain.ProjectObjectMother;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AssignmentAdminBackingBeanTest {
    @Test
    public void should_update_customer_when_single_project_is_selected_in_edit_mode() {
        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        AssignmentAdminBackingBean bean = new AssignmentAdminBackingBean(assignment);

        Project project = ProjectObjectMother.createProject(10);
        assignment.setProject(project);

        bean.updateCustomerBasedOnSelectedProject();

        assertEquals(project.getCustomer(), bean.getCustomer());
    }

    @Test
    public void should_not_update_customer_when_multiple_projects_are_selected() {
        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        assignment.setAssignmentId(null);
        AssignmentAdminBackingBean bean = new AssignmentAdminBackingBean(assignment);

        bean.setSelectedProjects(Arrays.asList(ProjectObjectMother.createProject(10), ProjectObjectMother.createProject(11)));

        bean.updateCustomerBasedOnSelectedProject();

        assertNull(bean.getCustomer());
    }

    @Test
    public void should_update_customer_when_single_projects_is_selected_in_add_mode() {
        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        assignment.setAssignmentId(null);should_update_customer_when_single_project_is_selected_in_edit_mode();
        AssignmentAdminBackingBean bean = new AssignmentAdminBackingBean(assignment);

        Project project = ProjectObjectMother.createProject(10);
        bean.setSelectedProjects(Arrays.asList(project));

        bean.updateCustomerBasedOnSelectedProject();

        assertEquals(project.getCustomer(), bean.getCustomer());
    }

    @Test
    public void should_create_assignment_per_project_when_multiple_selected() {
        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);
        assignment.setAssignmentId(null);
        AssignmentAdminBackingBean bean = new AssignmentAdminBackingBean(assignment);

        Project project1 = ProjectObjectMother.createProject(10);
        Project project2 = ProjectObjectMother.createProject(11);
        bean.setSelectedProjects(Arrays.asList(project1, project2));

        List<ProjectAssignment> forSave = bean.getProjectAssignmentsForSave();

        assertEquals(2, forSave.size());
        assertEquals(project1, forSave.get(0).getProject());
        assertEquals(project2, forSave.get(1).getProject());
    }
}
