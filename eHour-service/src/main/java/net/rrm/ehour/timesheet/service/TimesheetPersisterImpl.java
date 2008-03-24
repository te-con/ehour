/**
 * Created on Mar 14, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.timesheet.service;

import java.util.Date;
import java.util.List;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.util.EhourConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Timesheet persister
 **/

public class TimesheetPersisterImpl implements TimesheetPersister
{
	private	Logger				logger = Logger.getLogger(TimesheetPersisterImpl.class);

	private	TimesheetDAO		timesheetDAO;
	private ProjectAssignmentStatusService	projectAssignmentStatusService;
	private MailService			mailService;
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.service.TimesheetPersister#persistValidatedTimesheet(net.rrm.ehour.domain.ProjectAssignment, java.util.List)
	 */
	@Transactional(rollbackFor=OverBudgetException.class,
					propagation=Propagation.REQUIRES_NEW)
	public void validateAndPersist(ProjectAssignment assignment, List<TimesheetEntry> entries) throws OverBudgetException
	{
		ProjectAssignmentStatus beforeStatus = projectAssignmentStatusService.getAssignmentStatus(assignment);
		
		persistEntries(assignment, entries);

		ProjectAssignmentStatus afterStatus = projectAssignmentStatusService.getAssignmentStatus(assignment);
		if (!afterStatus.isValid())
		{
			throw new OverBudgetException(afterStatus);
		}
		else if (!beforeStatus.equals(afterStatus)
					&& canNotifyPm(assignment))
		{
			notifyPm(assignment, afterStatus);
		}
	}
	
	/**
	 * Persist entries
	 * @param assignment
	 * @param entries
	 */
	private void persistEntries(ProjectAssignment assignment, List<TimesheetEntry> entries)
	{
		for (TimesheetEntry entry : entries)
		{
			if (!entry.getEntryId().getProjectAssignment().equals(assignment))
			{
				logger.error("Invalid entry in assignment list, skipping: " + entry);
				continue;
			}
			
			if (StringUtils.isBlank(entry.getComment())
					&& (entry.getHours() == null || entry.getHours().equals(0f)))
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Deleting timesheet entry for assignment id " + entry.getEntryId().getProjectAssignment().getAssignmentId() +
							" for date " + entry.getEntryId().getEntryDate() + ", hours booked: " + entry.getHours());
				}
				timesheetDAO.delete(entry);
				
			}
			else
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Persisting timesheet entry for assignment id " + entry.getEntryId().getProjectAssignment().getAssignmentId() +
							" for date " + entry.getEntryId().getEntryDate() + ", hours booked: " + entry.getHours()
							+ ", comment: " + entry.getComment()
					);
				}
				
				entry.setUpdateDate(new Date());
				timesheetDAO.persist(entry);
			}
		}
	}
	
	/**
	 * Notify pm on phase change
	 * @param assignment
	 * @param status
	 */
	private void notifyPm(ProjectAssignment assignment, ProjectAssignmentStatus status)
	{
		TimesheetEntry entry;

		entry = timesheetDAO.getLatestTimesheetEntryForAssignment(assignment.getAssignmentId());


		// over alloted - fixed
		if (assignment.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED
				&& status.getStatusses().contains(ProjectAssignmentStatus.Status.OVER_ALLOTTED)) 
		{
			mailService.mailPMFixedAllottedReached(status.getAggregate(),
													entry.getEntryId().getEntryDate(),
													assignment.getProject().getProjectManager());
		}
		// over overrun - flex
		else if (assignment.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX
					&& status.getStatusses().contains(ProjectAssignmentStatus.Status.OVER_OVERRUN)) 
		{
			mailService.mailPMFlexOverrunReached(status.getAggregate(), 
										entry.getEntryId().getEntryDate(),
										assignment.getProject().getProjectManager());
		}
		// in overrun - flex TODO fixme
		else if (status.getStatusses().contains(ProjectAssignmentStatus.Status.IN_OVERRUN) 
				&& assignment.getAssignmentType().getAssignmentTypeId().intValue() == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX)
		{
			mailService.mailPMFlexAllottedReached(status.getAggregate(), 
													entry.getEntryId().getEntryDate(),
													assignment.getProject().getProjectManager());
		}	
	}
	
	/**
	 * 
	 * @param assignment
	 * @return
	 */
	private boolean canNotifyPm(ProjectAssignment assignment)
	{
		return assignment.isNotifyPm()
				&& assignment.getProject().getProjectManager() != null
				&& assignment.getProject().getProjectManager().getEmail() != null;
		
	}	

	/**
	 * @param timesheetDAO the timesheetDAO to set
	 */
	public void setTimesheetDAO(TimesheetDAO timesheetDAO)
	{
		this.timesheetDAO = timesheetDAO;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService)
	{
		this.mailService = mailService;
	}

	/**
	 * @param projectAssignmentStatusService the projectAssignmentStatusService to set
	 */
	public void setProjectAssignmentStatusService(ProjectAssignmentStatusService projectAssignmentStatusService)
	{
		this.projectAssignmentStatusService = projectAssignmentStatusService;
	}
}
