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
		User		user = null;
		List<User>	l;
		
		l = findByNamedQueryAndNamedParam("User.findByUsername", "username", username, true, CACHEREGION);
		
		if (l.size() > 0)
		{
			user = (User)l.get(0);
		}
		
		return user;	
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.user.dao.UserDAO#findByUsernameAndPassword(java.lang.String, java.lang.String)
	 */
	public User findByUsernameAndPassword(String username, String password)
	{
		String[] paramKeys = new String[]{"username", "password"};
		Object[] paramValues = new String[]{username, password};
		User		user = null;
		List<User> users;
		
		users = findByNamedQueryAndNamedParam("User.findByUsernameAndPassword", paramKeys, paramValues, true, CACHEREGION);
		
		if (users.size() > 0)
		{
			user = (User)users.get(0);
		}
		
		return user;	
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.user.dao.UserDAO#findUsersByNameMatch(java.lang.String, boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUsersByNameMatch(String pattern, boolean onlyActive)
	{
		String	hql;
		
		if (pattern != null && !pattern.trim().equals(""))
		{
			pattern = pattern.toLowerCase();
			pattern = "%" + pattern + "%";
		}
		else
		{
			pattern = "%";
		}
		
		hql = (onlyActive) ? "User.findActiveByUsernamePattern" :
							  "User.findByUsernamePattern";
		
		return getHibernateTemplate().findByNamedQueryAndNamedParam(hql,
																"pattern", pattern);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<User> findAllActiveUsers()
	{
		return getHibernateTemplate().findByNamedQuery("User.findAllActiveUsers");
	}

	
	/**
	 * Find users for departments with filter pattern and active flag
	 * @param pattern
	 * @param departmentIds
	 * @param onlyActive
	 * @return
	 */	
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

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.user.dao.UserDAO#findAllActiveUsersWithEmailSet()
	 */
	@SuppressWarnings("unchecked")
	public List<User> findAllActiveUsersWithEmailSet()
	{
		return getHibernateTemplate().findByNamedQuery("User.findAllActiveUsersWithEmailSet");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.user.dao.UserDAO#deletePmWithoutProject()
	 */
	public void deletePmWithoutProject()
	{
		Query q = getSession().getNamedQuery("User.deletePMsWithoutProject");
		q.executeUpdate();
	}
}
