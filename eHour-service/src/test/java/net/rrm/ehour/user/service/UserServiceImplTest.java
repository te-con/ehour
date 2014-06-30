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
import org.easymock.Capture;
import org.junit.Before;
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
    private ProjectAssignmentManagementService assignmentService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl();
        userDAO = createMock(UserDao.class);
        userDepartmentDAO = createMock(UserDepartmentDao.class);
        userRoleDAO = createMock(UserRoleDao.class);
        assignmentService = createMock(ProjectAssignmentManagementService.class);


        userService.setUserDAO(userDAO);
        userService.setUserDepartmentDAO(userDepartmentDAO);
        userService.setUserRoleDAO(userRoleDAO);
        userService.setProjectAssignmentManagementService(assignmentService);

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
        ProjectAssignment assignmentA, assignmentB;
        Project projectA, projectB;
        Set<ProjectAssignment> assignments = new HashSet<ProjectAssignment>();
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

        expect(userDAO.findById(1))
                .andReturn(user);

        replay(userDAO);

        user = userService.getUser(1);

        verify(userDAO);

        assertEquals("thies", user.getUsername());
        assertEquals(1, user.getProjectAssignments().size());
        assertEquals(1, user.getInactiveProjectAssignments().size());
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
    public void testGetUserRole() {
        expect(userRoleDAO.findById(UserRole.ROLE_USER))
                .andReturn(UserRole.USER);

        replay(userRoleDAO);

        UserRole ur = userService.getUserRole(UserRole.ROLE_USER);

        verify(userRoleDAO);

        assertEquals(UserRole.ROLE_USER, ur.getRole());
    }

    @Test
    public void testGetUserRoles() {
        expect(userRoleDAO.findAll())
                .andReturn(new ArrayList<UserRole>());

        replay(userRoleDAO);

        userService.getUserRoles();

        verify(userRoleDAO);
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
        expect(assignmentService.assignUserToDefaultProjects(user)).andReturn(user);

        replay(userDAO, assignmentService);

        userService.newUser(user, "password");

        verify(userDAO, assignmentService);

        assertNotSame("password", user.getPassword());
    }

    @Test
    public void shouldChangeUsername() throws ObjectNotUniqueException {
        User user = UserObjectMother.createUser();
        User persistedUser = new User();

        expect(userDAO.findByUsername(user.getUsername())).andReturn(null);
        expect(userDAO.findById(user.getUserId())).andReturn(persistedUser);
        expect(userDAO.persist(persistedUser)).andReturn(persistedUser);

        replay(userDAO, assignmentService);

        userService.editUser(user);

        verify(userDAO, assignmentService);
    }
}
