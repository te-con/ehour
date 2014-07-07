package net.rrm.ehour.ui.manage.assignment;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.ProjectObjectMother;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.DummyUIDataGenerator;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.apache.wicket.model.CompoundPropertyModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;


public class AssignmentFormPanelTest extends BaseSpringWebAppTester {
    private AssignmentAdminBackingBean backingBean;
    private CustomerService customerService;
    private ProjectAssignmentService assignmentService;
    private ProjectService projectService;

    @Before
    public void setup() {
        backingBean = new AssignmentAdminBackingBean();
        backingBean.setProjectAssignment(DummyUIDataGenerator.getProjectAssignment(1));

        assignmentService = createMock(ProjectAssignmentService.class);
        getMockContext().putBean(assignmentService);

        customerService = createMock(CustomerService.class);
        getMockContext().putBean(customerService);

        projectService = createMock(ProjectService.class);
        getMockContext().putBean(projectService);
    }

    @Test
    public void should_render() {
        expect(assignmentService.getProjectAssignmentTypes())
                .andReturn(DummyUIDataGenerator.getProjectAssignmentTypes());

        List<Customer> customers = new ArrayList<Customer>();
        customers.add(DummyUIDataGenerator.getCustomer(1));

        expect(customerService.getActiveCustomers()).andReturn(customers);

        expect(projectService.getActiveProjects()).andReturn(Arrays.asList(ProjectObjectMother.createProject(1)));
        expectLastCall().anyTimes();

        replay(customerService, assignmentService, projectService);
        startPanel();

        tester.assertNoErrorMessage();

        verify(customerService, assignmentService);
    }

    private void startPanel() {
        tester.startComponentInPage(new AssignmentFormPanel("id", new CompoundPropertyModel<AssignmentAdminBackingBean>(backingBean)));
    }
}