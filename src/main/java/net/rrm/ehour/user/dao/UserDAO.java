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

import java.util.List;

import net.rrm.ehour.dao.GenericDAO;
import net.rrm.ehour.user.domain.User;

public interface UserDAO extends GenericDAO<User, Integer>
{
	/**
	 * Find a user by username
	 * @param username
	 * @return
	 */
	public User findByUsername(String username);
	
	/**
	 * Find users where pattern matches either first name or last name
	 * @param pattern
	 * @param onlyActive -> include only active users
	 * @return
	 */
	public List<User> findUsersByNameMatch(String pattern, boolean onlyActive);
	
	/**
	 * Find all users
	 * @return
	 */
	public List<User> findAllActiveUsers();
	
	/**
	 * Find all active users with email address set
	 * @return
	 */
	public List<User> findAllActiveUsersWithEmailSet();
	
	/**
	 * Find users for departments with filter pattern and active flag
	 * @param pattern
	 * @param departmentIds
	 * @param onlyActive
	 * @return
	 */
	public List<User> findUsersForDepartments(String pattern, Integer[] departmentIds, boolean onlyActive);
	
	/**
	 * Find users with a PM role but no project
	 * @return
	 */
	public List<User> findUsersWithPMRoleButNoProject();
	
	/**
	 * Find users who don't have PM role but are PM
	 * @return
	 */
	public List<User> findUsersWhoDontHavePMRoleButArePM();
}
