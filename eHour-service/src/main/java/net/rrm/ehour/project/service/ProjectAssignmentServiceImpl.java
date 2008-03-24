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
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.dao.ProjectAssignmentDAO;
import net.rrm.ehour.project.dao.ProjectDAO;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.util.EhourConstants;
import net.rrm.ehour.util.EhourUtil;

import org.apache.log4j.Logger;

/**
 * Project assignment service
 **/

public class ProjectAssignmentServiceImpl implements ProjectAssignmentService
{
	private	ProjectAssignmentDAO	projectAssignmentDAO;
	private	TimesheetDAO			timesheetDAO;
	private	ProjectDAO				projectDAO;
	private	ProjectAssignmentStatusService	projectAssignmentStatusService;
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
			if (projectAssignmentStatusService.getAssignmentStatus(assignment).isAssignmentBookable())
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
	 * @see net.rrm.ehour.project.service.ProjectAssignmentService#checkAndNotify(net.rrm.ehour.domain.ProjectAssignment)
	 */
	public void checkAndNotify(ProjectAssignment assignment) throws OverBudgetException
	{
//		ProjectAssignment	dbAssignment;
//		ProjectAssignmentStatus	status;
//		TimesheetEntry		entry;
//		
//		// assignment in set might not be fetched from db but created with only the id
//		dbAssignment = projectAssignmentDAO.findById(assignment.getAssignmentId());
//		
//		status = projectAssignmentStatusService.getAssignmentStatus(dbAssignment);
//		
//		// over alloted - fixed
//		if (dbAssignment.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED
//				&& status.getStatusses().contains(ProjectAssignmentStatus.Status.OVER_ALLOTTED)) 
//		{
////			entry = timesheetDAO.getLatestTimesheetEntryForAssignment(assignment.getAssignmentId());
//			
//			// TODO review
////			if (canNotifyPm(dbAssignment))
////			{
////				mailService.mailPMFixedAllottedReached(status.getAggregate(),
////														entry.getEntryId().getEntryDate(),
////														dbAssignment.getProject().getProjectManager());
////			}
//			
//			ErrorInfo errorInfo = new ErrorInfo(ErrorInfo.ErrorCode.IN_OVERRUN);
//			errorInfo.setAssignment(dbAssignment);
//			errorInfo.setAggregate(status.getAggregate());
//			throw new OverBudgetException(errorInfo);
//		}
//		// over overrun - flex
//		else if (dbAssignment.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX
//					&& status.getStatusses().contains(ProjectAssignmentStatus.Status.OVER_OVERRUN)) 
//		{
//			entry = timesheetDAO.getLatestTimesheetEntryForAssignment(assignment.getAssignmentId());
//
//			// TODO review
////			if (canNotifyPm(dbAssignment))
////			{
////				mailService.mailPMFlexOverrunReached(status.getAggregate(), 
////														entry.getEntryId().getEntryDate(),
////														dbAssignment.getProject().getProjectManager());
////			}
//			
//			ErrorInfo errorInfo = new ErrorInfo(ErrorInfo.ErrorCode.OVER_OVERRUN_FLEX);
//			errorInfo.setAssignment(dbAssignment);
//			errorInfo.setAggregate(status.getAggregate());
//			throw new OverBudgetException(errorInfo);
//		}
//		// in overrun - flex TODO fixme
////		else if (status.getStatusses().contains(ProjectAssignmentStatus.Status.IN_OVERRUN) 
////				&& dbAssignment.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX)
////		{
////			entry = timesheetDAO.getLatestTimesheetEntryForAssignment(assignment.getAssignmentId());
////			
////			if (canNotifyPm(dbAssignment))
////			{
////				mailService.mailPMFlexAllottedReached(status.getAggregate(), 
////														entry.getEntryId().getEntryDate(),
////														dbAssignment.getProject().getProjectManager());
////			}
////
////			ErrorInfo errorInfo = new ErrorInfo(ErrorInfo.ErrorCode.OVER_OVERRUN_FLEX);
////			throw new OverBudgetException(errorInfo);
////		}
//		// over/before deadline
//		else if (status.getStatusses().contains(ProjectAssignmentStatus.Status.BEFORE_START))
//		{
//			ErrorInfo errorInfo = new ErrorInfo(ErrorInfo.ErrorCode.BEFORE_START);
//			errorInfo.setAssignment(dbAssignment);
//			errorInfo.setAggregate(status.getAggregate());
//			throw new OverBudgetException(errorInfo);
//		}
//		else if (status.getStatusses().contains(ProjectAssignmentStatus.Status.AFTER_DEADLINE))
//		{
//			ErrorInfo errorInfo = new ErrorInfo(ErrorInfo.ErrorCode.AFTER_DEADLINE);
//			errorInfo.setAssignment(dbAssignment);
//			errorInfo.setAggregate(status.getAggregate());
//			throw new OverBudgetException(errorInfo);
//		}
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
