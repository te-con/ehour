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

package net.rrm.ehour.user.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDao;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;

public interface UserDao extends GenericDao<User, Integer>
{
	/**
	 * Find a user by username and password
	 * @param username
	 * @return
	 */
	public User findByUsernameAndPassword(String username, String password);
	
	/**
	 * Find user by username
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
	 * @param departments
	 * @param onlyActive
	 * @return
	 */
	public List<User> findUsersForDepartments(String pattern, List<UserDepartment> departments, boolean onlyActive);
	
	/**
	 * Delete users with PM role but are not PM anymore
	 * @return
	 */
	public void deletePmWithoutProject();
}
