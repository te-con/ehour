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
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;
import java.util.Set;


public interface UserService {
    /**
     * Get user by userId
     * User.inactiveProjectAssignments is populated with project assignments ending before or starting after the current date
     */
    User getUser(Integer userID) throws ObjectNotFoundException;

    /**
     * Get user by userId and optional check if the user is deletable (as in, no hours booked on his/her assignments)
     */
    User getUserAndCheckDeletability(Integer userId) throws ObjectNotFoundException;

    User getUser(String username);

    User persistEditedUser(User user) throws ObjectNotUniqueException;

    void persistNewUser(User user, String password) throws ObjectNotUniqueException;

    void changePassword(String username, String newUnencryptedPassword);

    // get all users, active and inactive
    List<User> getUsers();

    // get all active users
    List<User> getActiveUsers();

    /**
     * Get all active users with @param userRole
     */
    List<User> getUsers(UserRole userRole);

    List<User> getUsersWithEmailSet();

    List<UserDepartment> getUserDepartments();

    UserDepartment persistUserDepartment(UserDepartment department) throws ObjectNotUniqueException;

    UserDepartment getUserDepartment(Integer departmentId) throws ObjectNotFoundException;

    List<UserRole> getUserRoles();

    /**
     * Add new role to user.
     * @param userId
     * @param newRole {@link UserRole}
     * @return
     */
    public User addRole(Integer userId, UserRole newRole);
    
    /**
     * Cascading delete of user
     */
    User validateProjectManagementRoles(Integer userId);

    void deleteUser(Integer userId);

    void deleteDepartment(Integer departmentId);

    void changePassword(String username, String currentPassword, String newUnencryptedPassword) throws BadCredentialsException;

    /**
     * Returns a list of all {@link User}s who are working on Activities of Projects of passed Customers
     * @param customers
     * @param onlyActiveUsers
     * @return
     */
    List<User> getAllUsersAssignedToCustomers(List<Customer> customers, boolean onlyActiveUsers);
}
