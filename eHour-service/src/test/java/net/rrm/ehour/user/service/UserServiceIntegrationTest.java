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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.rrm.ehour.AbstractServiceTest;
import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;

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
	// FIXME
//	@Test
//	public void testFindUsersByPatternAndUserRole()
//	{
//		List<User>	results;
//		
//		results = userService.getUsersByNameMatch(null, true, new UserRole(EhourConstants.ROLE_CONSULTANT));
//		
//		for (User user : results)
//		{
//			System.out.println(user.getUserId());
//		}
//		assertEquals(4, results.size());		
//	}	

	@Test
	public void testCheckProjectManagementRolesValid()
	{
		userService.addAndcheckProjectManagementRoles(3);

	}	
}
