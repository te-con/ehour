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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.NoResultsException;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.user.dao.UserDAO;
import net.rrm.ehour.user.dao.UserDepartmentDAO;
import net.rrm.ehour.user.dao.UserRoleDAO;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.util.EhourUtil;

import org.acegisecurity.providers.encoding.MessageDigestPasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author   Thies
 */
@Service("userService")
public class UserServiceImpl implements UserService
{
	private	static final Logger	LOGGER = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private	UserDAO				userDAO;
	@Autowired
	private	UserDepartmentDAO	userDepartmentDAO;
	@Autowired
	private	UserRoleDAO			userRoleDAO;
	@Autowired
	private	ProjectAssignmentManagementService		projectAssignmentManagementService;
	@Autowired
	private	AggregateReportService		aggregateReportService;
	@Autowired
	private TimesheetService	timesheetService;

	@Autowired
	private MessageDigestPasswordEncoder	passwordEncoder;

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
				DateRange assignmentRange = new DateRange(assignment.getDateStart(), assignment.getDateEnd());
				
				if ((!DateUtil.isDateWithinRange(currentDate , assignmentRange)) ||
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
			List<Serializable> assignmentIds = new ArrayList<Serializable>();

			assignmentIds.addAll(EhourUtil.getIdsFromDomainObjects(user.getProjectAssignments()));
			assignmentIds.addAll(EhourUtil.getIdsFromDomainObjects(user.getInactiveProjectAssignments()));
			
			List<AssignmentAggregateReportElement> aggregates =aggregateReportService.getHoursPerAssignment(assignmentIds);
			
			user.setDeletable(EhourUtil.isEmptyAggregateList(aggregates));
		}
		
		LOGGER.info("Retrieved user " + user.getUsername() + ", deletable: " + user.isDeletable());
		
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

	@Transactional
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
	 */
	@Transactional
	public User persistUser(User user) throws PasswordEmptyException, ObjectNotUniqueException
	{
		User	dbUser;

		LOGGER.info("Persisting user: " + user);

		// check username uniqueness
		dbUser = userDAO.findByUsername(user.getUsername());
				
		if (dbUser != null && !dbUser.getUserId().equals(user.getUserId()))
		{
			throw new ObjectNotUniqueException("Username already in use");
		}
		
		// copy over password or encrypt new one
		if (StringUtils.isEmpty(user.getUpdatedPassword()))
		{
			// if password is empty and user is new we have a problem
			if (user.getUserId() == null)
			{
				throw new PasswordEmptyException("New users need a password");
			}
			
			user.setPassword(dbUser.getPassword());
			user.setSalt(dbUser.getSalt());
		}
		else
		{
			user.setSalt((int)(Math.random() * 10000));
			user.setPassword(encryptPassword(user.getUpdatedPassword(), user.getSalt()));
		}
		
		// assign new users to default projects
		if (user.getUserId() == null)
		{
			projectAssignmentManagementService.assignUserToDefaultProjects(user);
		}
		
		userDAO.merge(user);
			
		return user;
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
	 * @see net.rrm.ehour.user.service.UserService#addAndcheckProjectManagementRoles(java.lang.Integer)
	 */
	@Transactional
	public User addAndcheckProjectManagementRoles(Integer userId)
	{
		User user = null;
		try
		{
			if (userId != null)
			{
				user = getAndAddPmRole(userId);
			}
			
			userDAO.deletePmWithoutProject();
		} catch (PasswordEmptyException e)
		{
			// won't happen
			e.printStackTrace();
		} catch (ObjectNotUniqueException e)
		{
			// won't happen
			e.printStackTrace();
		}
		
		return user;
	}
	
	
	/**
	 * Find user on id and add PM role
	 * @param userId
	 * @throws ObjectNotUniqueException 
	 * @throws PasswordEmptyException 
	 */
	private User getAndAddPmRole(Integer userId) throws PasswordEmptyException, ObjectNotUniqueException
	{
		User user = userDAO.findById(userId);
		
		UserRole userRole = userRoleDAO.findById(UserRole.ROLE_PROJECTMANAGER);
		
		user.getUserRoles().add(userRole);
		
		userDAO.persist(user);
		
		return user;
	}

	/**
	 * Encrypt password (sha1)
	 * @param plainPassword
	 * @return
	 */
	private String encryptPassword(String plainPassword, Object salt)
	{
		return passwordEncoder.encodePassword(plainPassword, salt);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.service.UserService#getUsers(net.rrm.ehour.user.domain.UserRole)
	 */
	public List<User> getUsers(UserRole userRole)
	{
		LOGGER.debug("Finding users on role");
		List<User> users = getUsersByNameMatch(null, true, userRole);
		
//		userDAO.initializeObject(users);
		return users;
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
	@Transactional
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
	@Transactional
	public void deleteDepartment(Integer departmentId)
	{
		UserDepartment department = userDepartmentDAO.findById(departmentId);

		LOGGER.info("Deleting department: " + department);
		
		for (User user : department.getUsers()) 
		{
			LOGGER.info("Deleting user: " + user);
			
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

	/**
	 * @param aggregateReportService the aggregateReportService to set
	 */
	public void setAggregateReportService(AggregateReportService aggregateReportService)
	{
		this.aggregateReportService = aggregateReportService;
	}

	/**
	 * @param passwordEncoder the passwordEncoder to set
	 */
	public void setPasswordEncoder(MessageDigestPasswordEncoder passwordEncoder)
	{
		this.passwordEncoder = passwordEncoder;
	}

	public void setProjectAssignmentManagementService(ProjectAssignmentManagementService projectAssignmentManagementService)
	{
		this.projectAssignmentManagementService = projectAssignmentManagementService;
	}
}
