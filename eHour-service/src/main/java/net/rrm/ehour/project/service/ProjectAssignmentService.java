/**
 * Created on Mar 23, 2007
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

package net.rrm.ehour.project.service;

import java.util.List;
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;

/**
 * Service for handling project assignments
 **/

public interface ProjectAssignmentService
{
	/**
	 * Assign user to project
	 * @param projectAssignment
	 */
	public ProjectAssignment assignUserToProject(ProjectAssignment projectAssignment);
	
	/**
	 * Assign user to default projects
	 * @param user
	 * @return
	 */
	public User assignUserToDefaultProjects(User user);
	
	/**
	 * Get active projects for user in date range 
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	public List<ProjectAssignment> getProjectAssignmentsForUser(Integer userId, DateRange dateRange);

	/**
	 * Get project assignment and mark it as deletable or not
	 * @param assignmentId
	 * @return
	 */
	public ProjectAssignment getProjectAssignment(Integer assignmentId)  throws ObjectNotFoundException;
	
	/**
	 * Delete project assignment
	 * @param assignmentId
	 */
	public void deleteProjectAssignment(Integer assignmentId) throws ObjectNotFoundException, ParentChildConstraintException;
	
	/**
	 * Check for time allotted overruns and mail the PM when required
	 * @param assignments
	 */
	public void checkForOverruns(Set<ProjectAssignment> assignments);
	
	/**
	 * Get project assignments for project
	 * @param project
	 */
	public List<ProjectAssignment> getProjectAssignments(Project project, DateRange dateRange);
	
	/**
	 * Get available project assignment types
	 * @return
	 */
	public List<ProjectAssignmentType> getProjectAssignmentTypes();
}
