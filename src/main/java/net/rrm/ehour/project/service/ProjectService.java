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
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;

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
	 * Persist the project and if a PM is assigned, give him a ROLE_PROJECTMANAGER role
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
	 * Get all projects for user where project itself is still active
	 * @param userId
	 * @return
	 */
	public List<ProjectAssignment> getAllProjectsForUser(Integer userId);

	
	/**
	 * Get active projects in daterange and any inactive if hours were booked on 'm in this period
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	
	public Set<ProjectAssignment> getProjectsForUser(Integer userId, DateRange dateRange);	
}
