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
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ManageUserPageTest extends BaseSpringWebAppTester {
    private User user;

    @Override
    protected void afterSetup() {
        // dont start it
    }

    @Before
    public void setup_userservice() throws Exception {
        super.setUp();

        List<User> users = new ArrayList<User>();
        user = new User();
        user.setUserId(1);
        user.setName("Edeling");
        user.setUserRoles(Sets.newHashSet(UserRole.ADMIN));
        users.add(user);

        when(userService.getUsers()).thenReturn(users);
        when(userService.getUserRoles()).thenReturn(new ArrayList<UserRole>());
    }

    @Test
    public void should_render() {
        super.startTester();

        tester.startPage(ManageUserPage.class);
        tester.assertRenderedPage(ManageUserPage.class);
        tester.assertNoErrorMessage();
    }
}
