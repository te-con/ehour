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
import net.rrm.ehour.ui.admin.project.page.ProjectAdmin;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
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
		getMockContext().putBean("projectService", projectService);

		UserService		userService = createMock(UserService.class);
		getMockContext().putBean("userService", userService);

		CustomerService	customerService = createMock(CustomerService.class);
		getMockContext().putBean("customerService", customerService);
		

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
