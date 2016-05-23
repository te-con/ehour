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

package net.rrm.ehour.ui.manage.user;

import com.google.common.collect.Sets;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserManagePageTest extends BaseSpringWebAppTester {
    @Mock
    private UserService userService;
    private User user;

    @Override
    protected void afterSetup() {
        // dont start it
    }

    @Before
    public void setup_userservice() throws Exception {
        super.setUp();
        getMockContext().putBean("userService", userService);

        List<User> users = new ArrayList<>();
        user = new User();
        user.setFirstName("thies");
        user.setUserId(1);
        user.setLastName("Edeling");
        user.setUserRoles(Sets.newHashSet(UserRole.ADMIN));
        users.add(user);

        when(userService.getActiveUsers()).thenReturn(users);
        when(userService.getUserRoles()).thenReturn(new ArrayList<UserRole>());
        when(userService.getUserDepartments()).thenReturn(new ArrayList<UserDepartment>());
    }

    @Test
    public void should_render() {
        super.startTester();

        tester.startPage(UserManagePage.class);
        tester.assertRenderedPage(UserManagePage.class);
        tester.assertNoErrorMessage();
    }

    @Test
    public void use_read_only_when_manager_views_admin() throws ObjectNotFoundException {
        getConfig().setSplitAdminRole(true);
        when(userService.getUserAndCheckDeletability(1)).thenReturn(user);

        webApp.setAuthorizedRoles(new Roles(UserRole.ROLE_MANAGER));
        super.startTester();

        tester.startPage(UserManagePage.class);

        tester.executeAjaxEvent("userSelection:border:border_body:entrySelectorFrame:entrySelectorFrame:blueBorder:blueBorder_body:listScroll:itemList:0", "click");

        tester.assertComponent("tabs:panel", UserManageReadOnlyPanel.class);
    }

    @Test
    public void use_edit_when_manager_views_non_admins() throws ObjectNotFoundException {
        getConfig().setSplitAdminRole(true);
        user.setUserRoles(Sets.newHashSet(UserRole.USER));
        when(userService.getUserAndCheckDeletability(1)).thenReturn(user);

        webApp.setAuthorizedRoles(new Roles(UserRole.ROLE_MANAGER));
        super.startTester();

        tester.startPage(UserManagePage.class);

        tester.executeAjaxEvent("userSelection:border:border_body:entrySelectorFrame:entrySelectorFrame:blueBorder:blueBorder_body:listScroll:itemList:0", "click");

        tester.assertComponent("tabs:panel", UserFormPanel.class);
    }
}
