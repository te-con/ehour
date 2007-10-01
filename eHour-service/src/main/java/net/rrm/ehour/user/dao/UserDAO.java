/**
 * Created on Nov 11, 2006
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
package net.rrm.ehour.user.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;

public interface UserDAO extends GenericDAO<User, Integer>
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
