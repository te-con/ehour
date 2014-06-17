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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.persistence.user.dao.UserRoleDao;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.util.ReportUtil;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.timesheet.service.IDeleteTimesheetEntry;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.util.DomainUtil;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Thies Edeling (thies@te-con.nl)
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDAO;
    @Autowired
    private UserDepartmentDao userDepartmentDAO;
    @Autowired
    private UserRoleDao userRoleDAO;
    @Autowired
    private ProjectAssignmentManagementService projectAssignmentManagementService;
    @Autowired
    private AggregateReportService aggregateReportService;
    @Autowired
    private IDeleteTimesheetEntry deleteTimesheetEntryService;

    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;


    public User getUser(Integer userId) throws ObjectNotFoundException {
        User user = userDAO.findById(userId);
        Set<ProjectAssignment> inactiveAssignments = new HashSet<ProjectAssignment>();
        Date currentDate = new Date();

        if (user != null && user.getProjectAssignments() != null) {
            for (ProjectAssignment assignment : user.getProjectAssignments()) {
                DateRange assignmentRange = new DateRange(assignment.getDateStart(), assignment.getDateEnd());

                if ((!DateUtil.isDateWithinRange(currentDate, assignmentRange)) ||
                        (assignment.getProject() == null || !assignment.getProject().isActive())) {
                    inactiveAssignments.add(assignment);
                }
            }

            user.getProjectAssignments().removeAll(inactiveAssignments);
            user.setInactiveProjectAssignments(inactiveAssignments);
        } else {
            throw new ObjectNotFoundException("User not found");
        }

        return user;
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.user.service.UserService#getUserAndCheckDeletability(java.lang.Integer)
      */
    public User getUserAndCheckDeletability(Integer userId) throws ObjectNotFoundException {
        User user = getUser(userId);

        if ((user.getProjectAssignments() == null || user.getProjectAssignments().size() == 0) &&
                (user.getInactiveProjectAssignments() == null || user.getInactiveProjectAssignments().size() == 0)) {
            user.setDeletable(true);
        } else {
            // bummer, we need to check if the user booked any hours on the assignments
            List<Integer> assignmentIds = new ArrayList<Integer>();

            assignmentIds.addAll(DomainUtil.getIdsFromDomainObjects(user.getProjectAssignments()));
            assignmentIds.addAll(DomainUtil.getIdsFromDomainObjects(user.getInactiveProjectAssignments()));

            List<AssignmentAggregateReportElement> aggregates = aggregateReportService.getHoursPerAssignment(assignmentIds);

            user.setDeletable(ReportUtil.isEmptyAggregateList(aggregates));
        }

        LOGGER.info("Retrieved user " + user.getUsername() + ", deletable: " + user.isDeletable());

        return user;
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.user.service.UserService#getUser(java.lang.String)
      */
    public User getUser(String username) {
        return userDAO.findByUsername(username);
    }

    public List<UserDepartment> getUserDepartments() {
        return userDepartmentDAO.findAll();
    }

    public void setUserDAO(UserDao dao) {
        userDAO = dao;
    }

    public void setUserDepartmentDAO(UserDepartmentDao dao) {
        userDepartmentDAO = dao;
    }

    public void setUserRoleDAO(UserRoleDao dao) {
        userRoleDAO = dao;
    }

    @Transactional
    public UserDepartment persistUserDepartment(UserDepartment department) throws ObjectNotUniqueException {
        UserDepartment otherDept;

        otherDept = userDepartmentDAO.findOnNameAndCode(department.getName(), department.getCode());

        if (otherDept == null) {
            userDepartmentDAO.persist(department);
        } else if (otherDept.getDepartmentId().equals(department.getDepartmentId())) {
            userDepartmentDAO.merge(department);
        } else {
            throw new ObjectNotUniqueException("name/code not unique");
        }

        return department;

    }

    public UserDepartment getUserDepartment(Integer departmentId) throws ObjectNotFoundException {
        UserDepartment userDepartment = userDepartmentDAO.findById(departmentId);

        if (userDepartment == null) {
            throw new ObjectNotFoundException("Department not found");
        }

        userDepartment.setDeletable(userDepartment.getUsers() == null || userDepartment.getUsers().size() == 0);

        return userDepartment;
    }

    public List<User> getUsers() {
        return userDAO.findUsers(false);
    }

    public List<User> getActiveUsers() {
        return userDAO.findActiveUsers();
    }


    public UserRole getUserRole(String userRoleId) {
        return userRoleDAO.findById(userRoleId);
    }


    /**
     * Get the assignable user roles
     */
    public List<UserRole> getUserRoles() {
        List<UserRole> userRoles = userRoleDAO.findAll();

        userRoles.remove(UserRole.PROJECTMANAGER);

        return userRoles;
    }

    /**
     * Persist user
     */
    @Transactional
    public User editUser(User user) throws ObjectNotUniqueException {
        // check username uniqueness
        User dbUser = userDAO.findByUsername(user.getUsername());

        if (dbUser != null && !dbUser.getUserId().equals(user.getUserId())) {
            throw new ObjectNotUniqueException("Username already in use");
        } else if (dbUser == null) {
            dbUser = findUserOnId(user);
        }

        dbUser.setActive(user.isActive());
        dbUser.setEmail(user.getEmail());
        dbUser.setFirstName(user.getFirstName());
        dbUser.setLastName(user.getLastName());
        dbUser.setUserDepartment(user.getUserDepartment());
        dbUser.setUsername(user.getUsername());

        boolean reAddPm = dbUser.getUserRoles().contains(UserRole.PROJECTMANAGER);
        dbUser.setUserRoles(user.getUserRoles());

        if (reAddPm && !user.getUserRoles().contains(UserRole.PROJECTMANAGER)) {
            dbUser.addUserRole(UserRole.PROJECTMANAGER);
        }

        userDAO.persist(dbUser);

        return dbUser;
    }

    private User findUserOnId(User user) {
        User dbUser;
        dbUser = userDAO.findById(user.getUserId());

        if (dbUser == null) {
            throw new IllegalArgumentException(String.format("%d user ID not found", user.getUserId()));
        }
        return dbUser;
    }

    @Override
    @Transactional
    public void newUser(User user, String password) throws ObjectNotUniqueException {
        // check username uniqueness
        User dbUser = userDAO.findByUsername(user.getUsername());

        if (dbUser != null && !dbUser.getUserId().equals(user.getUserId())) {
            throw new ObjectNotUniqueException("Username already in use");
        }

        // encrypt password
        user.setSalt((int) (Math.random() * 10000));
        user.setPassword(encryptPassword(password, user.getSalt()));

        userDAO.persist(user);

        // assign new users to default projects
        projectAssignmentManagementService.assignUserToDefaultProjects(user);
    }

    @Override
    @Transactional
    public void changePassword(String username, String currentPassword, String newUnencryptedPassword) throws BadCredentialsException {
        User user = userDAO.findByUsername(username);

        Validate.notNull(user, String.format("Can't find user with username %s", username));

        String encryptedCurrentPassword = encryptPassword(currentPassword, user.getSalt());

        if (!user.getPassword().equals(encryptedCurrentPassword)) {
            throw new BadCredentialsException("Invalid current password");
        }

        changePassword(user, newUnencryptedPassword);
    }

    @Override
    @Transactional
    public void changePassword(String username, String newUnencryptedPassword) {
        User user = userDAO.findByUsername(username);
        Validate.notNull(user, String.format("Can't find user with username %s", username));

        changePassword(user, newUnencryptedPassword);
    }

    private void changePassword(User user, String newUnencryptedPassword) {
        int salt = (int) (Math.random() * 10000);
        user.setSalt(salt);
        user.setPassword(encryptPassword(newUnencryptedPassword, salt));

        userDAO.persist(user);
    }


    /**
     * Encrypt password (sha1)
     */
    private String encryptPassword(String plainPassword, Object salt) {
        return passwordEncoder.encodePassword(plainPassword, salt);
    }

    public List<User> getUsersWithEmailSet() {
        return userDAO.findAllActiveUsersWithEmailSet();
    }

    @Override
    @Transactional
    public User validateProjectManagementRoles(Integer userId) {
        User user = null;
        try {
            if (userId != null) {
                user = getAndAddPmRole(userId);
            }

            userDAO.deletePmWithoutProject();
        } catch (ObjectNotUniqueException e) {
            // won't happen
            LOGGER.error("Account already exists", e);
        }

        return user;
    }


    /**
     * Find user on id and add PM role
     */
    private User getAndAddPmRole(Integer userId) throws ObjectNotUniqueException {
        User user = userDAO.findById(userId);

        UserRole userRole = userRoleDAO.findById(UserRole.ROLE_PROJECTMANAGER);

        user.getUserRoles().add(userRole);

        userDAO.persist(user);

        return user;
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.user.service.UserService#getActiveUsers(net.rrm.ehour.persistence.persistence.user.domain.UserRole)
      */
    @Override
    public List<User> getUsers(UserRole userRole) {

        List<User> users = userDAO.findActiveUsers();
        List<User> validUsers = new ArrayList<User>();

        // result of bad many-to-many mapping. should fix once..
        for (User user : users) {
            if (user.getUserRoles().contains(userRole)) {
                validUsers.add(user);
            }
        }

        return validUsers;
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.user.service.UserService#deleteUser(net.rrm.ehour.persistence.persistence.user.domain.User)
      */
    @Transactional
    public void deleteUser(Integer userId) {
        User user = userDAO.findById(userId);

        deleteTimesheetEntryService.deleteAllTimesheetDataForUser(user);

        userDAO.delete(user);
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.user.service.UserService#deleteDepartment(java.lang.Integer)
      */
    @Transactional
    public void deleteDepartment(Integer departmentId) {
        UserDepartment department = userDepartmentDAO.findById(departmentId);

        LOGGER.info("Deleting department: " + department);

        for (User user : department.getUsers()) {
            LOGGER.info("Deleting user: " + user);

            deleteUser(user.getUserId());
        }

        userDepartmentDAO.delete(department);
    }

    public void setPasswordEncoder(MessageDigestPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setProjectAssignmentManagementService(ProjectAssignmentManagementService projectAssignmentManagementService) {
        this.projectAssignmentManagementService = projectAssignmentManagementService;
    }
}
