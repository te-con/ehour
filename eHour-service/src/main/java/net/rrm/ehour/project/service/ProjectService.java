/**
 * Created on Nov 25, 2006
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
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;
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
	 * Get all (filtered) projects
	 * @param filter
	 * @param hideInactive
	 * @return
	 */
	public List<Project> getProjects(String filter, boolean hideInactive);
	
	/**
	 * Get project
	 * @param projectId
	 * @return
	 */
	public Project getProject(Integer projectId) throws ObjectNotFoundException;
	
	/**
	 * Get project and check deletability
	 * @param projectId
	 * @return
	 */
	public Project getProjectAndCheckDeletability(Integer projectId) throws ObjectNotFoundException;
	
	/**
	 * Set the project deletability flag
	 * @param project
	 * @return
	 */
	public void setProjectDeletability(Project project);
	
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
	public List<ProjectAssignment> getAllProjectsForUser(User user);

	
	/**
	 * Get active projects in daterange and any inactive if hours were booked on 'm in this period
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	
	public Set<ProjectAssignment> getProjectsForUser(Integer userId, DateRange dateRange);
	
	/**
	 * Get project's where user is project manager
	 * @param user
	 * @return
	 */
	public List<Project> getProjectManagerProjects(User user);
}
