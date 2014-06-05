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

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;

import java.util.List;

public interface ProjectService
{
	/**
	 * Get all (active & inactive) projects
	 */
	List<Project> getProjects();


    /** Get all active projects
     */
    List<Project> getActiveProjects();
	
	/**
	 * Get project
	 * @param projectId
	 * @return
	 */
	Project getProject(Integer projectId) throws ObjectNotFoundException;
	
	/**
	 * Get project and check deletability
	 * @param projectId
	 * @return
	 */
	Project getProjectAndCheckDeletability(Integer projectId) throws ObjectNotFoundException;
	
	/**
	 * Set the project deletability flag
	 * @param project
	 * @return
	 */
	void setProjectDeletability(Project project);
	
	/**
	 * Persist the project and if a PM is assigned, give him a ROLE_PROJECTMANAGER role
	 * @param project
	 * @return
	 */
    Project updateProject(Project project);

    Project createProject(Project project);

    /**
     * Validate whether the PM role setup is correct for a project
     * @param project
     */
    void validatePMRoles(Project project);

	/**
	 * Delete the project
	 * @param projectId
	 * @return
	 */
	void deleteProject(Integer projectId) throws ParentChildConstraintException;

	/**
	 * Get project's where user is project manager
	 *
     * @param user
     * @return
	 */
	List<Project> getProjectManagerProjects(User user);
}
