/**
 * Created on Nov 14, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.user.service;

import java.util.List;

import net.rrm.ehour.domain.CustomerFoldPreference;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.NoResultsException;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;


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
    public User getUser(Integer userID) throws ObjectNotFoundException;
    
    /**
     * Get user by userId and optional check if the user is deletable (as in, no hours booked on his/her
     * assignments)
     * @param userId
     * @param checkIfDeletable
     * @return
     */
    public User getUserAndCheckDeletability(Integer userId) throws ObjectNotFoundException;
    
	/**
	 * Get user by username
	 * @param userID
	 * @return
	 * @throws NoResultsException
	 */
    public User getUser(String username);    

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
     * Customer fold preference
     * @param customerFoldPreference
     */
    public void persistCustomerFoldPreference(CustomerFoldPreference customerFoldPreference);

    /**
     * Cascading delete of user
     * @param user
     */
    public void deleteUser(Integer userId);
    
    
    /**
     * Cascading delete of department
     * @param departmentId
     */
    public void deleteDepartment(Integer departmentId);
}
