/**
 * Created on Nov 25, 2006
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

package net.rrm.ehour.project.service;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.user.domain.User;

public interface ProjectService
{
	/**
	 * Get all projects
	 * @param hideInactive 
	 * @return
	 */
	public List<Project> getAllProjects(boolean hideInactive);
	
	/**
	 * Get project
	 * @param projectId
	 * @return
	 */
	public Project getProject(Integer projectId);
	
	/**
	 * Persist the project
	 * @param project
	 * @return
	 */
	public Project persistProject(Project project);
	
	/**
	 * Delete the project
	 * @param projectId
	 * @return
	 */
	public void deleteProject(Integer projectId) throws ParentChildConstraintException;
	
	/**
	 * Assign user to project
	 * @param projectAssignment
	 */
	public ProjectAssignment assignUserToProject(ProjectAssignment projectAssignment) throws ProjectAlreadyAssignedException;
	
	/**
	 * Assign user to default projects
	 * @param user
	 * @return
	 */
	public void assignUserToDefaultProjects(User user);
	
	/**
	 * Get active projects for user in date range 
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	public List<ProjectAssignment> getProjectAssignmentsForUser(Integer userId, DateRange dateRange);
	
	/**
	 * Get all projects for user where project itself is still active
	 * @param userId
	 * @return
	 */
	public List<ProjectAssignment> getAllProjectsForUser(Integer userId);
	
	/**
	 * Get project assignment and mark it as deletable or not
	 * @param assignmentId
	 * @return
	 */
	public ProjectAssignment getProjectAssignment(Integer assignmentId);
	
	/**
	 * Delete project assignment
	 * @param assignmentId
	 */
	public void deleteProjectAssignment(Integer assignmentId) throws ParentChildConstraintException;
}
