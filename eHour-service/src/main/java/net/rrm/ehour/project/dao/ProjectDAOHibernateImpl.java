/**
 * Created on Dec 4, 2006
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

package net.rrm.ehour.project.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.user.domain.User;

/**
 *  
 **/

public class ProjectDAOHibernateImpl extends GenericDAOHibernateImpl<Project, Integer> implements ProjectDAO
{
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
	public List<Project> findProjectForCustomers(List<Integer> customerIds, boolean onlyActive)
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
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam(hqlName,
																		"customerIds", 
																		customerIds.toArray());
		
		return results;			
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.ProjectDAO#findProjectWhereUserIsPM(net.rrm.ehour.user.domain.User)
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findActiveProjectsWhereUserIsPM(User user)
	{
		return getHibernateTemplate().findByNamedQueryAndNamedParam("Project.findActiveProjectsWhereUserIsPM",
																	"user", user);
	}
}
