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

package net.rrm.ehour.user.service;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserObjectMother;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.persistence.activity.dao.ActivityDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserRoleDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserDao userDAO;
    private UserRoleDao userRoleDAO;
    private ActivityDao activityDao;

    @Before
    public void setUp() {
        userService = new UserServiceImpl();
        userDAO = createMock(UserDao.class);
        userRoleDAO = createMock(UserRoleDao.class);
        activityDao = createMock(ActivityDao.class);

        userService.setUserDAO(userDAO);
        userService.setUserRoleDAO(userRoleDAO);
        userService.setActivityDao(activityDao);
    }

    @Test
    public void shouldGetActiveUsers() {
        expect(userDAO.findActiveUsers()).andReturn(new ArrayList<User>());

        replay(userDAO);

        userService.getUsers();

        verify(userDAO);
    }

    @Test
    public void testGetUserRoles() {
        List<UserRole> userRoles = userService.getUserRoles();

        assertEquals(UserRole.ROLES.size(), userRoles.size());
    }


    @Test
    public void shouldCreateNewUser() throws ObjectNotUniqueException {
        User user = UserObjectMother.createUser();

        expect(userDAO.persist(user)).andReturn(user);

        replay(userDAO);

        userService.editUser(user);

        verify(userDAO);
    }

    @Test
    public void shouldAddCustomerReviewerRoleCorrectly() {
        User earlierPersistedUser = new User();
        earlierPersistedUser.setUserId(1);

        expect(userDAO.findById(1)).andReturn(earlierPersistedUser);

        earlierPersistedUser.addUserRole(UserRole.CUSTOMERREVIEWER);

        expect(userDAO.persist(earlierPersistedUser)).andReturn(earlierPersistedUser);

        expect (userRoleDAO.findById(UserRole.CUSTOMERREVIEWER.getRole())).andReturn(UserRole.CUSTOMERREVIEWER);

        userDAO.cleanRedundantRoleInformation(UserRole.CUSTOMERREVIEWER);

        replay(userDAO);
        replay(userRoleDAO);

        User updatedUser = userService.addRole(1, UserRole.CUSTOMERREVIEWER);

        verify(userDAO);
        verify(userRoleDAO);

        Set<UserRole> userRoles = updatedUser.getUserRoles();
        assertNotNull(userRoles);
        assertEquals(1, userRoles.size());
        Assert.assertTrue(userRoles.contains(UserRole.CUSTOMERREVIEWER));
    }
}
