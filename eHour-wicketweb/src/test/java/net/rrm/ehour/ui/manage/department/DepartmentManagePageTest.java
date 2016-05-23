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

package net.rrm.ehour.ui.manage.department;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class DepartmentManagePageTest extends BaseSpringWebAppTester
{
	private UserService	userService;
	
	@Before
	public void before() throws Exception
	{
		userService = mock(UserService.class);
		getMockContext().putBean("userService", userService);

		List<UserDepartment> depts = new ArrayList<>();
		depts.add(new UserDepartment(1, "user", "DPT"));
		
		when(userService.getUserDepartments()).thenReturn(depts);
	}
	
	/**
	 * 
	 */
	@Test
	public void testDepartmentAdminRender()
	{
		tester.startPage(DepartmentManagePage.class);
		tester.assertRenderedPage(DepartmentManagePage.class);
		tester.assertNoErrorMessage();
	}
	
	@Test
	public void testEditTabClick()
	{
		tester.startPage(DepartmentManagePage.class);

		tester.clickLink("tabs:tabs-container:tabs:1:link", true);

		tester.assertRenderedPage(DepartmentManagePage.class);
		tester.assertNoErrorMessage();
	}
	
	@Test
	public void testSelectDepartment() throws ObjectNotFoundException
	{
		when(userService.getUserDepartment(1)).thenReturn(new UserDepartment(1, "user", "DPT"));

		tester.startPage(DepartmentManagePage.class);
		tester.assertRenderedPage(DepartmentManagePage.class);
		tester.assertNoErrorMessage();

        tester.executeAjaxEvent("entrySelectorFrame:entrySelectorFrame_body:deptSelector:entrySelectorFrame:blueBorder:blueBorder_body:listScroll:itemList:0", "click");
		verify(userService).getUserDepartment(1);
	}	
}
