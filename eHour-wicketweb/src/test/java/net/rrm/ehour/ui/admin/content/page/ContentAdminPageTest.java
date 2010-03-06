package net.rrm.ehour.ui.admin.content.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.List;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.DomainMother;
import net.rrm.ehour.domain.MotherUtil;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignmentMother;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.dto.ProjectAssignmentCollection;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.admin.content.tree.AssigneeTreeNode;
import net.rrm.ehour.ui.admin.content.tree.TreeNodeEventType;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.common.event.AjaxEventHook;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.user.service.UserService;

import org.junit.Before;
import org.junit.Test;

/**
 * Created on Feb 6, 2010 9:55:35 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ContentAdminPageTest extends AbstractSpringWebAppTester
{
	private UserService userService;
	private CustomerService customerService;
	private ProjectService projectService;
	private List<Customer> customers;
	private AjaxEventHook hook;
	private ProjectAssignmentManagementService managementService;
	
	@Before
	public void setup()
	{
		managementService = createMock(ProjectAssignmentManagementService.class);
		mockContext.putBean("projectAssignmentManagementService", managementService);
		
		customerService = createMock(CustomerService.class);
		mockContext.putBean("customerService", customerService);
		
		projectService = createMock(ProjectService.class);
		mockContext.putBean("projectService", projectService);
		
		userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);
		
		customers = DomainMother.createCustomers();
		
		// I'm travelling!
		UserDepartment department = customers.get(0)
										.getProjects().iterator().next()
										.getProjectAssignments().iterator().next()
										.getUser()
										.getUserDepartment();
		
		List<UserDepartment> departments = MotherUtil.createMultiple(department);
		
		expect(userService.getUserDepartments())
			.andReturn(departments);
	
		expect(customerService.getCustomers())
			.andReturn(customers);
		
		hook = new AjaxEventHook();
		EventPublisher.listenerHook = hook;
	}
	
	@Test
	public void shouldRender()
	{
		replay(userService, customerService, projectService, managementService);
		
		tester.startPage(ContentAdminPage.class);
		tester.assertNoErrorMessage();
		
		verify(userService, customerService, projectService, managementService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void selectProjectLink() throws ObjectNotFoundException
	{
		Project project = customers.get(0).getProjects().iterator().next();
		
		expect(projectService.getProject(project.getPK()))
			.andReturn(project)
			.anyTimes()
			;
		
		ProjectAssignmentCollection assignmentCollection = new ProjectAssignmentCollection();
		assignmentCollection.addProjectAssignment(ProjectAssignmentMother.createProjectAssignment(1));
		
		expect(managementService.getCombinedProjectAssignments(project.getPK()))
			.andReturn(assignmentCollection);
		
		replay(userService, customerService, projectService, managementService);
		
		tester.startPage(ContentAdminPage.class);
		
		tester.clickLink("assignables:tree:i:1:junctionLink", true);
		tester.clickLink("assignables:tree:i:2:nodeComponent:selectNode", true);
		
		tester.assertNoErrorMessage();
		
		verify(userService, customerService, projectService, managementService);
		
		assertEquals(1, hook.events.size());
		assertEquals(TreeNodeEventType.NODE_SELECTED, hook.events.get(0).getEventType());
		
		PayloadAjaxEvent<AssigneeTreeNode> event = (PayloadAjaxEvent<AssigneeTreeNode>) hook.events.get(0);
		
		assertEquals(1, ((Project)event.getPayload().getUserObject()).getPK().intValue());
	}	
}
