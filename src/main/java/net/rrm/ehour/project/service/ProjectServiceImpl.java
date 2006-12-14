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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * TODO 
 **/

public class ProjectServiceImpl implements ProjectService
{
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private	Logger					logger = Logger.getLogger(ProjectServiceImpl.class);
	
	public void setProjectAssignmentDAO(ProjectAssignmentDAO dao)
	{
		this.projectAssignmentDAO = dao;
	}
	
	public void setProjectDAO(ProjectDAO dao)
	{
		this.projectDAO = dao;
		
	}
	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getActiveProjectsForUser(java.lang.Integer, java.util.Calendar, java.util.Calendar)
	 */
	public List<Project> getActiveProjectsForUser(Integer userId, Calendar dateStart, Calendar dateEnd)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	/**
	 * 
	 */
	public List<Project> getAllProjects(boolean hideInactive)
	{
		List<Project>	res;
		
		if (hideInactive)
		{
			res = projectDAO.findAll(true);
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
		
		projectAssignmentDAO.persist(projectAssignment);
		
		return projectAssignment;
	}
	
	
	/**
	 * Check if the project is already assigned and the date overlaps
	 * @param projectAssignment
	 * @return
	 */
	private boolean isAssignmentDuplicate(ProjectAssignment projectAssignment)
	{
		boolean					alreadyAssigned = false;
		List<ProjectAssignment>	assignments;
		DateRange				range = projectAssignment.getDateRange();
		
		assignments = projectAssignmentDAO.findProjectForUser(projectAssignment.getProject().getProjectId(),
															 	projectAssignment.getUser().getUserId());

		for (ProjectAssignment assignment : assignments)
		{
			// if this is an update and the assignment is the same, skip it
			if (assignment.equals(projectAssignment.getAssignmentId()))
			{
				alreadyAssigned = false;
				continue;
			}
			
			if (DateUtil.isDateRangeOverlaps(assignment.getDateRange(), range))
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
	 * 
	 */
	public List<ProjectAssignment> getAllProjectsForUser(Integer userId)
	{
		List<ProjectAssignment>	results;
		
		results = projectAssignmentDAO.findProjectsForUser(userId);
		
		return results;
	}

	public List<ProjectAssignment> getProjectsForUser(Integer userId, Date date)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public List<ProjectAssignment> getProjectsForUser(Integer userId, DateRange dateRange)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get project assignment on id
	 */
	public ProjectAssignment getProjectAssignment(Integer assignmentId)
	{
		return projectAssignmentDAO.findById(assignmentId);
	}
}
