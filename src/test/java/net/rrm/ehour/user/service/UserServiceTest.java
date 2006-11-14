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

import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.service.UserServiceImpl;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;


/**
 * @author Thies
 *
 */
public class UserServiceTest extends MockObjectTestCase 
{
	private	UserService	userService;
	/**
	 * 
	 */
	protected void setUp()
	{
		userService = new UserServiceImpl();
	}

	/**
	 * Test method for {@link net.rrm.ehour.user.service.UserServiceImpl#getUser(java.lang.Integer)}.
	 */
	public void testGetUserInteger()
	{
		Mock 	dao = new Mock(UserDAO.class);
		User	user;
		dao = new Mock(UserDAO.class);
		dao.expects(once())
		   .method("findById")
		   .with(eq(new Integer(1)))
		   .will(returnValue(new User("thies", "pwd")));
		
		((UserServiceImpl)userService).setUserDAO((UserDAO) dao.proxy());
		
		user = userService.getUser(new Integer(1));
		
		assertEquals("thies", user.getUsername());	}

}
