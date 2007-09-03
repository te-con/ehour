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

import net.rrm.ehour.dao.GenericDAO;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;

/**
 * CRUD on ProjectAssignment domain object
 **/

public interface ProjectAssignmentDAO  extends GenericDAO<ProjectAssignment, Integer>
{
	/**
	 * Find assigned (active) project for user
	 * @param projectId
	 * @param userId
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentForUser(Integer projectId, Integer userId);
	
	/**
	 * Find assigned (active) projects for user with specific assignment type
	 * @param projectId
	 * @param userId
	 * @param assignmentType
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentForUser(Integer projectId, Integer userId, Integer assignmentType);
	
	/**
	 * Find (active) projects for user
	 * @param userId
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentsForUser(Integer userId);
	
	/**
	 * Find (active) projects for user in date range
	 * @param userId
	 * @param range
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentsForUser(Integer userId, DateRange range);
	
	/**
	 * Find project assignments for project in range
	 * @param project
	 * @param range
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentsForProject(Project project, DateRange range);
	
	/**
	 * Find all project assignment types (should be in a seperate DAO theoratically)
	 * @return
	 */
	public List<ProjectAssignmentType>	findProjectAssignmentTypes();
}
