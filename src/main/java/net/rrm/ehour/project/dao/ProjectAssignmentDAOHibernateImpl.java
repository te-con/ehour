/**
 * Created on Dec 10, 2006
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
import net.rrm.ehour.project.domain.ProjectAssignment;

/**
 * CRUD stuff on PA do 
 **/

public class ProjectAssignmentDAOHibernateImpl
	extends GenericDAOHibernateImpl<ProjectAssignment, Integer> 
	implements ProjectAssignmentDAO
{
	/**
	 * @todo fix this a bit better
	 */
	public ProjectAssignmentDAOHibernateImpl()
	{
		super(ProjectAssignment.class);
	}	
	
	/**
	 * Find assigned (active) projects for user
	 * @param projectId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectForUser(Integer projectId, Integer userId)
	{
		String[]	names = new String[]{"projectId", "userId"};
		Integer[]	values = new Integer[]{projectId, userId};
		List<ProjectAssignment>		results;
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("ProjectAssignment.findProjectForUser", 
															 names, values);
		
		return results;
	}
	
	/**
	 * Find (active) projects for user
	 * @param userId
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectsForUser(Integer userId)
	{
		List<ProjectAssignment>		results;
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("ProjectAssignment.findProjectsForUser", 
																		"userId", userId);
		
		return results;
		
	}
}
