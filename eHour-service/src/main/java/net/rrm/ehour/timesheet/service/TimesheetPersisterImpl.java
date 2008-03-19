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
import net.rrm.ehour.error.ErrorInfo;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.status.StatusChanger;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;

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
	private ProjectAssignmentService	projectAssignmentService;
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.service.TimesheetPersister#persistValidatedTimesheet(net.rrm.ehour.domain.ProjectAssignment, java.util.List)
	 */
	@Transactional(rollbackFor=OverBudgetException.class,
					propagation=Propagation.REQUIRES_NEW)
	@StatusChanger
	public void persistAndNotify(ProjectAssignment assignment, List<TimesheetEntry> entries) throws OverBudgetException
	{
		persistEntries(assignment, entries);
		
		
		try
		{
			projectAssignmentService.checkAndNotify(assignment);	
		}
		catch (OverBudgetException obe)
		{
			ErrorInfo errorInfo = obe.getErrorInfo();
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
	 * @param timesheetDAO the timesheetDAO to set
	 */
	public void setTimesheetDAO(TimesheetDAO timesheetDAO)
	{
		this.timesheetDAO = timesheetDAO;
	}

	/**
	 * @param projectAssignmentService the projectAssignmentService to set
	 */
	public void setProjectAssignmentService(ProjectAssignmentService projectAssignmentService)
	{
		this.projectAssignmentService = projectAssignmentService;
	}
}
