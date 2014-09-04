package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.persistence.user.dao.UserRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 12/6/10 11:47 PM
 */
@Service("userRoleParserDao")
public class UserRoleParserDaoPersistImpl implements UserRoleParserDao {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public UserRole findUserRole(String role) {
        return userRoleDao.findById(role);
    }

    @Override
    public User findUser(Integer userId) {
        return userDao.findById(userId);
    }

    @Override
    public void persistUser(User user) {
        userDao.persist(user);
    }
}
