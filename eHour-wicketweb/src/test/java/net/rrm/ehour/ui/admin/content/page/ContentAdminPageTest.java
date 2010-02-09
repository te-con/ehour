package net.rrm.ehour.ui.admin.content.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.List;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.DomainMother;
import net.rrm.ehour.domain.MotherUtil;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.Component;
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
	
	@Before
	public void setup()
	{
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
	}
	
	@Test
	public void shouldRender()
	{
		replay(userService, customerService, projectService);
		
		tester.startPage(ContentAdminPage.class);
		tester.assertNoErrorMessage();
		
		verify(userService, customerService, projectService);
	}
	
	@Test
	public void selectProjectLink() throws ObjectNotFoundException
	{
		Project project = customers.get(0).getProjects().iterator().next();
		
		expect(projectService.getProject(project.getPK()))
			.andReturn(project)
			.anyTimes()
			;
		
		replay(userService, customerService, projectService);
		
		tester.startPage(ContentAdminPage.class);
		
		tester.clickLink("assignables:tree:i:1:junctionLink", true);
		
		tester.clickLink("assignables:tree:i:2:nodeComponent:selectNode", true);
		
		tester.assertNoErrorMessage();
		
		verify(userService, customerService, projectService);
	}	
}
