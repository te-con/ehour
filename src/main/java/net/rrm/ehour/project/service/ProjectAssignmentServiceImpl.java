/**
 * Created on Mar 23, 2007
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
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.util.ProjectAssignmentUtil;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.user.domain.User;

import org.apache.log4j.Logger;

/**
 * TODO 
 **/

public class ProjectAssignmentServiceImpl implements ProjectAssignmentService
{
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private	TimesheetDAO			timesheetDAO;
	private	ProjectDAO				projectDAO;
	private	Logger					logger = Logger.getLogger(ProjectAssignmentServiceImpl.class);
	
	/**
	 * Assign user to project
	 *
	 */
	
	public ProjectAssignment assignUserToProject(ProjectAssignment projectAssignment) throws ProjectAlreadyAssignedException
	{
		if (projectAssignment.getAssignmentType().intValue() == ProjectAssignmentUtil.TYPE_DEFAULT_ASSIGNMENT &&
			isAlreadyAssignedAsDefault(projectAssignment))
		{
			throw new ProjectAlreadyAssignedException("Already default assignment made for this project");
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
			assignment.setAssignmentType(ProjectAssignmentUtil.TYPE_DEFAULT_ASSIGNMENT);
			assignment.setProject(project);
			assignment.setUser(user);
			assignment.setActive(true);
			
			if (!isAlreadyAssignedAsDefault(assignment, user.getProjectAssignments()))
			{
				logger.debug("Assigning user " + user.getUserId() + " to default project " + project.getName());
				user.addProjectAssignment(assignment);
			}
		}
		
		return user;
	}
	
	
	/**
	 * Check if the project is already assigned and the date overlaps
	 * @param projectAssignment
	 * @return
	 */
	private boolean isAlreadyAssignedAsDefault(ProjectAssignment projectAssignment)
	{
		List<ProjectAssignment> assignments;
		
		assignments = projectAssignmentDAO.findProjectAssignmentForUser(projectAssignment.getProject().getProjectId(),
															 	projectAssignment.getUser().getUserId(),
															 	ProjectAssignmentUtil.TYPE_DEFAULT_ASSIGNMENT);

		return isAlreadyAssignedAsDefault(projectAssignment, assignments);
	}	
	
	/**
	 * Check if this default assignment is already assigned as default
	 * 
	 * @param projectAssignment
	 * @param user
	 * @return
	 */
	private boolean isAlreadyAssignedAsDefault(ProjectAssignment projectAssignment, Collection<ProjectAssignment> assignments)
	{
		boolean		alreadyAssigned = false;
		int			projectId;
		
		// if the assignment ain't default, ignore it
		if (assignments == null ||
				projectAssignment.getAssignmentType().intValue() != ProjectAssignmentUtil.TYPE_DEFAULT_ASSIGNMENT)
		{
			return false;
		}
		
		projectId = projectAssignment.getProject().getProjectId().intValue();
		
		for (ProjectAssignment assignment : assignments)
		{
			if (assignment.getAssignmentType().intValue() == ProjectAssignmentUtil.TYPE_DEFAULT_ASSIGNMENT &&
				assignment.getProject().getProjectId().intValue() == projectId)
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Default assignment is already default assigned as assignmentId " + assignment.getAssignmentId());
				}
				
				alreadyAssigned = true;
				break;
			}
		}
			
		return alreadyAssigned;
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

	
	
	public void setTimesheetDAO(TimesheetDAO dao)
	{
		timesheetDAO = dao;
	}
	

	public void setProjectAssignmentDAO(ProjectAssignmentDAO dao)
	{
		this.projectAssignmentDAO = dao;
	}
	

	public void setProjectDAO(ProjectDAO dao)
	{
		this.projectDAO = dao;
	}	
}
