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

package net.rrm.ehour.user.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;

import org.hibernate.Query;


public class UserDAOHibernateImpl extends GenericDAOHibernateImpl<User, Integer> implements UserDAO
{
	private final static String	CACHEREGION = "query.User";
	
	public UserDAOHibernateImpl()
	{
		super(User.class);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.dao.UserDAO#findByUsername(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
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
	 * @see net.rrm.ehour.user.dao.UserDAO#findByUsernameAndPassword(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public User findByUsernameAndPassword(String username, String password)
	{
		String[]	paramKeys;
		Object[]	paramValues;
		User		user = null;
		List		l;
		
		paramKeys = new String[]{"username", "password"};
		paramValues = new String[]{username, password};
		
		l = findByNamedQueryAndNamedParam("User.findByUsernameAndPassword", paramKeys, paramValues, true, CACHEREGION);
		
		if (l.size() > 0)
		{
			user = (User)l.get(0);
		}
		
		return user;	
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.dao.UserDAO#findUsersByNameMatch(java.lang.String, boolean)
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
	@SuppressWarnings("unchecked")
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
	 * @see net.rrm.ehour.user.dao.UserDAO#findAllActiveUsersWithEmailSet()
	 */
	@SuppressWarnings("unchecked")
	public List<User> findAllActiveUsersWithEmailSet()
	{
		return getHibernateTemplate().findByNamedQuery("User.findAllActiveUsersWithEmailSet");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.dao.UserDAO#deletePmWithoutProject()
	 */
	public void deletePmWithoutProject()
	{
		Query q = getSession().getNamedQuery("User.deletePMsWithoutProject");
		q.executeUpdate();
	}
}
