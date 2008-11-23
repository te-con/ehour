/**
 * Created on Aug 13, 2007
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

package net.rrm.ehour.ui.admin.user.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.page.admin.user.UserAdmin;
import net.rrm.ehour.user.service.UserService;

import org.junit.Test;


/**
 * TODO 
 **/

public class UserAdminTest extends BaseUIWicketTester
{
	/**
	 * Test render
	 */
	@Test
	public void testUserAdminRender()
	{
		UserService userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);
		
		List<User>	users = new ArrayList<User>();
		User user = new User();
		user.setFirstName("thies");
		user.setUserId(1);
		user.setLastName("Edeling");
		users.add(user);
		
		expect(userService.getUsers())
			.andReturn(users);

		expect(userService.getUserRoles())
				.andReturn(new ArrayList<UserRole>());

		expect(userService.getUserDepartments())
			.andReturn(new ArrayList<UserDepartment>());

		replay(userService);
		
		tester.startPage(UserAdmin.class);
		tester.assertRenderedPage(UserAdmin.class);
		tester.assertNoErrorMessage();
		
		verify(userService);
	}
}
