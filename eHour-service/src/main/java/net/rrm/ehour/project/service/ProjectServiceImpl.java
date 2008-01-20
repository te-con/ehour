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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.util.ProjectAssignmentUtil;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.user.domain.User;

import org.apache.log4j.Logger;

/**
 * Project service
 **/

public class ProjectServiceImpl implements ProjectService
{
	private	ProjectDAO					projectDAO;
	private ProjectAssignmentDAO		projectAssignmentDAO;
	private	Logger						logger = Logger.getLogger(ProjectServiceImpl.class);
	private ProjectAssignmentService	projectAssignmentService;
	private	AggregateReportService		aggregateReportService;
	
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

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getProjects(java.lang.String, boolean)
	 */
	public List<Project> getProjects(String filter, boolean hideInactive)
	{
		return projectDAO.findProjects(filter, hideInactive);
	}

	/**
	 * Get all project assignments for user
	 * @param userId
	 */
	public List<ProjectAssignment> getAllProjectsForUser(User user)
	{
		List<ProjectAssignment>	results;
		
		results = projectAssignmentDAO.findProjectAssignmentsForUser(user);
		
		return results;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getProject(java.lang.Integer)
	 */
	public Project getProject(Integer projectId) throws ObjectNotFoundException
	{
		Project project = projectDAO.findById(projectId);
		
		if (project == null)
		{
			throw new ObjectNotFoundException("Project not found for id " + projectId);
		}
		
		return project;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getProjectAndCheckDeletability(java.lang.Integer)
	 */
	public Project getProjectAndCheckDeletability(Integer projectId) throws ObjectNotFoundException
	{
		Project project = getProject(projectId);
		
		setProjectDeletability(project);
		
		return project;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#setProjectDeletability(net.rrm.ehour.project.domain.Project)
	 */
	public void setProjectDeletability(Project project)
	{
		List<Integer> ids = ProjectAssignmentUtil.getAssignmentIds(project.getProjectAssignments());
		List<AssignmentAggregateReportElement> aggregates = null;
		
		if (ids != null && ids.size() > 0)
		{
			aggregates = aggregateReportService.getHoursPerAssignment(ids);
		}
		
		project.setDeletable(ProjectAssignmentUtil.isEmptyAggregateList(aggregates));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#persistProject(net.rrm.ehour.project.domain.Project)
	 */
	public Project persistProject(Project project)
	{
		projectDAO.persist(project);

//		userService.checkProjectManagementRolesValid();
		
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
		// FIXME Derby breaks on it
//		List<ProjectAssignment> bookedProjectAssignments = timesheetDAO.getBookedProjectAssignmentsInRange(userId, dateRange);
		
		Set<ProjectAssignment> mergedAssignments = new HashSet<ProjectAssignment>(activeProjectAssignments);
//		mergedAssignments.addAll(bookedProjectAssignments);
		
		return mergedAssignments;
	}	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getProjectManagerProjects(net.rrm.ehour.user.domain.User)
	 */
	public List<Project> getProjectManagerProjects(User user)
	{
		return projectDAO.findActiveProjectsWhereUserIsPM(user);
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
	 * @param aggregateReportService the aggregateReportService to set
	 */
	public void setAggregateReportService(AggregateReportService aggregateReportService)
	{
		this.aggregateReportService = aggregateReportService;
	}
}
