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
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userDepartmentDao")
public class UserDepartmentDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<UserDepartment, Integer> implements UserDepartmentDao {
    private static final Optional<String> CACHEREGION = Optional.of("query.Department");

    public UserDepartmentDaoHibernateImpl() {
        super(UserDepartment.class);
    }

    public UserDepartment findOnNameAndCode(String name, String code) {
        List<UserDepartment> results;

        String[] keys = new String[]{"name", "code"};
        Object[] params = new Object[]{name.toLowerCase(), code.toLowerCase()};

        results = findByNamedQueryAndNamedParam("UserDepartment.findByNameAndCode", keys, params, CACHEREGION);

        return (results.size() > 0) ? results.get(0) : null;
    }
}
