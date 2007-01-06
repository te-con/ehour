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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.exception.NoResultsException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;
import net.rrm.ehour.user.dao.UserRoleDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.user.dto.AuthUser;
import net.rrm.ehour.util.DateUtil;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author   Thies
 */
public class UserServiceImpl implements UserService
{
	private	UserDAO				userDAO;
	private	UserDepartmentDAO	userDepartmentDAO;
	private	UserRoleDAO			userRoleDAO;
	private	Logger				logger = Logger.getLogger(UserServiceImpl.class);
	private	ProjectService		projectService;

	/**
	 * Get user by userId 
	 * @param userID
	 * @return
	 * @throws NoResultsException
	 */	
	public User getUser(Integer userId) 
	{
		User 					user = userDAO.findById(userId);
		Set<ProjectAssignment>	inactiveAssignments = new HashSet<ProjectAssignment>();
		Date					currentDate = new Date();
		
		if (user != null && user.getProjectAssignments() != null)
		{
			for (ProjectAssignment assignment : user.getProjectAssignments())
			{
				if ((!DateUtil.isDateWithinRange(currentDate , assignment.getDateRange())) ||
					 (assignment.getProject() == null || !assignment.getProject().isActive()))
				{
					inactiveAssignments.add(assignment);
				}
			}
		}
		
		user.getProjectAssignments().removeAll(inactiveAssignments);
		
		user.setInactiveProjectAssignments(inactiveAssignments);
		
		return user;
	}


	/**
	 * 
	 */
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException
	{
		User		user = null;
		AuthUser	authUser;
		
		logger.debug("Finding user " + username);
		
		user = userDAO.findByUsername(username);
		
		if (user == null || !user.isActive())
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Load user by username for " + username + " but user unknown or inactive");
			}
			
			throw new UsernameNotFoundException("User unknown");
		}
		else
		{
			authUser = new AuthUser(user);
		}
		
		return authUser;
	}

	/**
	 * 
	 */
	
	public List getUserDepartments()
	{
		return userDepartmentDAO.findAll();
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
	 * Set the user dept dao
	 * @param dao
	 */
	public void setUserDepartmentDAO(UserDepartmentDAO dao)
	{
		userDepartmentDAO = dao;
	}

	public void setUserRoleDAO(UserRoleDAO dao)
	{
		userRoleDAO = dao;
	}

	/**
	 * Delete user department on id
	 * @param departmentId
	 * @throws parentChildConstraintException when there are still users attached to the department
	 */
	public void deleteUserDepartment(Integer departmentId) throws ParentChildConstraintException
	{
		UserDepartment department;
		
		department = userDepartmentDAO.findById(departmentId);
		
		if (department != null)
		{
			if (department.getUsers().size() > 0)
			{
				throw new ParentChildConstraintException("Users are still attached to this department");
			}
			else
			{
				userDepartmentDAO.delete(department);
			}
		}
	}

	/**
	 * @throws ObjectNotUniqueException 
	 * 
	 */
	public UserDepartment persistUserDepartment(UserDepartment department) throws ObjectNotUniqueException
	{
		try
		{
			userDepartmentDAO.persist(department);
		}
		catch (DataIntegrityViolationException cve)
		{
			throw new ObjectNotUniqueException(cve);
		}		
		
		return department;
		
	}


	/**
	 * Get user department by id
	 */
	public UserDepartment getUserDepartment(Integer departmentId)
	{
		return userDepartmentDAO.findById(departmentId);
	}


	/**
	 * 
	 */
	public List getUsersByNameMatch(String match, boolean inclInactive)
	{
		List	results;
		
//		if (match == null || match.equals(""))
//		{
//			logger.debug("Empty match pattern");
//			results = userDAO.findUsers();
//		}
//		else
//		{
			results = userDAO.findUsersByNameMatch(match, inclInactive);
//		}
		
		return results;
	}	
	
	/**
	 * 
	 */
	public List getUsers()
	{
		return userDAO.findUsers();
	}


	/**
	 * 
	 */
	public UserRole getUserRole(String userRoleId)
	{
		return userRoleDAO.findById(userRoleId);
	}


	/**
	 * 
	 */
	public List getUserRoles()
	{
		return userRoleDAO.findAll();
	}

	/**
	 * Persist user
	 */

	public User persistUser(User user) throws PasswordEmptyException
	{
		User	dbUser;
		byte[]	shaPass;
		
		if (user.getPassword() == null || user.getPassword().equals(""))
		{
			// if password is empty and user is new we have a problem
			if (user.getUserId() == null)
			{
				throw new PasswordEmptyException("New users need a password");
			}
			
			dbUser = userDAO.findById(user.getUserId());
			
			dbUser.setEmail(user.getEmail());
			dbUser.setFirstName(user.getFirstName());
			dbUser.setLastName(user.getLastName());
			dbUser.setUserDepartment(user.getUserDepartment());
			dbUser.setUsername(user.getUsername());
			dbUser.setUserRoles(user.getUserRoles());
			dbUser.setActive(user.isActive());
			
			userDAO.persist(dbUser);

			return dbUser;
		}
		else
		{
			shaPass = DigestUtils.sha(user.getPassword());
			user.setPassword(new String(Hex.encodeHex(shaPass)));
			
			// new users
			if (user.getUserId() == null)
			{
				projectService.assignUserToDefaultProjects(user);
			}
			
			userDAO.persist(user);
			
			return user;
		}
	}

	/**
	 * @param projectService the projectService to set
	 */
	public void setProjectService(ProjectService projectService)
	{
		this.projectService = projectService;
	}
}
