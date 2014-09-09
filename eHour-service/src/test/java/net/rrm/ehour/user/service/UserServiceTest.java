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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.persistence.activity.dao.ActivityDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.persistence.user.dao.UserRoleDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

/**
 * @author Thies
 * 
 */
public class UserServiceTest {
	private UserService userService;

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

		ReflectionTestUtils.setField(userService, "userDAO", userDAO);

		ReflectionTestUtils.setField(userService, "userDepartmentDAO", userDepartmentDAO);

		ReflectionTestUtils.setField(userService, "userRoleDAO", userRoleDAO);

		ReflectionTestUtils.setField(userService, "passwordEncoder", new ShaPasswordEncoder(1));
		
		ReflectionTestUtils.setField(userService, "activityDao", activityDao);
	}

	@Test
	public void testGetUsersByNameMatch() {
		expect(userDAO.findUsersByNameMatch("test", true)).andReturn(
				new ArrayList<User>());

		replay(userDAO);

		userService.getUsersByNameMatch("test", true);

		verify(userDAO);
	}

	/**
	 * Test method for
	 * {@link net.rrm.ehour.persistence.persistence.user.service.UserServiceImpl#getUser(java.lang.Integer)}
	 * .
	 * 
	 * @throws ObjectNotFoundException
	 */
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

		expect(userDAO.findById(1)).andReturn(user);

		replay(userDAO);

		user = userService.getUser(new Integer(1));

		verify(userDAO);

		assertEquals("thies", user.getUsername());
		assertEquals(1, user.getProjectAssignments().size());
		assertEquals(1, user.getInactiveProjectAssignments().size());
	}

	/**
	 * @throws ObjectNotFoundException
	 * 
	 * 
	 */
	@Test
	public void testGetUserDepartment() throws ObjectNotFoundException {
		UserDepartment ud;

		expect(userDepartmentDAO.findById(1)).andReturn(
				new UserDepartment(new Integer(1), "bla", "ble"));

		replay(userDepartmentDAO);

		ud = userService.getUserDepartment(new Integer(1));

		verify(userDepartmentDAO);

		assertEquals("bla", ud.getName());
	}

	@Test
	public void testGetUserRole() {
		String role = "ROLE_CONSULTANT";
		expect(userRoleDAO.findById(role)).andReturn(new UserRole(role));

		replay(userRoleDAO);

		UserRole ur = userService.getUserRole(role);

		verify(userRoleDAO);

		assertEquals(role, ur.getRole());
	}

	@Test
	public void testGetUserRoles() {
		ArrayList<UserRole> allRoles = new ArrayList<UserRole>();
		allRoles.add(UserRole.CONSULTANT);
		
		allRoles.add(UserRole.CUSTOMERREPORTER);
		allRoles.add(UserRole.CUSTOMERREVIEWER);
		allRoles.add(UserRole.PROJECTMANAGER);
		
		expect(userRoleDAO.findAll()).andReturn(allRoles);

		replay(userRoleDAO);

		List<UserRole> assignableUserRoles = userService.getUserRoles();

		verify(userRoleDAO);

		assertNotNull(assignableUserRoles);
		assertEquals(1, assignableUserRoles.size());
		assertEquals(UserRole.CONSULTANT, assignableUserRoles.get(0));
	}

	@Test
	public void testAddAndcheckProjectManagementRoles() {
		User user = new User(1);
		user.setPassword("aa");
		user.setSalt(new Integer(2));
		user.setUsername("user");

		expect(userDAO.findById(new Integer(1))).andReturn(user);

		expect(userDAO.persist(user)).andReturn(user);

		userDAO.deletePmWithoutProject();

		replay(userDAO);

		userService.addAndcheckProjectManagementRoles(new Integer(1));

		verify(userDAO);

		assertEquals("aa", user.getPassword());
	}

	@Test
	public void testUpdatePassword() throws PasswordEmptyException,
			ObjectNotUniqueException {
		User user = new User(1);
		user.setPassword("aa");
		user.setSalt(new Integer(2));
		user.setUsername("user");
		user.setUpdatedPassword("fefe");

		expect(userDAO.findByUsername("user")).andReturn(user);

		expect(userDAO.merge(user)).andReturn(user);

		replay(userDAO);

		userService.persistUser(user);

		verify(userDAO);

		assertFalse(user.getPassword().equals("aa"));
	}

	@Test
	public void testShouldUpdateUserRoleAppropriatelyIfAssociatedToCustomer()
			throws PasswordEmptyException, ObjectNotUniqueException {
		User user = new User(1);
		user.setPassword("aa");
		user.setUpdatedPassword("aa");
		user.setSalt(new Integer(2));
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

		User persistedUser = userService.persistUser(user);

		verify(userDAO);
		verify(userRoleDAO);

		assertNotNull(persistedUser);
		assertEquals(1, persistedUser.getCustomers().size());
	}

	@Test
	public void testShouldNotUpdateUserRoleIfNotAssociatedToCustomer()
			throws PasswordEmptyException, ObjectNotUniqueException {
		User user = new User(1);
		user.setPassword("aa");
		user.setUpdatedPassword("aa");
		user.setSalt(new Integer(2));
		user.setUsername("user");

		expect(userDAO.findByUsername(user.getUsername())).andReturn(null);

		expect(userDAO.merge(user)).andReturn(user);

		replay(userDAO);

		replay(userRoleDAO);

		User persistedUser = userService.persistUser(user);

		verify(userDAO);
		verify(userRoleDAO);

		assertNotNull(persistedUser);
		assertEquals(0, persistedUser.getCustomers().size());
	}

	@Test
	public void testShouldAddCustomerReviewerRoleCorrectly() {
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
		Assert.assertNotNull(userRoles);
		Assert.assertEquals(1, userRoles.size());
		Assert.assertTrue(userRoles.contains(UserRole.CUSTOMERREVIEWER));
	}

	@Test
	public void testShouldAddCustomerReporterRoleCorrectly() {
		User earlierPersistedUser = new User();
		earlierPersistedUser.setUserId(1);
		
		expect(userDAO.findById(1)).andReturn(earlierPersistedUser);

		earlierPersistedUser.addUserRole(UserRole.CUSTOMERREPORTER);
		
		expect(userDAO.persist(earlierPersistedUser)).andReturn(earlierPersistedUser);
		
		expect (userRoleDAO.findById(UserRole.CUSTOMERREPORTER.getRole())).andReturn(UserRole.CUSTOMERREPORTER);
		
		replay(userDAO);
		replay(userRoleDAO);
		
		User updatedUser = userService.addRole(1, UserRole.CUSTOMERREPORTER);
		
		verify(userDAO);
		verify(userRoleDAO);
		
		Set<UserRole> userRoles = updatedUser.getUserRoles();
		Assert.assertNotNull(userRoles);
		Assert.assertEquals(1, userRoles.size());
		Assert.assertTrue(userRoles.contains(UserRole.CUSTOMERREPORTER));
	}

	@Test
	public void testShouldReturnEmptySetWhenNoUsersAssociatedWithCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		
		expect(activityDao.findActivitiesForCustomers(customers)).andReturn(new ArrayList<Activity>());

		replay(activityDao);
		
		Set<User> result = userService.getAllUsersAssignedToCustomers(customers, true);
		
		verify(activityDao);
		
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testShouldReturnCorrectlyWhenUsersAssociatedWithCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		
		List<Activity> activities = new ArrayList<Activity>();

		Activity activity1 = new Activity();
		User user1  = new User();
		user1.setActive(false);
		user1.setUsername("user1");
		activity1.setAssignedUser(user1);
		
		Activity activity2 = new Activity();
		User user2  = new User();
		user2.setUsername("user2");
		user2.setActive(true);
		activity2.setAssignedUser(user2);
		
		activities.add(activity1);
		activities.add(activity2);
		
		expect(activityDao.findActivitiesForCustomers(customers)).andReturn(activities);
		
		replay(activityDao);
		
		Set<User> allUsersAssignedToCustomers = userService.getAllUsersAssignedToCustomers(customers, true);
		
		verify(activityDao);
		
		Assert.assertNotNull(allUsersAssignedToCustomers);
		Assert.assertEquals(1, allUsersAssignedToCustomers.size());
	}	

}
