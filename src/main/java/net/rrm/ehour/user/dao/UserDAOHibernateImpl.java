/**
 * Created on Nov 11, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.user.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.user.domain.User;


public class UserDAOHibernateImpl extends GenericDAOHibernateImpl<User, Integer> implements UserDAO
{
	public UserDAOHibernateImpl()
	{
		super(User.class);
	}	
	
	/**
	 * 
	 */
	public User findByUsername(String username)
	{
		User	user = null;
		List	l;
		
		l = getHibernateTemplate().findByNamedQueryAndNamedParam("User.findByUsername", "username", username);
		
		if (l.size() > 0)
		{
			user = (User)l.get(0);
		}
		
		return user;	
	}

	/**
	 * 
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
	public List<User> findUsersForDepartments(String pattern, Integer[] departmentIds, boolean onlyActive)
	{
		String		hql;
		String[]	paramKeys;
		Object[]	paramValues;
		
		if (pattern != null)
		{
			pattern = pattern.toLowerCase();
			pattern = "%" + pattern + "%";
		}
		else
		{
			pattern = "%";
		}		
		
		hql = (onlyActive) ? "User.findActiveByNamePatternForDepartment" :
			  				 "User.findNamePatternForDepartment";
		
		paramKeys = new String[]{"pattern", "departments"};
		paramValues = new Object[]{pattern, departmentIds};
		
		return getHibernateTemplate().findByNamedQueryAndNamedParam(hql, paramKeys, paramValues);
	}
}
