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

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;

import java.util.List;
import java.util.Set;


public interface UserService
{
    User getUser(Integer userID) throws ObjectNotFoundException;

    /**
     * Persist a user
     */
    public void editUser(User user);

    /**
     * Get all active users
     * @return
     */
    List<User> getUsers();

    /**
     * get users with role
     * @param userRole
     * @return
     */
    List<User> getUsers(UserRole userRole);

    List<LdapUser> getLdapUser(String ldapUid);

    User getAuthorizedUser(String ldapUid);

    /**
     * Get all user roles
     * @return
     */
    public List<UserRole> getUserRoles();

    /**
     * Add new role to user.
     * @param userId
     * @param newRole {@link UserRole}
     * @return
     */
    public User addRole(Integer userId, UserRole newRole);

    /**
     * Returns a list of all {@link User}s who are working on Activities of Projects of passed Customers
     * @param customers
     * @param onlyActiveUsers
     * @return
     */
    public Set<User> getAllUsersAssignedToCustomers(List<Customer> customers, boolean onlyActiveUsers);

    public boolean isLdapUserMemberOf(String userId, String  groupDn );

    List<LdapUser> getLdapUsers(String match, boolean authorizedOnly);
}
