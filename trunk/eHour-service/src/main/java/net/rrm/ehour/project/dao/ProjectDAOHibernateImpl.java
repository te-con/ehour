/**
 * Created on Dec 4, 2006
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

package net.rrm.ehour.project.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;

/**
 *  
 **/

public class ProjectDAOHibernateImpl extends GenericDAOHibernateImpl<Project, Integer> implements ProjectDAO
{
	protected final static String	CACHEREGION = "query.Project";
	
	/**
	 * @todo fix this a bit better
	 */
	public ProjectDAOHibernateImpl()
	{
		super(Project.class);
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.ProjectDAO#findAll(boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findAllActive()
	{
		return getHibernateTemplate().findByNamedQuery("Project.findAllActive");
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findDefaultProjects()
	{
		return getHibernateTemplate().findByNamedQuery("Project.findAllActiveDefault");
	}

	/**
	 * Get projects for customer respecting the active flag
	 * @param customerIds
	 * @param active
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findProjectForCustomers(List<Customer> customers, boolean onlyActive)
	{
		List	results;
		String	hqlName;
		
		if (!onlyActive)
		{
			hqlName = "Project.findAllProjectsForCustomers";
		}
		else
		{
			hqlName = "Project.findActiveProjectsForCustomers";
		}
		
		results = findByNamedQueryAndNamedParam(hqlName,
													"customers", 
													customers.toArray(),
													true,
													CACHEREGION);
		
		return results;			
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.ProjectDAO#findProjectWhereUserIsPM(net.rrm.ehour.user.domain.User)
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findActiveProjectsWhereUserIsPM(User user)
	{
		return findByNamedQueryAndNamedParam("Project.findActiveProjectsWhereUserIsPM",
												"user", user,
												true, CACHEREGION);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.ProjectDAO#findProjects(java.lang.String, boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findProjects(String pattern, boolean onlyActive)
	{
		String	hql;
		
		pattern = patternToSqlPattern(pattern);
		
		hql = (onlyActive) ? "Project.findActiveByNamePattern" :
							  "Project.findByNamePattern";
		
		return getHibernateTemplate().findByNamedQueryAndNamedParam(hql,
																"pattern", pattern);		
	}
}
