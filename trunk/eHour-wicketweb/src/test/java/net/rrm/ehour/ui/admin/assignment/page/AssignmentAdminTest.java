/**
 * Created on Aug 22, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.admin.assignment.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Test;


/**
 * TODO 
 **/

public class AssignmentAdminTest extends AbstractSpringWebAppTester
{
	/**
	 * Test render
	 */
	@Test
	public void testAssignmentAdminRender()
	{
		UserService userService = createMock(UserService.class);
		getMockContext().putBean("userService", userService);

		ProjectService projectService = createMock(ProjectService.class);
		getMockContext().putBean("projectService", projectService);
		
		CustomerService customerService = createMock(CustomerService.class);
		getMockContext().putBean("customerService", customerService);

		ProjectAssignmentService assignmentService = createMock(ProjectAssignmentService.class);
		getMockContext().putBean("assignmentService", assignmentService);

//		expect(projectService.getAllProjectsForUser(66))
//				.andReturn(new ArrayList<ProjectAssignment>());

//		replay(projectService);
		
		expect(assignmentService.getProjectAssignmentTypes())
				.andReturn(new ArrayList<ProjectAssignmentType>());
		
		replay(assignmentService);
		
		Customer cust = new Customer(22);
		List<Customer>	custs = new ArrayList<Customer>();
		custs.add(cust);
		
		expect(customerService.getCustomers(true))
				.andReturn(custs);
		
		replay(customerService);
		
		expect(userService.getUsers(new UserRole(EhourConstants.ROLE_CONSULTANT)))
				.andReturn(new ArrayList<User>());

		replay(userService);
		
		getTester().startPage(AssignmentAdmin.class);
		getTester().assertRenderedPage(AssignmentAdmin.class);
		getTester().assertNoErrorMessage();
		
		verify(userService);
//		verify(customerService);
//		verify(assignmentService);
	}
}
