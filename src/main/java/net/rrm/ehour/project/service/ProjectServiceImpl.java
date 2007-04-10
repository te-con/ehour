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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;

/**
 * TODO 
 **/

public class ProjectServiceImpl implements ProjectService
{
	private	ProjectDAO				projectDAO;
	private ProjectAssignmentDAO	projectAssignmentDAO;
	private	Logger					logger = Logger.getLogger(ProjectServiceImpl.class);
	private ProjectAssignmentService	projectAssignmentService;
	private TimesheetDAO			timesheetDAO;
	private	UserService				userService;
	
	/**
	 * 
	 */
	public List<Project> getAllProjects(boolean hideInactive)
	{
		List<Project>	res;
		
		if (hideInactive)
		{
			logger.debug("Finding all active projects");
			res = projectDAO.findAllActive();
		}
		else
		{
			logger.debug("Finding all projects");
			res = projectDAO.findAll();
		}

		return res;
	}


	/**
	 * Get all project assignments for user
	 * @param userId
	 */
	public List<ProjectAssignment> getAllProjectsForUser(Integer userId)
	{
		List<ProjectAssignment>	results;
		
		results = projectAssignmentDAO.findProjectAssignmentsForUser(userId);
		
		return results;
	}
	
	/**
	 * 
	 */
	public Project getProject(Integer projectId)
	{
		return projectDAO.findById(projectId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#persistProject(net.rrm.ehour.project.domain.Project)
	 */
	public Project persistProject(Project project)
	{
		Project	dbProject = getProject(project.getProjectId());
		
		if (dbProject == null)
		{
			projectDAO.persist(project);
		}
		else 
		{
			if (!(dbProject.getProjectManager() == null && project.getProjectManager() == null) || 
				!dbProject.getProjectManager().equals(project.getProjectManager()))
			{
				if (dbProject.getProjectManager() != null)
				{
					removePMRoleFromUser(dbProject.getProjectManager(), dbProject);
				}
			}

			project = projectDAO.merge(project);
		}
		
		return project;
	}
	
	/**
	 * Remove PM role from user when he's no longer PM on any project
	 * @param user
	 */
	private void removePMRoleFromUser(User user, Project dbProject)
	{
		List<Project> projects = projectDAO.findActiveProjectsWhereUserIsPM(user);
		
		if (projects == null || projects.size() == 0 || (projects.size() == 1 && projects.get(0).equals(dbProject))) 
		{
			// TODO use constant
			logger.info("Removing project manager's role from user " + user.getPK());
			userService.removeRoleFromUser(user,"ROLE_PROJECTMANAGER");
		}
	}

	/**
	 * 
	 */
	public void deleteProject(Integer projectId) throws ParentChildConstraintException
	{
		Project	project;
		
		project = projectDAO.findById(projectId);
		
		if (project.getProjectAssignments() != null &&
			project.getProjectAssignments().size() > 0)
		{
			logger.debug("Can't delete project, still has " + project.getProjectAssignments().size() + " assignments");
			throw new ParentChildConstraintException("Project assignments still attached");
		}
		else
		{
			logger.debug("Deleting project " + project);
			projectDAO.delete(project);
		}
	}

	
	/**
	 * Get active projects for user 
	 */

	public Set<ProjectAssignment> getProjectsForUser(Integer userId, DateRange dateRange)
	{
		List<ProjectAssignment>	activeProjectAssignments = projectAssignmentService.getProjectAssignmentsForUser(userId, dateRange);
		List<ProjectAssignment> bookedProjectAssignments = timesheetDAO.getBookedProjectAssignmentsInRange(userId, dateRange);
		
		Set<ProjectAssignment> mergedAssignments = new HashSet<ProjectAssignment>(activeProjectAssignments);
		mergedAssignments.addAll(bookedProjectAssignments);
		
		return mergedAssignments;
	}	
	
	/**
	 * 
	 * @param dao
	 */

	public void setProjectDAO(ProjectDAO dao)
	{
		this.projectDAO = dao;
	}


	/**
	 * @param projectAssignmentDAO the projectAssignmentDAO to set
	 */
	public void setProjectAssignmentDAO(ProjectAssignmentDAO projectAssignmentDAO)
	{
		this.projectAssignmentDAO = projectAssignmentDAO;
	}


	/**
	 * @param projectAssignmentService the projectAssignmentService to set
	 */
	public void setProjectAssignmentService(ProjectAssignmentService projectAssignmentService)
	{
		this.projectAssignmentService = projectAssignmentService;
	}


	/**
	 * @param timesheetDAO the timesheetDAO to set
	 */
	public void setTimesheetDAO(TimesheetDAO timesheetDAO)
	{
		this.timesheetDAO = timesheetDAO;
	}


	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}
}
