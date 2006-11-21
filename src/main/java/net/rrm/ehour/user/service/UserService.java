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
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;


public interface UserService extends UserDetailsService
{

	/**
	 * Get user by username (acegi)
	 * @param username
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException;
	
	/**
	 * Get user by userID
	 * @param userID
	 * @return
	 * @throws NoResultsException
	 */
    public User getUser(Integer userID);
    
    /**
     * Get list of all user departments
     * @return
     */
    public List getUserDepartments();

    /**
     * Persist user department to database
     * @param department
     * @return
     */
    public UserDepartment persistUserDepartment(UserDepartment department);
    
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
}
