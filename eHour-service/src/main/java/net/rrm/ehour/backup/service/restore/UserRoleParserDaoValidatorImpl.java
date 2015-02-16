package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 11/30/10 12:30 AM
 */
public class UserRoleParserDaoValidatorImpl implements UserRoleParserDao {
    private int findUserCount = 0;
    private int persistCount = 0;

    @Override
    public UserRole findUserRole(String role) {
        return UserRole.ROLES.get(role);
    }

    @Override
    public User findUser(Integer userId) {
        findUserCount++;
        return new User(userId);
    }

    @Override
    public void persistUser(User user) {
        persistCount++;
    }

    public int getPersistCount() {
        return persistCount;
    }

    public int getFindUserCount() {
        return findUserCount;
    }
}
