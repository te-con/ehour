/**
 * Created on Nov 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
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
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;
import net.rrm.ehour.user.dao.UserRoleDAO;

import org.acegisecurity.providers.encoding.ShaPasswordEncoder;


/**
 * @author Thies
 *
 */
@SuppressWarnings("unchecked")
public class UserServiceTest extends TestCase 
{
	private	UserService			userService;
	private	UserDAO				userDAO;
	private	UserDepartmentDAO	userDepartmentDAO;
	private	UserRoleDAO			userRoleDAO;
	
	/**
	 * 
	 */
	protected void setUp()
	{
		userService = new UserServiceImpl();
		userDAO = createMock(UserDAO.class);
		userDepartmentDAO = createMock(UserDepartmentDAO.class);
		userRoleDAO = createMock(UserRoleDAO.class);
		
		((UserServiceImpl)userService).setUserDAO(userDAO);
		((UserServiceImpl)userService).setUserDepartmentDAO(userDepartmentDAO);
		((UserServiceImpl)userService).setUserRoleDAO(userRoleDAO);
		
		((UserServiceImpl)userService).setPasswordEncoder(new ShaPasswordEncoder(1));
	}
	
	
	/**
	 * 
	 *
	 */
//	public void testLoadUserByUsername()
//	{
//		User	user;
//		user = new User();
//		user.setActive(false);
//		
//		expect(userDAO.findByUsername("test"))
//			.andReturn(user);
//		
//		replay(userDAO);
//		
//		try
//		{
//			userService.loadUserByUsername("test");
//			fail("No exception thrown");
//		}
//		catch (UsernameNotFoundException unne)
//		{
//			verify(userDAO);	
//		}
//	}
	
	/**
	 * 
	 *
	 */
	public void testGetUsersByNameMatch()
	{
		expect(userDAO.findUsersByNameMatch("test", true))
				.andReturn(new ArrayList());
		
		replay(userDAO);
		
		userService.getUsersByNameMatch("test", true);
		
		verify(userDAO);
	}
	/**
	 * Test method for {@link net.rrm.ehour.user.service.UserServiceImpl#getUser(java.lang.Integer)}.
	 * @throws ObjectNotFoundException 
	 */
	public void testGetUser() throws ObjectNotFoundException
	{
		User				user;
		ProjectAssignment	paA, paB;
		Project				prA, prB;
		Set					pas = new HashSet();
		Calendar			calA, calB;
		
		
		user = new User("thies", "pwd");

		prA = new Project();
		prA.setActive(true);
		paA = new ProjectAssignment();
		paA.setAssignmentId(1);
		paA.setAssignmentType(new ProjectAssignmentType(0));
		calA = new GregorianCalendar();
		calA.add(Calendar.MONTH, -5);
		paA.setDateStart(calA.getTime());
		calA.add(Calendar.MONTH, 1);
		paA.setDateEnd(calA.getTime());
		paA.setProject(prA);
		pas.add(paA);
		
		prB = new Project();
		prB.setActive(true);

		paB = new ProjectAssignment();
		paB.setAssignmentId(2);
		paB.setAssignmentType(new ProjectAssignmentType(0));
		calB = new GregorianCalendar();
		calB.add(Calendar.MONTH, -2);
		paB.setDateStart(calB.getTime());
		calB = new GregorianCalendar();
		calB.add(Calendar.MONTH, 1);
		paB.setDateEnd(calB.getTime());
		paB.setProject(prB);
		pas.add(paB);
		
		user.setProjectAssignments(pas);
		
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
		
		expect(userDAO.findByUsername("user"))
			.andReturn(user);

		userDAO.deletePmWithoutProject();
		expect(userDAO.merge(user))
			.andReturn(user);
		
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
