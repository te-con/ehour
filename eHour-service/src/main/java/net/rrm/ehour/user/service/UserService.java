/**
 * Created on Nov 14, 2006
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

import java.util.List;

import net.rrm.ehour.exception.NoResultsException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.user.domain.CustomerFoldPreference;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;


public interface UserService 
{
	/**
	 * Get user by userId
	 * User.inactiveProjectAssignments is populated with project 
	 * assignments ending before or starting after the current date
	 * @param userID
	 * @return
	 * @throws NoResultsException
	 */
    public User getUser(Integer userID);
    
	/**
	 * Get user by username
	 * @param userID
	 * @return
	 * @throws NoResultsException
	 */
    public User getUser(String username);    
    
    /**
     * Get user on username and plain password
     * @param username
     * @return
     */
    public User getUser(String username, String plainPassword);
    
    /**
     * Persist a user
     * @param user
     * @return
     */
    public User persistUser(User user)  throws PasswordEmptyException, ObjectNotUniqueException;
    
    /**
     * Get users where first name or last name matches
     * @param match
     * @param inclInactive incl inactive users?
     * @return
     */
    public List<User> getUsersByNameMatch(String match, boolean inclInactive);
    
    /**
     * Get all active users
     * @return
     */
    public List<User> getUsers();
    
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
	 * Delete user department on id
	 * @param departmentId
	 * @throws ParentChildConstraintException when there are still users attached to the department
	 */
    public void deleteUserDepartment(Integer  departmentId) throws ParentChildConstraintException;
    
    /**
     * Get user department on id 
     * @param departmentId
     * @return
     */
    public UserDepartment getUserDepartment(Integer departmentId);
    
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
     * Check if PM roles are still valid and remove 'm if necc.
     *
     */
    public void checkProjectManagementRolesValid();
    
    /**
     * Customer fold preference
     * @param customerFoldPreference
     */
    public void persistCustomerFoldPreference(CustomerFoldPreference customerFoldPreference);
}
