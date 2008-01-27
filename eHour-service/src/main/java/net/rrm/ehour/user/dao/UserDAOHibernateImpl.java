/**
 * Created on Nov 11, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.user.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;

import org.hibernate.Session;


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
	 * 
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.dao.UserDAO#findUsersWithPMRoleButNoProject(net.rrm.ehour.user.domain.User)
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUsersWithPMRoleButNoProject()
	{
		Session s = getHibernateTemplate().getSessionFactory().getCurrentSession();
		
		return s.getNamedQuery("User.findUsersWhoHavePMRoleButNoProject").list();
//		
//		return s.
//		
//		return getHibernateTemplate().findByNamedQuery("User.findUsersWhoHavePMRoleButNoProject");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.dao.UserDAO#findUsersWhoDontHavePMRoleButArePM()
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUsersWhoDontHavePMRoleButArePM()
	{
		return getHibernateTemplate().findByNamedQuery("User.findUsersWhoDontHavePMRoleButProject");
	}
}
