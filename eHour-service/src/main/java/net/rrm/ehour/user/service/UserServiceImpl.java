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
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;
import net.rrm.ehour.user.dao.UserRoleDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.util.DateUtil;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 * @author   Thies
 */
public class UserServiceImpl implements UserService
{
	private	UserDAO				userDAO;
	private	UserDepartmentDAO	userDepartmentDAO;
	private	UserRoleDAO			userRoleDAO;
	private	Logger				logger = Logger.getLogger(UserServiceImpl.class);
	private	ProjectAssignmentService		projectAssignmentService;

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
	
	public List<UserDepartment> getUserDepartments()
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
		UserDepartment	otherDept;
		
		otherDept = userDepartmentDAO.findOnNameAndCode(department.getName(), department.getCode());
		
		if (otherDept == null)
		{
			userDepartmentDAO.persist(department);
		}
		else if (otherDept.getDepartmentId().equals(department.getDepartmentId()))
		{
			userDepartmentDAO.merge(department);
		}
		else
		{
			throw new ObjectNotUniqueException("name/code not unique");
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
	public List<User> getUsersByNameMatch(String match, boolean inclInactive)
	{
		List<User>	results;
		
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
	public List<User> getUsers()
	{
		return userDAO.findAllActiveUsers();
	}


	/**
	 * 
	 */
	public UserRole getUserRole(String userRoleId)
	{
		return userRoleDAO.findById(userRoleId);
	}


	/**
	 * Get the assignable user roles
	 */
	public List<UserRole> getUserRoles()
	{
		List<UserRole> userRoles = userRoleDAO.findAll();
		
		userRoles.remove(new UserRole("ROLE_PROJECTMANAGER"));
		
		return userRoles;
	}

	/**
	 * Persist user
	 */

	public User persistUser(User user) throws PasswordEmptyException, ObjectNotUniqueException
	{
		User	dbUser;
		User	nameUser;

		nameUser = userDAO.findByUsername(user.getUsername());
				
		if (nameUser != null && !nameUser.getUserId().equals(user.getUserId()))
		{
			throw new ObjectNotUniqueException("Username already in use");
		}
		
		
		if (user.getPassword() == null || user.getPassword().trim().equals(""))
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
			user.setPassword(encryptPassword(user.getPassword()));
			
			// new users
			if (user.getUserId() == null)
			{
				projectAssignmentService.assignUserToDefaultProjects(user);
			}
			
			userDAO.merge(user);
			
			return user;
		}
	}

	/**
	 * @param projectAssignmentService the projectAssignmentService to set
	 */
	public void setProjectAssignmentService(ProjectAssignmentService projectAssignmentService)
	{
		this.projectAssignmentService = projectAssignmentService;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#getUsersWithEmailSet()
	 */
	public List<User> getUsersWithEmailSet()
	{
		return userDAO.findAllActiveUsersWithEmailSet();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#checkProjectManagementRolesValid()
	 */
	public void checkProjectManagementRolesValid()
	{
		List<User>	invalidUsers;
		List<User>	validUsers;
		
		Set<UserRole>	userRoles;
		
		// invalids
		invalidUsers = userDAO.findUsersWithPMRoleButNoProject();
		
		for (User user : invalidUsers)
		{
			logger.info("Removing projectmgmt role from " + user.getLastName());
			
			userRoles = new HashSet<UserRole>();
			
			// no clue why set.remove won't work nor do I care
			for (UserRole role : user.getUserRoles())
			{
				if (!role.getRole().equals("ROLE_PROJECTMANAGER"))
				{
					userRoles.add(role);
				}
			}
			
			user.setUserRoles(userRoles);
			userDAO.merge(user);
		}

		// valids
		validUsers = userDAO.findUsersWhoDontHavePMRoleButArePM();
		
		for (User user : validUsers)
		{
			logger.info("Adding projectmgmt role to " + user.getLastName());
			user.getUserRoles().add(new UserRole("ROLE_PROJECTMANAGER"));
			userDAO.merge(user);
		}
	}


	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#getUser(java.lang.String)
	 */
	public User getUser(String username, String plainPassword)
	{
		User		user = null;
		
		logger.debug("Finding user " + username + " on user & password");
		
		user = userDAO.findByUsernameAndPassword(username, encryptPassword(plainPassword));
		
		return user;
	}

	/**
	 * Encrypt password (sha1)
	 * @param plainPassword
	 * @return
	 */
	private String encryptPassword(String plainPassword)
	{
		byte[]	shaPass;
		
		shaPass = DigestUtils.sha(plainPassword);
		
		return new String(Hex.encodeHex(shaPass));
	}
}
