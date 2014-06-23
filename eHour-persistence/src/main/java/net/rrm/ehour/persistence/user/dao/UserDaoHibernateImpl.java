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

package net.rrm.ehour.persistence.user.dao;

import com.google.common.base.Optional;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userDao")
public class UserDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<User, Integer> implements UserDao {
    private static final Optional<String> CACHEREGION = Optional.of("query.User");

    public UserDaoHibernateImpl() {
        super(User.class);
    }

    @Override
    public User findByUsername(String username) {
        List<User> l = findByNamedQueryAndNamedParam("User.findByUsername", "username", username, CACHEREGION);

        return l.size() > 0 ? l.get(0) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUsers(boolean onlyActive) {
        return onlyActive ? findByNamedQuery("User.findActiveUsers", CACHEREGION) : findAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findActiveUsers() {
        return findUsers(true);
    }

    @Override
    public List<User> findUsersForDepartments(List<UserDepartment> departments, boolean onlyActive) {
        String hql;
        String[] paramKeys;
        Object[] paramValues;

        hql = (onlyActive) ? "User.findActiveForDepartment" :
                "User.findForDepartment";

        paramKeys = new String[]{"departments"};
        paramValues = new Object[]{departments.toArray()};

        return findByNamedQueryAndNamedParam(hql, paramKeys, paramValues, CACHEREGION);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findAllActiveUsersWithEmailSet() {
        return findByNamedQuery("User.findAllActiveUsersWithEmailSet", CACHEREGION);
    }

    @Override
    public void deletePmWithoutProject() {
        Query q = getSession().getNamedQuery("User.deletePMsWithoutProject");
        q.executeUpdate();
    }
}
