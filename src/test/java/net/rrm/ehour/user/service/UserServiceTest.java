/**
 * Created on Nov 4, 2006
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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;


/**
 * @author Thies
 *
 */
public class UserServiceTest extends TestCase 
{
	private	UserService			userService;
	private	UserDAO				userDAO;
	private	UserDepartmentDAO	userDepartmentDAO;
	
	/**
	 * 
	 */
	protected void setUp()
	{
		userService = new UserServiceImpl();
		userDAO = createMock(UserDAO.class);
		userDepartmentDAO = createMock(UserDepartmentDAO.class);
		
		((UserServiceImpl)userService).setUserDAO(userDAO);
		((UserServiceImpl)userService).setUserDepartmentDAO(userDepartmentDAO);
		
	}

	/**
	 * Test method for {@link net.rrm.ehour.user.service.UserServiceImpl#getUser(java.lang.Integer)}.
	 */
	public void testGetUserInteger()
	{
		User	user;
		
		
		expect(userDAO.findById(1))
				.andReturn(new User("thies", "pwd"));
		
		replay(userDAO);
		
		user = userService.getUser(new Integer(1));
		
		verify(userDAO);
		
		assertEquals("thies", user.getUsername());
	}
	
	/**
	 * 
	 *
	 */
	public void testGetUserDepartment()
	{
		UserDepartment ud;

		expect(userDepartmentDAO.findById(1))
				.andReturn(new UserDepartment(new Integer(1), "bla", "ble"));
		
		replay(userDepartmentDAO);
		
		ud = userService.getUserDepartment(new Integer(1));
		
		verify(userDepartmentDAO);
		
		assertEquals("bla", ud.getName());
	}

}
