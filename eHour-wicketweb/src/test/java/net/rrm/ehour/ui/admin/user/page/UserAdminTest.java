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

package net.rrm.ehour.ui.admin.user.page;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;

public class UserAdminTest extends AbstractSpringWebAppTester
{
	/**
	 * Test render
	 */
	@Test
	public void testUserAdminRender()
	{
		UserService userService = createMock(UserService.class);
		getMockContext().putBean("userService", userService);
		
		List<User>	users = new ArrayList<User>();
		User user = new User();
		user.setFirstName("thies");
		user.setUserId(1);
		user.setLastName("Edeling");
		users.add(user);
		
		expect(userService.getActiveUsers())
			.andReturn(users);

		expect(userService.getUserRoles())
				.andReturn(new ArrayList<UserRole>());

		expect(userService.getUserDepartments())
			.andReturn(new ArrayList<UserDepartment>());

		replay(userService);
		
		getTester().startPage(UserAdmin.class);
		getTester().assertRenderedPage(UserAdmin.class);
		getTester().assertNoErrorMessage();
		
		verify(userService);
	}
}
