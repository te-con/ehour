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

import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.persistence.user.dao.UserRoleDao;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserDao userDAO;
    private UserDepartmentDao userDepartmentDAO;
    private ProjectAssignmentManagementService assignmentService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl();
        userDAO = mock(UserDao.class);
        userDepartmentDAO = mock(UserDepartmentDao.class);
        UserRoleDao userRoleDAO = mock(UserRoleDao.class);
        assignmentService = mock(ProjectAssignmentManagementService.class);


        userService.setUserDAO(userDAO);
        userService.setUserDepartmentDAO(userDepartmentDAO);
        userService.setUserRoleDAO(userRoleDAO);
        userService.setProjectAssignmentManagementService(assignmentService);

        userService.setPasswordEncoder(new ShaPasswordEncoder(1));
    }

    @Test
    public void shouldGetActiveUsers() {
        when(userDAO.findActiveUsers()).thenReturn(new ArrayList<User>());

        userService.getActiveUsers();

        verify(userDAO).findActiveUsers();
    }

    @Test
    public void testGetUser() throws ObjectNotFoundException {
        User user;
        ProjectAssignment assignmentA, assignmentB;
        Project projectA, projectB;
        Set<ProjectAssignment> assignments = new HashSet<>();
        Calendar calA, calB;


        user = new User("thies", "pwd");

        projectA = new Project();
        projectA.setActive(true);
        assignmentA = new ProjectAssignment();
        assignmentA.setAssignmentId(1);
        assignmentA.setAssignmentType(new ProjectAssignmentType(0));
        calA = new GregorianCalendar();
        calA.add(Calendar.MONTH, -5);
        assignmentA.setDateStart(calA.getTime());
        calA.add(Calendar.MONTH, 1);
        assignmentA.setDateEnd(calA.getTime());
        assignmentA.setProject(projectA);
        assignments.add(assignmentA);

        projectB = new Project();
        projectB.setActive(true);

        assignmentB = new ProjectAssignment();
        assignmentB.setAssignmentId(2);
        assignmentB.setAssignmentType(new ProjectAssignmentType(0));
        calB = new GregorianCalendar();
        calB.add(Calendar.MONTH, -2);
        assignmentB.setDateStart(calB.getTime());
        calB = new GregorianCalendar();
        calB.add(Calendar.MONTH, 1);
        assignmentB.setDateEnd(calB.getTime());
        assignmentB.setProject(projectB);
        assignments.add(assignmentB);

        user.setProjectAssignments(assignments);

        when(userDAO.findById(1))
                .thenReturn(user);

        user = userService.getUser(1);

        verify(userDAO).findById(1);

        assertEquals("thies", user.getUsername());
        assertEquals(1, user.getProjectAssignments().size());
        assertEquals(1, user.getInactiveProjectAssignments().size());
    }

    @Test
    public void testGetUserDepartment() throws ObjectNotFoundException {
        UserDepartment ud;

        when(userDepartmentDAO.findById(1))
                .thenReturn(new UserDepartment(1, "bla", "ble"));

        ud = userService.getUserDepartment(1);

        verify(userDepartmentDAO).findById(1);

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

        when(userDAO.findById(1))
                .thenReturn(user);

        when(userDAO.persist(user))
                .thenReturn(user);

        userDAO.deletePmWithoutProject();

        userService.validateProjectManagementRoles(1);

        assertEquals("aa", user.getPassword());
    }

    @Test
    public void shouldUpdatePassword() throws ObjectNotUniqueException {
        User user = new User(1);
        user.setPassword("aa");
        user.setUsername("user");

        when(userDAO.findByUsername("user")).thenReturn(user);
        when(userDAO.persist(any(User.class))).thenReturn(user);

        userService.changePassword("user", "pwd");

        assertFalse(user.getPassword().equals("pwd"));
    }

    @Test
    public void shouldCreateNewUser() throws ObjectNotUniqueException {
        User user = UserObjectMother.createUser();

        when(userDAO.findByUsername(user.getUsername())).thenReturn(null);
        when(userDAO.persist(user)).thenReturn(user);
        when(assignmentService.assignUserToDefaultProjects(user)).thenReturn(user);

        userService.persistNewUser(user, "password");

        verify(assignmentService).assignUserToDefaultProjects(user);

        assertNotSame("password", user.getPassword());
    }

    @Test
    public void shouldChangeUsername() throws ObjectNotUniqueException {
        User user = UserObjectMother.createUser();
        User persistedUser = new User();

        when(userDAO.findByUsername(user.getUsername())).thenReturn(null);
        when(userDAO.findById(user.getUserId())).thenReturn(persistedUser);
        when(userDAO.persist(persistedUser)).thenReturn(persistedUser);

        userService.persistEditedUser(user);

        verify(userDAO).persist(persistedUser);
    }
}
