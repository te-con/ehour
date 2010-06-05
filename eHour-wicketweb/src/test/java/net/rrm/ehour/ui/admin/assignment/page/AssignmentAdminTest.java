/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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

import org.junit.Test;


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
		
		expect(userService.getUsers(UserRole.CONSULTANT))
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
