/**
 * Created on Nov 17, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.user.service;

import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;

/**
 * Small unit test to test service <-> dao interaction with tx 
 **/

public class UserServiceIntegrationTest extends BaseDAOTest
{
	private UserService userService;
	
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
	public void testDeleteUserDepartment() throws Exception
	{
		userService.deleteUserDepartment(new Integer(2));
		
		// should throw exception
		try
		{
			userService.deleteUserDepartment(new Integer(1));
			assertTrue(false);
		} catch (ParentChildConstraintException pcce)
		{
			// 
		}
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
							  "classpath:/applicationContext-service.xml"};	
	}		
}
