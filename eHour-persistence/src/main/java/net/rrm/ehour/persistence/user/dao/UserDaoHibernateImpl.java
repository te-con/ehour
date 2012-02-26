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

import java.util.List;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<User, Integer> implements UserDao
{
	private final static String	CACHEREGION = "query.User";
	
	public UserDaoHibernateImpl()
	{
		super(User.class);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.user.dao.UserDAO#findByUsername(java.lang.String)
	 */
	public User findByUsername(String username)
	{
		List<User> l = findByNamedQueryAndNamedParam("User.findByUsername", "username", username, true, CACHEREGION);

        return  l.size() > 0 ? l.get(0) : null;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.user.dao.UserDAO#findByUsernameAndPassword(java.lang.String, java.lang.String)
	 */
	public User findByUsernameAndPassword(String username, String password)
	{
		String[] paramKeys = new String[]{"username", "password"};
		Object[] paramValues = new String[]{username, password};

        List<User> users = findByNamedQueryAndNamedParam("User.findByUsernameAndPassword", paramKeys, paramValues, true, CACHEREGION);

        return users.size() > 0 ? users.get(0) : null;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.user.dao.UserDAO#findUsersByNameMatch(java.lang.String, boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUsers(boolean onlyActive)
	{
        return onlyActive ? getHibernateTemplate().findByNamedQuery("User.findActiveUsers") : getHibernateTemplate().loadAll(User.class);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<User> findActiveUsers() {
        return findUsers(true);
    }

	public List<User> findUsersForDepartments(String pattern, List<UserDepartment> departments, boolean onlyActive)
	{
		String		hql;
		String[]	paramKeys;
		Object[]	paramValues;
		
		pattern = patternToSqlPattern(pattern);		
		
		hql = (onlyActive) ? "User.findActiveByNamePatternForDepartment" :
			  				 "User.findNamePatternForDepartment";
		
		paramKeys = new String[]{"pattern", "departments"};
		paramValues = new Object[]{pattern, departments.toArray()}; 
		
		return findByNamedQueryAndNamedParam(hql, paramKeys, paramValues, true, CACHEREGION);
	}

	@SuppressWarnings("unchecked")
	public List<User> findAllActiveUsersWithEmailSet()
	{
		return getHibernateTemplate().findByNamedQuery("User.findAllActiveUsersWithEmailSet");
	}

	public void deletePmWithoutProject()
	{
		Query q = getSession().getNamedQuery("User.deletePMsWithoutProject");
		q.executeUpdate();
	}
}
