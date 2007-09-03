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

package net.rrm.ehour.ui.page.admin.department;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;

import net.rrm.ehour.ui.common.BaseUITest;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.service.UserService;


/**
 * TODO 
 **/

public class DepartmentAdminTest extends BaseUITest
{
	/**
	 * Test render
	 */
	public void testDepartmentAdminRender()
	{
		UserService userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);
		
		expect(userService.getUserDepartments())
			.andReturn(new ArrayList<UserDepartment>());

		replay(userService);
		
		tester.startPage(DepartmentAdmin.class);
		tester.assertRenderedPage(DepartmentAdmin.class);
		tester.assertNoErrorMessage();
		
		verify(userService);
	}
}
