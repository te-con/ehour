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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.EhourConstants;
import net.rrm.ehour.util.EhourUtil;

import org.apache.log4j.Logger;
public class ProjectAssignmentServiceImpl implements ProjectAssignmentService
{
	private	final static Logger		LOGGER = Logger.getLogger(ProjectAssignmentServiceImpl.class);

	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentStatusService	projectAssignmentStatusService;
	private	ReportAggregatedDAO		reportAggregatedDAO;
	private UserService				userService;
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectAssignmentService#assignUsersToProjects(net.rrm.ehour.domain.Project)
	 */
	public void assignUsersToProjects(Project project)
	{
		List<User> users = userService.getUsers(new UserRole(EhourConstants.ROLE_CONSULTANT));
		
		for (User user : users)
		{
			ProjectAssignment assignment = ProjectAssignment.createProjectAssignment(project, user);

			if (!isAlreadyAssigned(assignment, user.getProjectAssignments()))
			{
				LOGGER.debug("Assigning user " + user + " to " + project);
				assignUserToProject(assignment);	
			}
		}
	}
	
	/**
	 * Assign user to project
	 *
	 */
	
	public ProjectAssignment assignUserToProject(ProjectAssignment projectAssignment) 
	{
		projectAssignmentDAO.persist(projectAssignment);
		
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
			assignment = ProjectAssignment.createProjectAssignment(project, user);
			
			if (!isAlreadyAssigned(assignment, user.getProjectAssignments()))
			{
				LOGGER.debug("Assigning user " + user.getUserId() + " to default project " + project.getName());
				user.addProjectAssignment(assignment);
			}
		}
		
		return user;
	}
	
	/**
	 * Check if this default assignment is already assigned
	 * 
	 * @param projectAssignment
	 * @param user
	 * @return
	 */
	private boolean isAlreadyAssigned(ProjectAssignment projectAssignment, Collection<ProjectAssignment> assignments)
	{
		boolean		alreadyAssigned = false;
		int			projectId;
		
		if (assignments == null)
		{
			return false;
		}
		
		projectId = projectAssignment.getProject().getProjectId().intValue();
		
		for (ProjectAssignment assignment : assignments)
		{
			if (assignment.getProject().getProjectId().intValue() == projectId)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("Default assignment is already assigned as assignmentId " + assignment.getAssignmentId());
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
		List<ProjectAssignment>	assignments;
		List<ProjectAssignment>	validAssignments = new ArrayList<ProjectAssignment>();
		
		assignments = projectAssignmentDAO.findProjectAssignmentsForUser(userId, dateRange);
		
		for (ProjectAssignment assignment : assignments)
		{
			if (projectAssignmentStatusService.getAssignmentStatus(assignment, dateRange).isAssignmentBookable())
			{
				validAssignments.add(assignment);
				continue;
			}
		}
		
		return validAssignments;
	}
	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getAllProjectsForUser(net.rrm.ehour.domain.User, boolean)
	 */
	public List<ProjectAssignment> getProjectAssignmentsForUser(User user, boolean hideInactive)
	{
		List<ProjectAssignment>	results;
		List<ProjectAssignment>	filteredResults;

		results = projectAssignmentDAO.findProjectAssignmentsForUser(user);

		if (hideInactive)
		{
			Date today = new Date();
			
			filteredResults = new ArrayList<ProjectAssignment>();
			
			for (ProjectAssignment projectAssignment : results)
			{
				if (projectAssignment.isActive() && 
						(projectAssignment.getDateStart() == null || projectAssignment.getDateStart().before(today)) &&
						(projectAssignment.getDateEnd() == null || projectAssignment.getDateEnd().after(today)))
				{
					filteredResults.add(projectAssignment);
				}
			}
			
			results = filteredResults;
		}
		
		return results;
	}	
	

	/**
	 * Get project assignment on id
	 * 
	 */
	public ProjectAssignment getProjectAssignment(Integer assignmentId) throws ObjectNotFoundException
	{
		ProjectAssignment 	assignment;
		
		assignment = projectAssignmentDAO.findById(assignmentId);
		
		if (assignment == null)
		{
			throw new ObjectNotFoundException("Assignment not found for id: " + assignmentId);
		}
		
		List<Serializable>	ids = new ArrayList<Serializable>();
		ids.add(assignmentId);
		List<AssignmentAggregateReportElement>	aggregates;
		
		// call to report service needed but due to circular reference go straight to DAO
		aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(ids);
		assignment.setDeletable(EhourUtil.isEmptyAggregateList(aggregates));
		
		return assignment;
	}

	/**
	 * @throws ObjectNotFoundException 
	 * 
	 */
	public void deleteProjectAssignment(Integer assignmentId) throws ParentChildConstraintException, ObjectNotFoundException
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

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectAssignmentService#getProjectAssignments(net.rrm.ehour.project.domain.Project, net.rrm.ehour.data.DateRange)
	 */
	public List<ProjectAssignment> getProjectAssignments(Project project, DateRange range)
	{
		return projectAssignmentDAO.findProjectAssignmentsForProject(project, range);
		
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectAssignmentService#getProjectAssignmentTypes()
	 */
	public List<ProjectAssignmentType> getProjectAssignmentTypes()
	{
		return projectAssignmentDAO.findProjectAssignmentTypes();
	}

	public void setProjectAssignmentDAO(ProjectAssignmentDAO dao)
	{
		this.projectAssignmentDAO = dao;
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
	 * @param projectAssignmentStatusService the projectAssignmentStatusService to set
	 */
	public void setProjectAssignmentStatusService(ProjectAssignmentStatusService projectAssignmentStatusService)
	{
		this.projectAssignmentStatusService = projectAssignmentStatusService;
	}

	/**
	 * @param reportAggregatedDAO the reportAggregatedDAO to set
	 */
	public void setReportAggregatedDAO(ReportAggregatedDAO reportAggregatedDAO)
	{
		this.reportAggregatedDAO = reportAggregatedDAO;
	}
}
