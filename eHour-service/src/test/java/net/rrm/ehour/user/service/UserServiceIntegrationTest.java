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

import java.util.List;

import org.omg.CORBA.COMM_FAILURE;

import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.util.EhourConstants;

/**
 * Small unit test to test service <-> dao interaction with tx 
 **/

public class UserServiceIntegrationTest extends BaseDAOTest
{
	private UserService userService;
	
	public void testUpdateUser() throws ObjectNotFoundException, PasswordEmptyException, ObjectNotUniqueException
	{
		User user = new User(1);
		user.setUsername("thies");
		
		user.addUserRole(new UserRole("ROLE_ADMIN"));
		
		user = userService.persistUser(user);
		
		assertEquals("ROLE_ADMIN", user.getUserRoles().iterator().next().getRole());
		
		
	}	
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testAddUser() throws Exception
	{
		User	user = DummyDataGenerator.getUser();
		userService.persistUser(user);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testGetUserIsDeletable() throws Exception
	{
		User	user = userService.getUserAndCheckDeletability(3);
		assertTrue(user.isDeletable());
	}	
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testGetUserIsNotDeletable() throws Exception
	{
		User	user = userService.getUserAndCheckDeletability(1);
		assertFalse(user.isDeletable());
	}	
	
	/**
	 * 
	 * @throws Exception
	 */
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
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testGetUserDepartmentNoDelete() throws Exception
	{
		UserDepartment dept = userService.getUserDepartment(1);
		
		assertFalse(dept.isDeletable());
	}
		
	/**
	 * 
	 * @throws Exception
	 */
	public void testGetUserDepartmentDelete() throws Exception
	{
		UserDepartment dept = userService.getUserDepartment(2);
		
		assertTrue(dept.isDeletable());
	}

	
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
	

	
	public void testFindUsersByPatternAndUserRole()
	{
		List<User>	results;
		
		results = userService.getUsersByNameMatch(null, true, new UserRole(EhourConstants.ROLE_CONSULTANT));
		assertEquals(4, results.size());		
	}	
	
	/**
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}
	
	protected String[] getConfigLocations()
	{
		return new String[] { "classpath:/applicationContext-datasource.xml",
							  "classpath:/applicationContext-dao.xml",
							  "classpath:/applicationContext-mail.xml", 							  
							  "classpath:/applicationContext-service.xml"};	
	}		
}
