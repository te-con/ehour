/**
 * Created on Dec 10, 2006
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
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;

/**
 * CRUD stuff on PA do 
 **/

public class ProjectAssignmentDAOHibernateImpl
	extends GenericDAOHibernateImpl<ProjectAssignment, Integer> 
	implements ProjectAssignmentDAO
{
	private final static String	CACHEREGION = "query.ProjectAssignment";
	
	/**
	 * @todo fix this a bit better
	 */
	public ProjectAssignmentDAOHibernateImpl()
	{
		super(ProjectAssignment.class);
	}
	
	/**
	 * Find (active) projects for user in date range
	 * @param userId
	 * @param range
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentsForUser(Integer userId, DateRange range)
	{
		List<ProjectAssignment>		results;

		String[]	keys = new String[]{"dateStart", "dateEnd", "userId"};
		Object[]	params = new Object[]{range.getDateStart(), range.getDateEnd(), userId}; 
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForUserInRange"
				, keys
				, params
				, true
				, CACHEREGION);			
		
		
		return results;
	}
	
	
	/**
	 * Find assigned (active) projects for user
	 * @param projectId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentForUser(Integer projectId, Integer userId)
	{
		String[]	keys = new String[]{"projectId", "userId"};
		Integer[]	params = new Integer[]{projectId, userId};
		List<ProjectAssignment>		results;
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForUserForProject"
				, keys
				, params
				, true
				, CACHEREGION);		
		
		return results;
	}
	
	/**
	 * Find (active) projects for user
	 * @param userId
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentsForUser(Integer userId)
	{
		List<ProjectAssignment>		results;
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForUser"
				, "userId"
				, userId
				, true
				, CACHEREGION);			
		
		return results;
	}

	/**
	 * Find project assignments for user for a specific type
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentForUser(Integer projectId, Integer userId, Integer assignmentType)
	{
		List<ProjectAssignment>		results;
		String[]	names = new String[]{"userId", "type"};
		Integer[]	values = new Integer[]{userId, assignmentType};
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectsForUserForType"
				, names
				, values
				, true
				, CACHEREGION);		
		
		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.ProjectAssignmentDAO#findProjectAssignmentsForProject(net.rrm.ehour.project.domain.Project, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentsForProject(Project project, DateRange range)
	{
		List<ProjectAssignment>		results;
		String[]	keys = new String[]{"dateStart", "dateEnd", "project"};
		Object[]	params = new Object[]{range.getDateStart(), range.getDateEnd(), project}; 
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForProjectInRange"
													, keys
													, params
													, true
													, CACHEREGION);
		
		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.ProjectAssignmentDAO#findProjectAssignmentTypes()
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignmentType> findProjectAssignmentTypes()
	{
		return getHibernateTemplate().loadAll(ProjectAssignmentType.class);
	}
}
