package net.rrm.ehour.ui.manage.assignment;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.ProjectAssignment;
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

import static org.mockito.Mockito.*;

public class AssignmentFormPanelTest extends BaseSpringWebAppTester {
    private AssignmentAdminBackingBean backingBean;
    private CustomerService customerService;
    private ProjectAssignmentService assignmentService;
    private ProjectService projectService;

    @Before
    public void setup() {
        backingBean = new AssignmentAdminBackingBean();
        backingBean.setProjectAssignment(DummyUIDataGenerator.getProjectAssignment(1));

        assignmentService = mock(ProjectAssignmentService.class);
        getMockContext().putBean(assignmentService);

        customerService = mock(CustomerService.class);
        getMockContext().putBean(customerService);

        projectService = mock(ProjectService.class);
        getMockContext().putBean(projectService);
    }

    @Test
    public void should_render() {
        when(assignmentService.getProjectAssignmentTypes())
                .thenReturn(DummyUIDataGenerator.getProjectAssignmentTypes());

        List<Customer> customers = new ArrayList<>();
        customers.add(DummyUIDataGenerator.getCustomer(1));

        when(customerService.getActiveCustomers()).thenReturn(customers);

        when(projectService.getActiveProjects()).thenReturn(Arrays.asList(ProjectObjectMother.createProject(1)));

        startPanel();

        tester.assertNoErrorMessage();

        verify(customerService).getActiveCustomers();
        verify(projectService, times(4)).getActiveProjects();
    }

    @Test
    public void should_display_warning_booking_disabled() {
        backingBean = new AssignmentAdminBackingBean();
        ProjectAssignment projectAssignment = DummyUIDataGenerator.getProjectAssignment(1);
        projectAssignment.getProject().setActive(false);

        backingBean.setProjectAssignment(projectAssignment);

        when(assignmentService.getProjectAssignmentTypes())
                .thenReturn(DummyUIDataGenerator.getProjectAssignmentTypes());

        List<Customer> customers = new ArrayList<>();
        customers.add(DummyUIDataGenerator.getCustomer(1));

        when(customerService.getActiveCustomers()).thenReturn(customers);

        when(projectService.getActiveProjects()).thenReturn(Arrays.asList(ProjectObjectMother.createProject(1)));

        startPanel();

        tester.assertNoErrorMessage();

        verify(customerService).getActiveCustomers();
    }

    private void startPanel() {
        tester.startComponentInPage(new AssignmentFormPanel("id", new CompoundPropertyModel<>(backingBean)));
    }
}