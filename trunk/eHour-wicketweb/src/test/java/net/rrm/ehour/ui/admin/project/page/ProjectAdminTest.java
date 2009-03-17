/**
 * Created on Aug 20, 2007
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

package net.rrm.ehour.ui.admin.project.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.project.page.ProjectAdmin;
import net.rrm.ehour.user.service.UserService;

import org.junit.Test;


/**
 * Render testcase for project admin page
 **/

public class ProjectAdminTest extends AbstractSpringWebAppTester
{
	/**
	 * Test render
	 */
	@Test
	public void testProjectAdminRender()
	{
		ProjectService	projectService = createMock(ProjectService.class);
		mockContext.putBean("projectService", projectService);

		UserService		userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);

		CustomerService	customerService = createMock(CustomerService.class);
		mockContext.putBean("customerService", customerService);
		

		expect(customerService.getCustomers(true))
				.andReturn(new ArrayList<Customer>());
		
		expect(userService.getUsersWithEmailSet())
				.andReturn(new ArrayList<User>());

		expect(projectService.getAllProjects(true))
				.andReturn(new ArrayList<Project>());
		
		replay(projectService);
		
		getTester().startPage(ProjectAdmin.class);
		getTester().assertRenderedPage(ProjectAdmin.class);
		getTester().assertNoErrorMessage();
		
		verify(projectService);
	}
}
