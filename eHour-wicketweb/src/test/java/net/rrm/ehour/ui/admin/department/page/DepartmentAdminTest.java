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

package net.rrm.ehour.ui.admin.department.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;

import org.junit.Before;
import org.junit.Test;

public class DepartmentAdminTest extends AbstractSpringWebAppTester
{
	private UserService	userService;
	
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		
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
