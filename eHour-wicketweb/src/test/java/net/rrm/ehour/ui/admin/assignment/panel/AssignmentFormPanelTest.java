package net.rrm.ehour.ui.admin.assignment.panel;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.DummyUIDataGenerator;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBeanImpl;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;


public class AssignmentFormPanelTest extends AbstractSpringWebAppTester
{
	private AssignmentAdminBackingBean backingBean;
	private CustomerService customerService;
	private ProjectAssignmentService assignmentService;
	private ProjectAssignmentManagementService managementService;
	
	@Before
	public void setup()
	{
		backingBean = new AssignmentAdminBackingBeanImpl();
		backingBean.setProjectAssignment(DummyUIDataGenerator.getProjectAssignment(1));

		assignmentService = createMock(ProjectAssignmentService.class);
		mockContext.putBean(assignmentService);
		
		managementService = createMock(ProjectAssignmentManagementService.class);
		mockContext.putBean(managementService);
		
		customerService = createMock(CustomerService.class);
		mockContext.putBean(customerService);
		
	}
	
	@Test
	public void shouldRenderInitially()
	{
		expect(assignmentService.getProjectAssignmentTypes())
			.andReturn(DummyUIDataGenerator.getProjectAssignmentTypes());
		
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(DummyUIDataGenerator.getCustomer(1));
		
		expect(customerService.getCustomers(true))
			.andReturn(customers);
		
		replay(customerService, assignmentService, managementService);
//		replay(getConfigService());
		startPanel();
		
		tester.assertNoErrorMessage();
		
		verify(customerService, assignmentService, managementService);
	}
	
	@SuppressWarnings("serial")
	private void startPanel()
	{
		tester.startPanel(new TestPanelSource()
		{
			
			public Panel getTestPanel(String panelId)
			{
				return new AssignmentFormPanel(panelId, new CompoundPropertyModel(backingBean));
			}
		});
	}
}
