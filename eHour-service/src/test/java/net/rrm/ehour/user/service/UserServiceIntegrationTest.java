/**
 * Created on Nov 17, 2006
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import net.rrm.ehour.AbstractServiceTest;
import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceIntegrationTest extends AbstractServiceTest
{
	@Autowired
	private UserService userService;
	
	@Test
	public void testUpdateUser() throws ObjectNotFoundException, PasswordEmptyException, ObjectNotUniqueException
	{
		User user = new User(1);
		user.setUsername("thies");
		
		user.addUserRole(new UserRole("ROLE_ADMIN"));
		
		user = userService.persistUser(user);
		
		assertEquals("ROLE_ADMIN", user.getUserRoles().iterator().next().getRole());
	}	
	
	@Test
	public void testAddUser() throws Exception
	{
		User	user = DummyDataGenerator.getUser();
		user.setUpdatedPassword("aa");
		userService.persistUser(user);
	}
	
	@Test
	public void testGetUserIsDeletable() throws Exception
	{
		User	user = userService.getUserAndCheckDeletability(3);
		assertTrue(user.isDeletable());
	}	
	
	@Test
	public void testGetUserIsNotDeletable() throws Exception
	{
		User	user = userService.getUserAndCheckDeletability(1);
		assertFalse(user.isDeletable());
	}	
	
	@Test
	public void testDeleteUserDepartment() throws Exception
	{
		userService.deleteDepartment(new Integer(2));
		
		// should throw exception
		try
		{
			userService.getUserDepartment(2);
			fail();
		} catch (ObjectNotFoundException onfe) 
		{
			// expected
		}
	}
	
	@Test
	public void testGetUserDepartmentNoDelete() throws Exception
	{
		UserDepartment dept = userService.getUserDepartment(1);
		
		assertFalse(dept.isDeletable());
	}
		
	@Test
	public void testGetUserDepartmentDelete() throws Exception
	{
		UserDepartment dept = userService.getUserDepartment(2);
		
		assertTrue(dept.isDeletable());
	}

	@Test
	public void testPersistUser() throws Exception
	{
		User user = new User();
		user.setUsername("thies");
		user.setFirstName("Thies");
		user.setLastName("Unit");
		user.setEmail("dum@dum.nl");
		user.setUserDepartment(new UserDepartment(1));
		user.setActive(true);
		user.setUserId(1);
		
		userService.persistUser(user);
	}
	
	@Test
	public void testDeleteUser()
	{
		userService.deleteUser(1);
		
		try
		{
			userService.getUser(1);
			fail();
		} catch (ObjectNotFoundException e)
		{
		}
	}
	
	@Test
	public void testFindUsersByPatternAndUserRole()
	{
		List<User>	results;
		
		results = userService.getUsersByNameMatch(null, true, new UserRole(EhourConstants.ROLE_CONSULTANT));
		assertEquals(4, results.size());		
	}	

	@Test
	public void testCheckProjectManagementRolesValid()
	{
		userService.addAndcheckProjectManagementRoles(3);

	}	
}
