package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;

/**
 * User: thies
 * Date: 11/29/10
 * Time: 11:30 PM
 */
public interface UserRoleParserDao
{
    public UserRole findUserRole(String role);

    public User findUser(Integer userId);

    public void persistUser(User user);

}
