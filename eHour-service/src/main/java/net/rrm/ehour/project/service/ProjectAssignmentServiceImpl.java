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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.dto.AssignmentStatus;
import net.rrm.ehour.project.util.ProjectAssignmentUtil;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.util.EhourConstants;

import org.apache.log4j.Logger;

/**
 * Project assignment service
 **/

public class ProjectAssignmentServiceImpl implements ProjectAssignmentService
{
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private	TimesheetDAO			timesheetDAO;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentUtil	timeAllottedUtil;
	private	MailService				mailService;
	private	Logger					logger = Logger.getLogger(ProjectAssignmentServiceImpl.class);
	private	ReportAggregatedDAO		reportAggregatedDAO;
	
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
			assignment = new ProjectAssignment();
			assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
			assignment.setProject(project);
			assignment.setUser(user);
			assignment.setActive(true);
			
			if (!isAlreadyAssigned(assignment, user.getProjectAssignments()))
			{
				logger.debug("Assigning user " + user.getUserId() + " to default project " + project.getName());
				user.addProjectAssignment(assignment);
			}
		}
		
		return user;
	}
	
	/**
	 * Check if this default assignment is already assigned as default
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
				if (logger.isDebugEnabled())
				{
					logger.debug("Default assignment is already assigned as assignmentId " + assignment.getAssignmentId());
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
			if (timeAllottedUtil.getAssignmentStatus(assignment).isAssignmentBookable())
			{
				validAssignments.add(assignment);
				continue;
			}
		}
		
		return validAssignments;
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
		
		List<Integer>	ids = new ArrayList<Integer>();
		ids.add(assignmentId);
		List<AssignmentAggregateReportElement>	aggregates;
		
		// call to report service needed but due to circular reference go straight to DAO
		aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(ids);
		assignment.setDeletable(ProjectAssignmentUtil.isEmptyAggregateList(aggregates));
		
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
	 * @see net.rrm.ehour.project.service.ProjectAssignmentService#checkForOverruns(java.util.Set)
	 */
	public void checkForOverruns(Set<ProjectAssignment> assignments)
	{
		ProjectAssignment	dbAssignment;
		AssignmentStatus	status;
		TimesheetEntry		entry;
		
		
		for (ProjectAssignment assignment : assignments)
		{
			// assignment in set might not be fetched from db but created with only the id
			dbAssignment = projectAssignmentDAO.findById(assignment.getAssignmentId());
			
			// don't bother to check for notifications if we won't/can't send email anyway
			if (!dbAssignment.isNotifyPm()
				|| dbAssignment.getProject().getProjectManager() == null
				|| dbAssignment.getProject().getProjectManager().getEmail() == null)
			{
				continue;
			}
		
			status = timeAllottedUtil.getAssignmentStatus(dbAssignment);

			
			if (status.getAssignmentPhase() == AssignmentStatus.OVER_ALLOTTED_PHASE 
				&& dbAssignment.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED)
			{
				entry = timesheetDAO.getLatestTimesheetEntryForAssignment(assignment.getAssignmentId());
				mailService.mailPMFixedAllottedReached(status.getAggregate(),
														entry.getEntryId().getEntryDate(),
														dbAssignment.getProject().getProjectManager());
			}
			else if (status.getAssignmentPhase() == AssignmentStatus.OVER_OVERRUN_PHASE 
					&& dbAssignment.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX)
			{
				entry = timesheetDAO.getLatestTimesheetEntryForAssignment(assignment.getAssignmentId());
				mailService.mailPMFlexOverrunReached(status.getAggregate(), 
														entry.getEntryId().getEntryDate(),
														dbAssignment.getProject().getProjectManager());
			}
			else if (status.getAssignmentPhase() == AssignmentStatus.IN_OVERRUN_PHASE 
					&& dbAssignment.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX)
			{
				entry = timesheetDAO.getLatestTimesheetEntryForAssignment(assignment.getAssignmentId());
				mailService.mailPMFlexAllottedReached(status.getAggregate(), 
														entry.getEntryId().getEntryDate(),
														dbAssignment.getProject().getProjectManager());
			}
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
	
	/**
	 * 
	 * @param dao
	 */
	public void setTimesheetDAO(TimesheetDAO dao)
	{
		timesheetDAO = dao;
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
	 * @param timeAllottedUtil the timeAllottedUtil to set
	 */
	public void setTimeAllottedUtil(ProjectAssignmentUtil timeAllottedUtil)
	{
		this.timeAllottedUtil = timeAllottedUtil;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService)
	{
		this.mailService = mailService;
	}

	/**
	 * @param reportAggregatedDAO the reportAggregatedDAO to set
	 */
	public void setReportAggregatedDAO(ReportAggregatedDAO reportAggregatedDAO)
	{
		this.reportAggregatedDAO = reportAggregatedDAO;
	}
}
