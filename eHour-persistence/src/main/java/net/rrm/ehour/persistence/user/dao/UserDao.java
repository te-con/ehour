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

package net.rrm.ehour.persistence.user.dao;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.persistence.dao.GenericDao;

import java.util.List;

public interface UserDao extends GenericDao<User, Integer>
{
	/**
	 * Find user by username
	 * @param username
	 * @return
	 */
	
	User findByUsername(String username);
	
	/**
	 * Find users
	 * @param onlyActive -> include only active users
	 * @return
	 */
	List<User> findUsers(boolean onlyActive);

	/**
	 * Find active users
	 * @return
	 */
	List<User> findActiveUsers();
	
	/**
	 * Find all active users with email address set
	 * @return
	 */
	List<User> findAllActiveUsersWithEmailSet();
	
	/**
	 * Find users for departments with active flag
	 * @param pattern
	 * @param departments
	 * @param onlyActive
	 * @return
	 */
	List<User> findUsersForDepartments(List<UserDepartment> departments, boolean onlyActive);
	
	/**
	 * Delete users with PM role but are not PM anymore
	 * @return
	 */
	void deletePmWithoutProject();
}
