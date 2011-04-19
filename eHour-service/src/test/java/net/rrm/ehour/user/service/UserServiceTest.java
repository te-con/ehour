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
import java.util.Set;

import junit.framework.TestCase;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.persistence.user.dao.UserRoleDao;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;


/**
 * @author Thies
 *
 */
public class UserServiceTest extends TestCase 
{
	private	UserService			userService;
	private	UserDao				userDAO;
	private	UserDepartmentDao	userDepartmentDAO;
	private	UserRoleDao			userRoleDAO;
	
	/**
	 * 
	 */
	protected void setUp()
	{
		userService = new UserServiceImpl();
		userDAO = createMock(UserDao.class);
		userDepartmentDAO = createMock(UserDepartmentDao.class);
		userRoleDAO = createMock(UserRoleDao.class);
		
		((UserServiceImpl)userService).setUserDAO(userDAO);
		((UserServiceImpl)userService).setUserDepartmentDAO(userDepartmentDAO);
		((UserServiceImpl)userService).setUserRoleDAO(userRoleDAO);
		
		((UserServiceImpl)userService).setPasswordEncoder(new ShaPasswordEncoder(1));
	}
	
	
	/**
	 * 
	 *
	 */
	public void testGetUsersByNameMatch()
	{
		expect(userDAO.findUsersByNameMatch("test", true))
				.andReturn(new ArrayList<User>());
		
		replay(userDAO);
		
		userService.getUsersByNameMatch("test", true);
		
		verify(userDAO);
	}
	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.user.service.UserServiceImpl#getUser(java.lang.Integer)}.
	 * @throws ObjectNotFoundException 
	 */
	public void testGetUser() throws ObjectNotFoundException
	{
		User				user;
		ProjectAssignment	assignmentA, assignmentB;
		Project				projectA, projectB;
		Set<ProjectAssignment>	assignments = new HashSet<ProjectAssignment>();
		Calendar			calA, calB;
		
		
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
	public void testGetUserDepartment() throws ObjectNotFoundException
	{
		UserDepartment ud;

		expect(userDepartmentDAO.findById(1))
				.andReturn(new UserDepartment(new Integer(1), "bla", "ble"));
		
		replay(userDepartmentDAO);
		
		ud = userService.getUserDepartment(new Integer(1));
		
		verify(userDepartmentDAO);
		
		assertEquals("bla", ud.getName());
	}
	
	public void testGetUserRole()
	{
		String	role = "ROLE_CONSULTANT";
		expect(userRoleDAO.findById(role))
			.andReturn(new UserRole(role));
		
		replay(userRoleDAO);
		
		UserRole ur = userService.getUserRole(role);
		
		verify(userRoleDAO);
		
		assertEquals(role, ur.getRole());
	}
	
	public void testGetUserRoles()
	{
		expect(userRoleDAO.findAll())
			.andReturn(new ArrayList<UserRole>());
		
		replay(userRoleDAO);
		
		userService.getUserRoles();
		
		verify(userRoleDAO);
	}
	
	public void testAddAndcheckProjectManagementRoles()
	{
		User user = new User(1);
		user.setPassword("aa");
		user.setSalt(new Integer(2));
		user.setUsername("user");
		
		expect(userDAO.findById(new Integer(1)))
			.andReturn(user);
		
		expect(userDAO.persist(user))
			.andReturn(user);

		userDAO.deletePmWithoutProject();
		
		replay(userDAO);
		
		userService.addAndcheckProjectManagementRoles(new Integer(1));
		
		verify(userDAO);
		
		assertEquals("aa", user.getPassword());
	}
	
	public void testUpdatePassword() throws PasswordEmptyException, ObjectNotUniqueException
	{
		User user = new User(1);
		user.setPassword("aa");
		user.setSalt(new Integer(2));
		user.setUsername("user");
		user.setUpdatedPassword("fefe");
		
		expect(userDAO.findByUsername("user"))
			.andReturn(user);

		expect(userDAO.merge(user))
			.andReturn(user);
		
		replay(userDAO);
		
		userService.persistUser(user);
		
		verify(userDAO);
		
		assertFalse(user.getPassword().equals("aa") );
	}	
	
//	public void testPersistUserDepartment() throws ObjectNotUniqueException
//	{
//		UserDepartment ud = new UserDepartment();
//		ud.setDepartmentId(1);
//		ud.setName("t1");
//		ud.setCode("t2");
//		
//		UserDepartment ud2 = new UserDepartment();
//		ud2.setDepartmentId(1);
//		ud2.setName("t3");
//		ud2.setCode("t4");
//		
//		expect(userDepartmentDAO.findOnNameAndCode("t3", "t4"))
//			.andReturn(ud);
//		
//		userDepartmentDAO.merge(ud2);
//		
//		replay(userDepartmentDAO);
//		
//		userService.persistUserDepartment(ud2);
//		
//		verify(userDepartmentDAO);
//		
//		reset(userDepartmentDAO);
//		
//		ud2.setDepartmentId(2);
//		
//
//		expect(userDepartmentDAO.findOnNameAndCode("t3", "t4"))
//			.andReturn(ud);
//		
//		replay(userDepartmentDAO);
//		
//		try
//		{
//			userService.persistUserDepartment(ud2);
//			fail();
//		}
//		catch (ObjectNotUniqueException onue)
//		{
//			verify(userDepartmentDAO);
//		}
//	}
}
