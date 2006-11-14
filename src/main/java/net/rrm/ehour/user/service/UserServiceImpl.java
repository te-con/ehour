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

import net.rrm.ehour.exception.NoResultsException;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.dto.AuthUser;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

/**
 * @author   Thies
 */
public class UserServiceImpl implements UserService
{
	private	UserDAO		userDAO;
	private	Logger		logger = Logger.getLogger(UserServiceImpl.class);



	/**
	 * Get user by userID
	 * @param userID
	 * @return
	 * @throws NoResultsException
	 */	
	public User getUser(Integer userId) 
	{
		return userDAO.findById(userId);
	}

	/**
	 * Set the User DOA
	 * @param  dao
	 * @uml.property  name="userDAO"
	 */
	
	public void setUserDAO(UserDAO dao)
	{
		userDAO = dao;
	}

	/**
	 * 
	 */
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException
	{
		User		user = null;
		AuthUser	authUser;
		
		user = userDAO.findByUsername(username);
		
		if (user == null)
		{
			throw new UsernameNotFoundException("User unknown");
		}
		else
		{
			authUser = new AuthUser(user);
		}
		
		return authUser;
	}
}
