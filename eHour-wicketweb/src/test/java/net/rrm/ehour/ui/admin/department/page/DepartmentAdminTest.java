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

package net.rrm.ehour.ui.admin.department.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.service.exception.ObjectNotFoundException;
import net.rrm.ehour.service.user.service.UserService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.junit.Before;
import org.junit.Test;

public class DepartmentAdminTest extends AbstractSpringWebAppTester
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
		
		getTester().startPage(DepartmentAdmin.class);
		getTester().assertRenderedPage(DepartmentAdmin.class);
		getTester().assertNoErrorMessage();
		
		verify(userService);
	}
	
	@Test
	public void testEditTabClick()
	{
		replay(userService);

		getTester().startPage(DepartmentAdmin.class);
		getTester().assertRenderedPage(DepartmentAdmin.class);
		getTester().assertNoErrorMessage();
		
		getTester().clickLink("tabs:tabs-container:tabs:1:link", true);
		verify(userService);
	}
	
	@Test
	public void testSelectDepartment() throws ObjectNotFoundException
	{
		expect(userService.getUserDepartment(1))
			.andReturn(new UserDepartment(1, "user", "DPT"));	
		
		replay(userService);

		getTester().startPage(DepartmentAdmin.class);
		getTester().assertRenderedPage(DepartmentAdmin.class);
		getTester().assertNoErrorMessage();
		
		getTester().clickLink("entrySelectorFrame:deptSelector:entrySelectorFrame:blueBorder:itemListHolder:itemList:0:itemLink", true);
		verify(userService);
	}	
	
//	FIXME, https://issues.apache.org/jira/browse/WICKET-861?page=com.atlassian.jira.plugin.system.issuetabpanels:all-tabpanel
//	@Test
//	public void testAddDepartment() throws ObjectNotFoundException
//	{
//		replay(userService);
//
//		getTester().startPage(DepartmentAdmin.class);
//		getTester().assertRenderedPage(DepartmentAdmin.class);
//		getTester().assertNoErrorMessage();
//		
//		FormTester formTester = getTester().newFormTester("tabs:panel:border:deptForm");
//		formTester.setValue("department.name", "test");
//		formTester.submit();
//		
//		getTester().assertErrorMessages(new String[]{"bla"});
//		
//		
//		verify(userService);
//	}		
}
