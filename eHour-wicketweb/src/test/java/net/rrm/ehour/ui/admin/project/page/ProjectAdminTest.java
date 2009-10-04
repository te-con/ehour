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
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.DummUIDataGenerator;
import net.rrm.ehour.ui.admin.project.dto.ProjectAdminBackingBeanImpl;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.Component;
import org.junit.Before;
import org.junit.Test;


/**
 * Render testcase for project admin page
 **/

public class ProjectAdminTest extends AbstractSpringWebAppTester
{
	private ProjectService projectService;
	private UserService userService;
	private CustomerService customerService;
	
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		
		projectService = createMock(ProjectService.class);
		getMockContext().putBean("projectService", projectService);

		userService = createMock(UserService.class);
		getMockContext().putBean("userService", userService);

		customerService = createMock(CustomerService.class);
		getMockContext().putBean("customerService", customerService);	
	}

	@Test
	public void shouldRender()
	{
		expect(customerService.getCustomers(true))
				.andReturn(new ArrayList<Customer>());
		
		expect(userService.getUsersWithEmailSet())
				.andReturn(new ArrayList<User>());

		expect(projectService.getAllProjects(true))
				.andReturn(new ArrayList<Project>());
		
		replay(projectService, userService, customerService);
		
		getTester().startPage(ProjectAdmin.class);
		getTester().assertRenderedPage(ProjectAdmin.class);
		getTester().assertNoErrorMessage();
		
		verify(projectService, userService, customerService);
	}
	
//	@Test
//	public void removeEditTab()
//	{
//		expect(customerService.getCustomers(true))
//				.andReturn(new ArrayList<Customer>())
//				.times(2);
//		
//		expect(userService.getUsersWithEmailSet())
//				.andReturn(new ArrayList<User>())
//				.times(2);
//
//		List<Project> projects = new ArrayList<Project>();
//		projects.add(DummUIDataGenerator.createProject());
//
//		expect(projectService.getAllProjects(true))
//				.andReturn(projects)
//				.times(2);
//		
//		replay(projectService, userService, customerService);
//		
//		getTester().startPage(ProjectAdmin.class);
//		getTester().assertRenderedPage(ProjectAdmin.class);
//		getTester().assertNoErrorMessage();
//		
////		getTester().clickLink("entrySelectorFrame:projectSelector", true);
//		
//		Component itemList = getTester().getComponentFromLastRenderedPage("entrySelectorFrame:projectSelector:itemListHolder");
//		
//		AddEditTabbedPanel tabs = (AddEditTabbedPanel)getTester().getComponentFromLastRenderedPage("tabs");
//		tabs.setEditBackingBean(new ProjectAdminBackingBeanImpl(new Project()));
//		getTester().clickLink("tabs:tabs-container:tabs:1:link", true);
//		
//		assertEquals(3, tabs.getTabs().size());
//		
////		getTabbedPanel().setEditBackingBean(new ProjectAdminBackingBeanImpl(projectService.getProjectAndCheckDeletability(projectId)));
//		
//		getTester().clickLink("tabs:tabs-container:tabs:0:link", true);
//		assertEquals(1, tabs.getTabs().size());
//		
//		verify(projectService, userService, customerService);
//	}
	
}
