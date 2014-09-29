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
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.persistence.activity.dao.ActivityDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserRoleDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
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
    private UserRoleDao userRoleDAO;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private LdapTemplate ldapTemplate;

    public List<User> getUsers() {
        return userDAO.findActiveUsers();
    }

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
    public User getAuthorizedUser(String ldapUid) {

        User user = userDAO.findByUsername(ldapUid);

        if (user != null && user.isActive()) {
            String filter = String.format("(&(uid=%s)(objectClass=person))", ldapUid);

            List cn = ldapTemplate.search("", filter, new AttributesMapper() {
                public Object mapFromAttributes(Attributes attrs) throws NamingException {
                    return attrs.get("cn").get();
                }
            });

            if (cn.size() > 1) {
                LOGGER.warn(String.format("Multiple LDAP entries found for uid %s", ldapUid));
            }

            if (cn.size() > 0) {
                user.setName((String) cn.get(0));
            } else {
                LOGGER.warn(String.format("No LDAP entry found for uid %s", ldapUid));
            }
        } else {
            user = null;
        }

        return user;
    }

    public List<UserRole> getUserRoles() {
        return Lists.newArrayList(UserRole.ROLES.values());
    }

    @Transactional(readOnly = true)
    public User getUser(Integer userId) throws ObjectNotFoundException {
        return userDAO.findById(userId);
    }
    /**
     * Persist user
     */
    @Transactional
    public void editUser(User user) {
        userDAO.persist(user);
    }


    /**
     * Find user on id and add PM role
     *
     * @param userId
     * @param newRole {@link UserRole} to be added to the User's set of Roles.
     * @throws ObjectNotUniqueException
     */
    private User getUserAndAddRole(Integer userId, UserRole newRole) throws ObjectNotUniqueException {
        User user = userDAO.findById(userId);

        UserRole userRole = userRoleDAO.findById(newRole.getRole());

        user.getUserRoles().add(userRole);

        userDAO.persist(user);

        return user;
    }

    @Override
    public Set<User> getAllUsersAssignedToCustomers(List<Customer> customers, boolean onlyActiveUsers) {
        Set<User> result = new HashSet<User>();
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

    @Override
    @Transactional
    public User addRole(Integer userId, UserRole newRole) {
        User user = null;
        try {
            if (userId != null) {
                user = getUserAndAddRole(userId, newRole);
                userDAO.cleanRedundantRoleInformation(newRole);
            }
        } catch (ObjectNotUniqueException exc) {
            // won't happen
            LOGGER.error("Account already exists", exc);
        }
        return user;
    }


    public void setUserDAO(UserDao userDAO) {
        this.userDAO = userDAO;
    }


    public void setUserRoleDAO(UserRoleDao userRoleDAO) {
        this.userRoleDAO = userRoleDAO;
    }

    // LLI
    public boolean isLdapUserMemberOf(String userId, String  groupDn ){
        ContextMapper contextMapper = new LdapContextMapper();
        String filter = String.format("(&(uid=%s)(objectClass=person)(isMemberOf=%s))", userId, groupDn);

        List<LdapUser> ldapUser = (List<LdapUser> )ldapTemplate.search("", filter, contextMapper);
        if ( ldapUser.size() == 1 ) return true;
        else return false;
    }


    private static class LdapContextMapper implements ContextMapper {
        @Override
        public Object mapFromContext(Object ctx) {
            DirContextAdapter context = (DirContextAdapter) ctx;

            String cn = context.getStringAttribute("cn");
            String uid = context.getStringAttribute("uid");
            String mail = context.getStringAttribute("mail");
            Name dn = context.getDn();

            StringBuilder dnBuilder = new StringBuilder();
            if (dn != null) {
                for (int i = dn.size() - 1; i >= 0; i--) {
                    dnBuilder.append(dn.get(i));
                    dnBuilder.append(',');
                }
            }

            String fullDn = dnBuilder.toString();

            return new LdapUser(cn, uid, mail, fullDn.substring(0, fullDn.length() - 1));
        }
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }
}
