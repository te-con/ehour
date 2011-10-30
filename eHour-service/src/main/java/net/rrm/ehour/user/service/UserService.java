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

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;

import java.util.List;


public interface UserService 
{
	/**
	 * Get user by userId
	 * User.inactiveProjectAssignments is populated with project 
	 * assignments ending before or starting after the current date
	 * @param userID to look for
	 * @return
	 */
    public User getUser(Integer userID) throws ObjectNotFoundException;
    
    /**
     * Get user by userId and optional check if the user is deletable (as in, no hours booked on his/her
     * assignments)
     * @param userId
     * @return
     */
    public User getUserAndCheckDeletability(Integer userId) throws ObjectNotFoundException;
    
	/**
	 * Get user by username
	 */
    public User getUser(String username);    

    /**
     * Persist a user
     */
    public User editUser(User user)  throws PasswordEmptyException, ObjectNotUniqueException;

    /**
     * Create a new user
     * @param user
     * @return
     * @throws PasswordEmptyException
     * @throws ObjectNotUniqueException
     */
    public void newUser(User user, String password)   throws PasswordEmptyException, ObjectNotUniqueException;

    /**
     * Change password for user
     * @param username of the user to change the password of
     * @param newUnencryptedPassword the new unencrypted password
     */
    public boolean changePassword(String username, String newUnencryptedPassword);

    /**
     * Get users where first name or last name matches
     * @param match
     * @param inclInactive incl inactive users?
     * @return
     */
    public List<User> getUsersByNameMatch(String match, boolean inclInactive);
    
    /**
     * Get users where first name or last name matches with user role
     * @param match
     * @param inclInactive
     * @param userRole
     * @return
     */
    public List<User> getUsersByNameMatch(String match, boolean inclInactive, UserRole userRole);
    
    /**
     * Get all active users
     * @return
     */
    public List<User> getUsers();
    
    /**
     * Get all active users with userRole
     * @param userRole
     * @return
     */
    public List<User> getUsers(UserRole userRole);
    /**
     * Get all active users with email set
     * @return
     */
    public List<User> getUsersWithEmailSet();
    
    /**
     * Get list of all user departments
     * @return
     */
    public List<UserDepartment> getUserDepartments();

    /**
     * Persist user department to database
     * @param department
     * @return List with userdepartments
     */
    public UserDepartment persistUserDepartment(UserDepartment department) throws ObjectNotUniqueException;
	
    /**
     * Get user department on id 
     * @param departmentId
     * @return
     */
    public UserDepartment getUserDepartment(Integer departmentId) throws ObjectNotFoundException;
    
    /**
     * Get userrole on Id
     * @param userRoleId
     * @return
     */
    public UserRole getUserRole(String userRoleId);
    
    /**
     * Get all user roles
     * @return
     */
    public List<UserRole> getUserRoles();
    
    /**
     * Add pm role to user and validate all other pm roles
     * @param userId to add pm role to
     */
    public User addAndcheckProjectManagementRoles(Integer userId);

    /**
     * Cascading delete of user
     */
    public void deleteUser(Integer userId);
    
    
    /**
     * Cascading delete of department
     * @param departmentId
     */
    public void deleteDepartment(Integer departmentId);
}
