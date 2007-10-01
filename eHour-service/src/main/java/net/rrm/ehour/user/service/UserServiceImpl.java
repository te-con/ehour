/**
 * Created on Nov 4, 2006
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.exception.NoResultsException;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.util.ProjectAssignmentUtil;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.user.dao.CustomerFoldPreferenceDAO;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;
import net.rrm.ehour.user.dao.UserRoleDAO;
import net.rrm.ehour.user.domain.CustomerFoldPreference;
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
	private CustomerFoldPreferenceDAO	customerFoldPreferenceDAO;
	private	UserDAO				userDAO;
	private	UserDepartmentDAO	userDepartmentDAO;
	private	UserRoleDAO			userRoleDAO;
	private	Logger				logger = Logger.getLogger(UserServiceImpl.class);
	private	ProjectAssignmentService		projectAssignmentService;
	private	ReportService		reportService;
	private TimesheetService	timesheetService;

	/**
	 * Get user by userId 
	 * @param userID
	 * @return
	 * @throws NoResultsException
	 */	
	public User getUser(Integer userId) throws ObjectNotFoundException
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
			
			user.getProjectAssignments().removeAll(inactiveAssignments);
			user.setInactiveProjectAssignments(inactiveAssignments);
		}
		else
		{
			throw new ObjectNotFoundException("User not found");
		}
			
		
		return user;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#getUserAndCheckDeletability(java.lang.Integer)
	 */
	public User getUserAndCheckDeletability(Integer userId) throws ObjectNotFoundException
	{
		User	user = getUser(userId);
		
		if ( (user.getProjectAssignments() == null || user.getProjectAssignments().size() == 0) &&
			 (user.getInactiveProjectAssignments() == null || user.getInactiveProjectAssignments().size() == 0))
		{
			user.setDeletable(true);
		}
		else
		{
			// bummer, we need to check if the user booked any hours on the assignments
			List<Integer>	assignmentIds = new ArrayList<Integer>();

			assignmentIds.addAll(ProjectAssignmentUtil.getAssignmentIds(user.getProjectAssignments()));
			assignmentIds.addAll(ProjectAssignmentUtil.getAssignmentIds(user.getInactiveProjectAssignments()));
			
			List<ProjectAssignmentAggregate> aggregates =reportService.getHoursPerAssignment(assignmentIds);
			
			user.setDeletable(ProjectAssignmentUtil.isEmptyAggregateList(aggregates));
		}
		
		logger.info("Retrieved user " + user.getUsername() + ", deletable: " + user.isDeletable());
		
		return user;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#getUser(java.lang.String)
	 */
	public User getUser(String username) 
	{
		User user = userDAO.findByUsername(username);
		
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
	public UserDepartment getUserDepartment(Integer departmentId) throws ObjectNotFoundException
	{
		UserDepartment userDepartment = userDepartmentDAO.findById(departmentId);
		
		if (userDepartment == null)
		{
			throw new ObjectNotFoundException("Department not found");
		}
		
		userDepartment.setDeletable(userDepartment.getUsers() == null || userDepartment.getUsers().size() == 0);
		
		return userDepartment;
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
	 * FIXME updating existing user doesn't work
	 */

	public User persistUser(User user) throws PasswordEmptyException, ObjectNotUniqueException
	{
		User	dbUser;

		logger.info("Persisting user: " + user);

		// check username uniqueness
		dbUser = userDAO.findByUsername(user.getUsername());
				
		if (dbUser != null && !dbUser.getUserId().equals(user.getUserId()))
		{
			throw new ObjectNotUniqueException("Username already in use");
		}
		
		// copy over password or encrypt new one
		if (user.getPassword() == null || user.getPassword().trim().equals(""))
		{
			// if password is empty and user is new we have a problem
			if (user.getUserId() == null)
			{
				throw new PasswordEmptyException("New users need a password");
			}
			
			user.setPassword(dbUser.getPassword());
		}
		else
		{
			user.setPassword(encryptPassword(user.getPassword()));
		}
		
		// assign new users to default projects
		if (user.getUserId() == null)
		{
			projectAssignmentService.assignUserToDefaultProjects(user);
		}
		
		userDAO.merge(user);
			
		return user;
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
		// FIXME
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

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#persistCustomerFoldPreference(net.rrm.ehour.user.domain.CustomerFoldPreference)
	 */
	public void persistCustomerFoldPreference(CustomerFoldPreference customerFoldPreference)
	{
		customerFoldPreferenceDAO.persist(customerFoldPreference);
	}

	/**
	 * @param customerFoldPreferenceDAO the customerFoldPreferenceDAO to set
	 */
	public void setCustomerFoldPreferenceDAO(CustomerFoldPreferenceDAO customerFoldPreferenceDAO)
	{
		this.customerFoldPreferenceDAO = customerFoldPreferenceDAO;
	}

	/**
	 * @param reportService the reportService to set
	 */
	public void setReportService(ReportService reportService)
	{
		this.reportService = reportService;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#getUsers(net.rrm.ehour.user.domain.UserRole)
	 */
	public List<User> getUsers(UserRole userRole)
	{
		logger.debug("Finding users on role");
		return getUsersByNameMatch(null, true, userRole);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#getUsersByNameMatch(java.lang.String, boolean, net.rrm.ehour.user.domain.UserRole)
	 */
	public List<User> getUsersByNameMatch(String match, boolean inclInactive, UserRole userRole)
	{
		List<User> users = userDAO.findUsersByNameMatch(match, inclInactive);
		List<User> validUsers = new ArrayList<User>();
		
		// result of bad many-to-many mapping. should fix once..
		for (User user : users)
		{
			if (user.getUserRoles().contains(userRole))
			{
				validUsers.add(user);
			}
		}
		
		return validUsers;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#deleteUser(net.rrm.ehour.user.domain.User)
	 */
	public void deleteUser(Integer userId)
	{
		User	user = userDAO.findById(userId);

		timesheetService.deleteTimesheetEntries(user);
		
		userDAO.delete(user);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#deleteDepartment(java.lang.Integer)
	 */
	public void deleteDepartment(Integer departmentId)
	{
		UserDepartment department = userDepartmentDAO.findById(departmentId);

		logger.info("Deleting department: " + department);
		
		for (User user : department.getUsers()) 
		{
			logger.info("Deleting user: " + user);
			
			deleteUser(user.getUserId());
		}
		
		userDepartmentDAO.delete(department);
	}
	
	/**
	 * @param timesheetService the timesheetService to set
	 */
	public void setTimesheetService(TimesheetService timesheetService)
	{
		this.timesheetService = timesheetService;
	}
}
