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

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userDao")
public class UserDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<User, Integer> implements UserDao {
    private static final String CACHEREGION = "query.User";

    public UserDaoHibernateImpl() {
        super(User.class);
    }

    public User findByUsername(String username) {
        List<User> l = findByNamedQueryAndNamedParam("User.findByUsername", "username", username, true, CACHEREGION);

        return l.size() > 0 ? l.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    public List<User> findUsers(boolean onlyActive) {
        return onlyActive ? getHibernateTemplate().findByNamedQuery("User.findActiveUsers") : getHibernateTemplate().loadAll(User.class);
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public List<User> findActiveUsers() {
        return findUsers(true);
    }

    public List<User> findUsersForDepartments(String pattern, List<UserDepartment> departments, boolean onlyActive) {
        String hql;
        String[] paramKeys;
        Object[] paramValues;

        pattern = patternToSqlPattern(pattern);

        hql = (onlyActive) ? "User.findActiveByNamePatternForDepartment" :
                "User.findNamePatternForDepartment";

        paramKeys = new String[]{"pattern", "departments"};
        paramValues = new Object[]{pattern, departments.toArray()};

        return findByNamedQueryAndNamedParam(hql, paramKeys, paramValues, true, CACHEREGION);
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllActiveUsersWithEmailSet() {
        return getHibernateTemplate().findByNamedQuery("User.findAllActiveUsersWithEmailSet");
    }

    public void deletePmWithoutProject() {
        Query q = getSession().getNamedQuery("User.deletePMsWithoutProject");
        q.executeUpdate();
    }
}
