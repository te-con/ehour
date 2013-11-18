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

package net.rrm.ehour.ui.admin.department;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;

public class DepartmentAdminPageTest extends BaseSpringWebAppTester
{
	private UserService	userService;
	
	@Before
	public void before() throws Exception
	{
		userService = createMock(UserService.class);
		getMockContext().putBean("userService", userService);

		List<UserDepartment> depts = new ArrayList<UserDepartment>();
		depts.add(new UserDepartment(1, "user", "DPT"));
		
		expect(userService.getUserDepartments()).andReturn(depts);
	}
	
	/**
	 * 
	 */
	@Test
	public void testDepartmentAdminRender()
	{
		replay(userService);
		
		tester.startPage(DepartmentAdminPage.class);
		tester.assertRenderedPage(DepartmentAdminPage.class);
		tester.assertNoErrorMessage();
		
		verify(userService);
	}
	
	@Test
	public void testEditTabClick()
	{
		replay(userService);

		tester.startPage(DepartmentAdminPage.class);
		tester.assertRenderedPage(DepartmentAdminPage.class);
		tester.assertNoErrorMessage();
		
		tester.clickLink("tabs:tabs-container:tabs:1:link", true);
		verify(userService);
	}
	
	@Test
	public void testSelectDepartment() throws ObjectNotFoundException
	{
		expect(userService.getUserDepartment(1))
			.andReturn(new UserDepartment(1, "user", "DPT"));	
		
		replay(userService);

		tester.startPage(DepartmentAdminPage.class);
		tester.assertRenderedPage(DepartmentAdminPage.class);
		tester.assertNoErrorMessage();

        tester.executeAjaxEvent("entrySelectorFrame:entrySelectorFrame_body:deptSelector:entrySelectorFrame:blueBorder:blueBorder_body:itemListHolder:itemList:0", "click");
		verify(userService);
	}	
}
