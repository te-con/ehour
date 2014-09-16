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

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.persistence.activity.dao.ActivityDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.persistence.user.dao.UserRoleDao;
import org.easymock.Capture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserDao userDAO;
    private UserDepartmentDao userDepartmentDAO;
    private UserRoleDao userRoleDAO;
    private ActivityDao activityDao;

    @Before
    public void setUp() {
        userService = new UserServiceImpl();
        userDAO = createMock(UserDao.class);
        userDepartmentDAO = createMock(UserDepartmentDao.class);
        userRoleDAO = createMock(UserRoleDao.class);
        activityDao = createMock(ActivityDao.class);

        userService.setUserDAO(userDAO);
        userService.setUserDepartmentDAO(userDepartmentDAO);
        userService.setUserRoleDAO(userRoleDAO);
        userService.setActivityDao(activityDao);

        userService.setPasswordEncoder(new ShaPasswordEncoder(1));
    }

    @Test
    public void shouldGetActiveUsers() {
        expect(userDAO.findActiveUsers()).andReturn(new ArrayList<User>());

        replay(userDAO);

        userService.getActiveUsers();

        verify(userDAO);
    }

    @Test
    public void testGetUser() throws ObjectNotFoundException {
        User user;
        Activity activityA, activityB;
        Project projectA, projectB;
        Set<Activity> activities = new HashSet<Activity>();
        Calendar calA, calB;


        user = new User("thies", "pwd");

        projectA = new Project();
        projectA.setActive(true);
        activityA = new Activity();
        activityA.setId(1);
        calA = new GregorianCalendar();
        calA.add(Calendar.MONTH, -5);
        activityA.setDateStart(calA.getTime());
        calA.add(Calendar.MONTH, 1);
        activityA.setDateEnd(calA.getTime());
        activityA.setProject(projectA);
        activities.add(activityA);

        projectB = new Project();
        projectB.setActive(true);

        activityB = new Activity();
        activityB.setId(2);
        calB = new GregorianCalendar();
        calB.add(Calendar.MONTH, -2);
        activityB.setDateStart(calB.getTime());
        calB = new GregorianCalendar();
        calB.add(Calendar.MONTH, 1);
        activityB.setDateEnd(calB.getTime());
        activityB.setProject(projectB);
        activities.add(activityB);

        user.setActivities(activities);

        expect(userDAO.findById(1))
                .andReturn(user);

        replay(userDAO);

        user = userService.getUser(1);

        verify(userDAO);

        assertEquals("thies", user.getUsername());
        assertEquals(2, user.getActivities().size());
    }

    @Test
    public void testGetUserDepartment() throws ObjectNotFoundException {
        UserDepartment ud;

        expect(userDepartmentDAO.findById(1))
                .andReturn(new UserDepartment(1, "bla", "ble"));

        replay(userDepartmentDAO);

        ud = userService.getUserDepartment(1);

        verify(userDepartmentDAO);

        assertEquals("bla", ud.getName());
    }

    @Test
    public void testGetUserRoles() {
        List<UserRole> userRoles = userService.getUserRoles();

        assertEquals(UserRole.ROLES.size(), userRoles.size());
    }

    @Test
    public void testAddAndcheckProjectManagementRoles() {
        User user = new User(1);
        user.setPassword("aa");
        user.setSalt(2);
        user.setUsername("user");

        expect(userDAO.findById(1))
                .andReturn(user);

        expect(userDAO.persist(user))
                .andReturn(user);

        userDAO.deletePmWithoutProject();

        replay(userDAO);

        userService.validateProjectManagementRoles(1);

        verify(userDAO);

        assertEquals("aa", user.getPassword());
    }

    @Test
    public void shouldUpdatePassword() throws ObjectNotUniqueException {
        Capture<User> capturer = new Capture<User>();

        User user = new User(1);
        user.setPassword("aa");
        user.setUsername("user");

        expect(userDAO.findByUsername("user")).andReturn(user);
        expect(userDAO.persist(and(isA(User.class), capture(capturer)))).andReturn(user);

        replay(userDAO);

        userService.changePassword("user", "pwd");

        verify(userDAO);

        assertFalse(user.getPassword().equals("pwd"));
    }

    @Test
    public void shouldCreateNewUser() throws ObjectNotUniqueException {
        User user = UserObjectMother.createUser();

        expect(userDAO.findByUsername(user.getUsername())).andReturn(null);
        expect(userDAO.persist(user)).andReturn(user);

        replay(userDAO);

        userService.persistNewUser(user, "password");

        verify(userDAO);

        assertNotSame("password", user.getPassword());
    }

    @Test
    public void shouldChangeUsername() throws ObjectNotUniqueException {
        User user = UserObjectMother.createUser();
        User persistedUser = new User();

        expect(userDAO.findByUsername(user.getUsername())).andReturn(null);
        expect(userDAO.findById(user.getUserId())).andReturn(persistedUser);
        expect(userDAO.persist(persistedUser)).andReturn(persistedUser);

        replay(userDAO);

        userService.persistEditedUser(user);

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

    @Test
    @Ignore
    public void shouldUpdateUserRoleAppropriatelyIfAssociatedToCustomer()
            throws ObjectNotUniqueException {
        User user = new User(1);
        user.setPassword("aa");
        user.setUpdatedPassword("aa");
        user.setSalt(2);
        user.setUsername("user");

        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setActive(true);
        Set<Customer> associatedCustomers = new HashSet<Customer>();
        associatedCustomers.add(customer);
        user.setCustomers(associatedCustomers);

        expect(userDAO.findByUsername(user.getUsername())).andReturn(null);

        expect(userRoleDAO.findById(UserRole.ROLE_CUSTOMERREVIEWER)).andReturn(
                UserRole.CUSTOMERREVIEWER);

        expect(userDAO.merge(user)).andReturn(user);

        replay(userDAO);

        replay(userRoleDAO);

        User persistedUser = userService.persistEditedUser(user);

        verify(userDAO);
        verify(userRoleDAO);

        assertNotNull(persistedUser);
        assertEquals(1, persistedUser.getCustomers().size());
    }

    @Test
    @Ignore
    public void shouldNotUpdateUserRoleIfNotAssociatedToCustomer() throws ObjectNotUniqueException {
        User user = new User(1);
        user.setPassword("aa");
        user.setUpdatedPassword("aa");
        user.setSalt(2);
        user.setUsername("user");

        expect(userDAO.findByUsername(user.getUsername())).andReturn(null);

        expect(userDAO.merge(user)).andReturn(user);

        replay(userDAO);

        replay(userRoleDAO);

        User persistedUser = userService.persistEditedUser(user);

        verify(userDAO);
        verify(userRoleDAO);

        assertNotNull(persistedUser);
        assertEquals(0, persistedUser.getCustomers().size());
    }

    @Test
    public void testShouldReturnNullWhenNoUsersAssociatedWithCustomers() {
        List<Customer> customers = new ArrayList<Customer>();

        expect(activityDao.findActivitiesForCustomers(customers)).andReturn(Lists.<Activity>newArrayList());

        replay(activityDao);

        List<User> allUsersAssignedToCustomers = userService.getAllUsersAssignedToCustomers(customers, true);

        verify(activityDao);

        assertEquals(0, allUsersAssignedToCustomers.size());
    }

    @Test
    public void testShouldReturnCorrectlyWhenUsersAssociatedWithCustomers() {
        List<Customer> customers = new ArrayList<Customer>();

        List<Activity> activities = new ArrayList<Activity>();

        Activity activity1 = new Activity();
        User user1  = new User();
        user1.setUsername("user1");
        activity1.setAssignedUser(user1);

        Activity activity2 = new Activity();
        User user2  = new User();
        user2.setUsername("user2");
        activity2.setAssignedUser(user2);

        activities.add(activity1);
        activities.add(activity2);

        expect(activityDao.findActivitiesForCustomers(customers)).andReturn(activities);

        replay(activityDao);

        List<User> allUsersAssignedToCustomers = userService.getAllUsersAssignedToCustomers(customers, true);

        verify(activityDao);

        assertNotNull(allUsersAssignedToCustomers);
        assertEquals(2, allUsersAssignedToCustomers.size());
    }
}
