/**
 * Created on Aug 22, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.page.admin.assignment;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.common.BaseUITest;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.EhourConstants;


/**
 * TODO 
 **/

public class AssignmentAdminTest extends BaseUITest
{
	/**
	 * Test render
	 */
	public void testAssignmentAdminRender()
	{
		UserService userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);

		ProjectService projectService = createMock(ProjectService.class);
		mockContext.putBean("projectService", projectService);
		
		CustomerService customerService = createMock(CustomerService.class);
		mockContext.putBean("customerService", customerService);

		ProjectAssignmentService assignmentService = createMock(ProjectAssignmentService.class);
		mockContext.putBean("assignmentService", assignmentService);

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
		
		tester.startPage(AssignmentAdmin.class);
		tester.assertRenderedPage(AssignmentAdmin.class);
		tester.assertNoErrorMessage();
		
		verify(userService);
//		verify(customerService);
//		verify(assignmentService);
	}
}
