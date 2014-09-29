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

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.persistence.activity.dao.ActivityDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserDepartmentDao;
import net.rrm.ehour.persistence.user.dao.UserRoleDao;
import net.rrm.ehour.timesheet.service.IDeleteTimesheetEntry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    private ActivityDao activityDao;

    @Autowired
    private UserRoleDao userRoleDAO;

    @Autowired
    private IDeleteTimesheetEntry deleteTimesheetEntryService;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Transactional(readOnly = true)
    public User getUser(Integer userId) throws ObjectNotFoundException {
        return userDAO.findById(userId);
    }

    @Transactional(readOnly = true)
    public User getUserAndCheckDeletability(Integer userId) throws ObjectNotFoundException {
        User user = getUser(userId);

        // TODO Not proper implemented, never ported with activity
        LOGGER.info("Retrieved user " + user.getUsername() + ", deletable: " + user.isDeletable());

        return user;
    }

    @Transactional(readOnly = true)
    public User getUser(String username) {
        return userDAO.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<UserDepartment> getUserDepartments() {
        return userDepartmentDAO.findAll();
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

    @Transactional(readOnly = true)
    public UserDepartment getUserDepartment(Integer departmentId) throws ObjectNotFoundException {
        UserDepartment userDepartment = userDepartmentDAO.findById(departmentId);

        if (userDepartment == null) {
            throw new ObjectNotFoundException("Department not found");
        }

        userDepartment.setDeletable(userDepartment.getUsers() == null || userDepartment.getUsers().size() == 0);

        return userDepartment;
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userDAO.findUsers(false);
    }

    @Transactional(readOnly = true)
    public List<User> getActiveUsers() {
        return userDAO.findActiveUsers();
    }

    @Transactional(readOnly = true)
    public List<UserRole> getUserRoles() {
        return Lists.newArrayList(UserRole.ROLES.values());
    }

    @Transactional
    public User persistEditedUser(User user) throws ObjectNotUniqueException {
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
        User dbUser = userDAO.findById(user.getUserId());

        if (dbUser == null) {
            throw new IllegalArgumentException(String.format("%d user ID not found", user.getUserId()));
        }
        return dbUser;
    }

    @Override
    @Transactional
    public void persistNewUser(User user, String password) throws ObjectNotUniqueException {
        // check username uniqueness
        User dbUser = userDAO.findByUsername(user.getUsername());

        if (dbUser != null && !dbUser.getUserId().equals(user.getUserId())) {
            throw new ObjectNotUniqueException("Username already in use");
        }

        // encrypt password
        user.setSalt((int) (Math.random() * 10000));

        userDAO.persist(user);
    }

    @Override
    public List<User> getAllUsersAssignedToCustomers(List<Customer> customers, boolean onlyActiveUsers) {
        List<User> result = Lists.newArrayList();
        List<Activity> activities = activityDao.findActivitiesForCustomers(customers);
        for (Activity activity : activities) {
            if (onlyActiveUsers) {
                if (activity.getAssignedUser() != null && activity.getAssignedUser().isActive()) {
                    result.add(activity.getAssignedUser());
                }
            } else {
                result.add(activity.getAssignedUser());
            }
        }
        return result;
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
    public List<User> getUsers() {
        return userDAO.findAllActiveUsers();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LdapUser> getLdapUsers(final String match, final boolean authorizedOnly) {
        ContextMapper contextMapper = new LdapContextMapper();

        List<LdapUser> ldapUsers = (List<LdapUser>) ldapTemplate.search("", "(objectClass=person)", contextMapper);

        List<User> users = userDAO.findAll();

        for (User user : users) {
            for (LdapUser ldapUser : ldapUsers) {
                if (user.getUsername().equalsIgnoreCase(ldapUser.uid)) {
                    ldapUser.setUser(user);
                }
            }
        }

        applyRestrictions(match, authorizedOnly, ldapUsers);

        sortLdapUsers(ldapUsers);

        return ldapUsers;
    }

    @SuppressWarnings("unchecked")
    public List<LdapUser> getLdapUser(String ldapUid) {
        ContextMapper contextMapper = new LdapContextMapper();

        String filter = String.format("(&(uid=%s)(objectClass=person))", ldapUid);
        return (List<LdapUser>) ldapTemplate.search("", filter, contextMapper);

    }

    private void sortLdapUsers(List<LdapUser> ldapUsers) {
        Collections.sort(ldapUsers, new Comparator<LdapUser>() {
            @Override
            public int compare(LdapUser o1, LdapUser o2) {
                return (o1.fullName == null && o2 != null) ? 1 :
                        (o1.fullName == null && o2 == null) ? 0 :
                                (o1.fullName != null && o2 == null) ? -1 :
                                        o1.fullName.compareTo(o2.fullName);
            }
        });
    }

    private void applyRestrictions(final String match, final boolean authorizedOnly, List<LdapUser> ldapUsers) {
        CollectionUtils.filter(ldapUsers, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                LdapUser user = (LdapUser) object;

                boolean isMatch = (match == null || "".equals(match)) || StringUtils.containsIgnoreCase(user.fullName, match);

                return (!authorizedOnly || user.isAuthorized()) && isMatch;
            }
        });
    }

    @Override
    @Transactional
    public User addRole(Integer userId, UserRole newRole) {
        User user = null;
        try {
            if (userId != null) {
                user = getUserAndAddRole(userId, newRole);
            }
        } catch (ObjectNotUniqueException exc) {
            // won't happen
            LOGGER.error("Account already exists", exc);
        }
        return user;
    }


    @Transactional
    public void deleteUser(Integer userId) {
        User user = userDAO.findById(userId);

        deleteTimesheetEntryService.deleteAllTimesheetDataForUser(user);

        userDAO.delete(user);
    }

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

    public void setUserDAO(UserDao dao) {
        userDAO = dao;
    }

    public void setUserDepartmentDAO(UserDepartmentDao dao) {
        userDepartmentDAO = dao;
    }

    public void setUserRoleDAO(UserRoleDao dao) {
        userRoleDAO = dao;
    }


    private User getUserAndAddRole(Integer userId, UserRole newRole) throws ObjectNotUniqueException {
        User user = userDAO.findById(userId);

        UserRole userRole = userRoleDAO.findById(newRole.getRole());

        user.getUserRoles().add(userRole);

        userDAO.persist(user);

        return user;
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }
}