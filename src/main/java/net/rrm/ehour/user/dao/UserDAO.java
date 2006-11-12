/**
 * Created on Nov 11, 2006
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
package net.rrm.ehour.user.dao;

import net.rrm.ehour.user.domain.User;

public interface UserDAO
{
	/**
	 * Find by Id
	 * @param userId
	 * @return
	 */
	public User findById(Integer userId);
	
	/**
	 * Find a user by username & password combination
	 * @param username
	 * @param password
	 * @return
	 */
	public User findByUsernameAndPassword(String username, String password);
	
	/**
	 * Find a user by username
	 * @param username
	 * @return
	 */
	public User findByUsername(String username);
	
	/**
	 * Persist a user
	 * @param user
	 * @return
	 */
	public void persist(User user);
}
