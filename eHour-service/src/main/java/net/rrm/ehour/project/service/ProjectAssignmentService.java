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

package net.rrm.ehour.project.service;

import java.util.List;

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
	 * 
	 * @param project
	 */
	public void assignUsersToProjects(Project project);
	
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
	 * Get projects for user
	 * @param user
	 * @param hideInactive
	 * @return
	 */
	public List<ProjectAssignment> getProjectAssignmentsForUser(User user, boolean hideInactive);

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
	 * Get project assignments for project
	 * @param project
	 */
	public List<ProjectAssignment> getProjectAssignments(Project project, DateRange dateRange);
	
	/**
	 * Get project assignments for project
	 * @param project
	 * @param hideInActive
	 * @return
	 */
	public List<ProjectAssignment> getProjectAssignments(Project project, boolean hideInActive);
	
	/**
	 * Get available project assignment types
	 * @return
	 */
	public List<ProjectAssignmentType> getProjectAssignmentTypes();
	
	/**
	 * Update project assignment
	 * @param assignment
	 */
	public void updateProjectAssignment(ProjectAssignment assignment);
}
