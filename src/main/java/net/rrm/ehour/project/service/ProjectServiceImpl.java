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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * TODO 
 **/

public class ProjectServiceImpl implements ProjectService
{
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private	TimesheetDAO			timesheetDAO;
	private	Logger					logger = Logger.getLogger(ProjectServiceImpl.class);
	
	/**
	 * 
	 */
	public List<Project> getAllProjects(boolean hideInactive)
	{
		List<Project>	res;
		
		if (hideInactive)
		{
			res = projectDAO.findAllActive();
		}
		else
		{
			res = projectDAO.findAll();
		}

		return res;
	}
	
	/**
	 * 
	 */
	public Project getProject(Integer projectId)
	{
		return projectDAO.findById(projectId);
	}
	
	/**
	 * 
	 */
	
	public Project persistProject(Project project)
	{
		projectDAO.persist(project);
		return project;
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
			throw new ParentChildConstraintException("Project assignments still attached");
		}
		else
		{
			projectDAO.delete(project);
		}
	}
	
	/**
	 * Assign user to project
	 */
	
	public ProjectAssignment assignUserToProject(ProjectAssignment projectAssignment) throws ProjectAlreadyAssignedException
	{
		if (isAssignmentDuplicate(projectAssignment))
		{
			throw new ProjectAlreadyAssignedException("Project already assigned for date range");
		}

		// use merge as the session already contains an attached PA with the same id when checking for dupes
		if (projectAssignment.getAssignmentId() != null)
		{
			projectAssignmentDAO.merge(projectAssignment);
		}
		else
		{
			projectAssignmentDAO.persist(projectAssignment);
		}
		
		return projectAssignment;
	}
	
	/**
	 * Assign user to default projects
	 */
	public User assignUserToDefaultProjects(User user)
	{
		List<Project>		defaultProjects;
		ProjectAssignment	assignment;
		
		defaultProjects = projectDAO.findDefaultProjects();
		
		for (Project project : defaultProjects)
		{
			assignment = new ProjectAssignment();
			assignment.setDefaultAssignment(true);
			assignment.setProject(project);
			assignment.setUser(user);
			assignment.setActive(true);
			
			if (!isAssignmentDuplicate(assignment, user.getProjectAssignments()))
			{
				logger.debug("Assigning user " + user.getUserId() + " to default project " + project.getName());
				user.addProjectAssignment(assignment);
			}
		}
		
		return user;
	}
	
	
	/**
	 * Check if the project is already assigned and the date overlaps
	 * 
	 * @param projectAssignment
	 * @param user
	 * @return
	 */
	private boolean isAssignmentDuplicate(ProjectAssignment projectAssignment, Collection<ProjectAssignment> assignments)
	{
		boolean		alreadyAssigned = false;
		DateRange	range = projectAssignment.getDateRange();
		
		if (assignments == null)
		{
			return false;
		}
		
		for (ProjectAssignment assignment : assignments)
		{
			// if this is an update and the assignment is the same, skip it
			if (assignment.getAssignmentId() != null &&
				assignment.getAssignmentId().equals(projectAssignment.getAssignmentId()))
			{
				alreadyAssigned = false;
				continue;
			}
			
			if (assignment.isDefaultAssignment() ||
				DateUtil.isDateRangeOverlaps(assignment.getDateRange(), range))
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Assignment overlaps with assignmentId " + assignment.getAssignmentId());
				}
				
				alreadyAssigned = true;
				break;
			}
		}
			
		return alreadyAssigned;
	}
	
	/**
	 * Check if the project is already assigned and the date overlaps
	 * @param projectAssignment
	 * @return
	 */
	private boolean isAssignmentDuplicate(ProjectAssignment projectAssignment)
	{
		List<ProjectAssignment> assignments;
		
		assignments = projectAssignmentDAO.findProjectAssignmentForUser(projectAssignment.getProject().getProjectId(),
															 	projectAssignment.getUser().getUserId());

		return isAssignmentDuplicate(projectAssignment, assignments);
	}

	/**
	 * 
	 */
	public List<ProjectAssignment> getAllProjectsForUser(Integer userId)
	{
		List<ProjectAssignment>	results;
		
		results = projectAssignmentDAO.findProjectAssignmentsForUser(userId);
		
		return results;
	}
	
	/**
	 * Get active projects for user in date range 
	 * @param userId
	 * @param dateRange
	 * @return
	 */	
	
	public List<ProjectAssignment> getProjectAssignmentsForUser(Integer userId, DateRange dateRange)
	{
		return projectAssignmentDAO.findProjectAssignmentsForUser(userId, dateRange);
	}

	/**
	 * Get project assignment on id
	 * 
	 */
	public ProjectAssignment getProjectAssignment(Integer assignmentId)
	{
		ProjectAssignment 	assignment;
		int					timesheetEntries;
		
		assignment = projectAssignmentDAO.findById(assignmentId);
		timesheetEntries = timesheetDAO.getTimesheetEntryCountForAssignment(assignmentId);
		
		assignment.setDeletable(timesheetEntries == 0);
		
		return assignment;
	}

	/**
	 * 
	 */
	public void deleteProjectAssignment(Integer assignmentId) throws ParentChildConstraintException
	{
		ProjectAssignment pa = getProjectAssignment(assignmentId);
		
		if (pa.isDeletable())
		{
			projectAssignmentDAO.delete(pa);
		}
		else
		{
			throw new ParentChildConstraintException("Timesheet entries booked on assignment.");
		}
		
	}
	
	/**
	 * Get active projects for user 
	 */

	public Set<ProjectAssignment> getProjectsForUser(Integer userId, DateRange dateRange)
	{
		List<ProjectAssignment>	activeProjectAssignments = getProjectAssignmentsForUser(userId, dateRange);
		List<ProjectAssignment> bookedProjectAssignments = timesheetDAO.getBookedProjectAssignmentsInRange(userId, dateRange);
		
		Set<ProjectAssignment> mergedAssignments = new HashSet<ProjectAssignment>(activeProjectAssignments);
		mergedAssignments.addAll(bookedProjectAssignments);
		
		return mergedAssignments;
	}
	

	public void setProjectAssignmentDAO(ProjectAssignmentDAO dao)
	{
		this.projectAssignmentDAO = dao;
	}
	
	public void setProjectDAO(ProjectDAO dao)
	{
		this.projectDAO = dao;
	}
	
	public void setTimesheetDAO(TimesheetDAO dao)
	{
		timesheetDAO = dao;
	}
}
