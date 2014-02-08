package net.rrm.ehour.ui.admin.assignment.form;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentObjectMother;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.admin.assignment.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class CreateAssignmentProjectSelectionPanelTest extends BaseSpringWebAppTester {

    private CustomerService customerService;
    private ProjectService projectService;

    @Before
    public void set_up() {
        customerService = mock(CustomerService.class);
        getMockContext().putBean(customerService);

        projectService = mock(ProjectService.class);
        getMockContext().putBean(projectService);
    }


    @Test
    public void should_render() {

        ProjectAssignment assignment = ProjectAssignmentObjectMother.createProjectAssignment(1);

        AssignmentAdminBackingBean backingBean = new AssignmentAdminBackingBean(assignment);
        tester.startComponentInPage(new CreateAssignmentProjectSelectionPanel("id", new Model<AssignmentAdminBackingBean>(backingBean)));

        tester.assertNoErrorMessage();
    }
}
