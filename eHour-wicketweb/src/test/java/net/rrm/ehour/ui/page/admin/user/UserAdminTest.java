/**
 * Created on Aug 13, 2007
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

package net.rrm.ehour.ui.page.admin.user;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.ui.common.BaseUITest;
import net.rrm.ehour.ui.page.admin.mainconfig.MainConfig;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.service.UserService;


/**
 * TODO 
 **/

public class UserAdminTest  extends BaseUITest
{
	/**
	 * Test render
	 */
	public void testMainConfigRender()
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

		replay(userService);
		
		tester.startPage(UserAdmin.class);
		tester.assertRenderedPage(UserAdmin.class);
		tester.assertNoErrorMessage();
		
		verify(userService);
	}
}
