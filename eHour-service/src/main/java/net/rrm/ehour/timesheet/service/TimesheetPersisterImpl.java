/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.timesheet.service;

import java.util.Date;
import java.util.List;

import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.timesheet.dao.TimesheetDao;
import net.rrm.ehour.util.EhourConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Timesheet persister
 **/
@NonAuditable
@Service("timesheetPersister")
public class TimesheetPersisterImpl implements TimesheetPersister
{
	private	static final Logger	LOGGER = Logger.getLogger(TimesheetPersisterImpl.class);

	@Autowired
	private	TimesheetDao		timesheetDAO;

	@Autowired
	private ProjectAssignmentStatusService	projectAssignmentStatusService;
	
	@Autowired
	private MailService			mailService;
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.service.TimesheetPersister#validateAndPersist(net.rrm.ehour.domain.ProjectAssignment, java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@Transactional(rollbackFor=OverBudgetException.class,
					propagation=Propagation.REQUIRES_NEW)
	public void validateAndPersist(ProjectAssignment assignment, 
									List<TimesheetEntry> entries,
									DateRange weekRange) throws OverBudgetException
	{
		ProjectAssignmentStatus beforeStatus = projectAssignmentStatusService.getAssignmentStatus(assignment);
		
		boolean checkAfterStatus = beforeStatus.isValid();
		
		try
		{
			persistEntries(assignment, entries, weekRange, !beforeStatus.isValid());
		}
		catch (OverBudgetException obe)
		{
			// make sure it's retrown by checking the after status
			checkAfterStatus = true;
		}

		ProjectAssignmentStatus afterStatus = projectAssignmentStatusService.getAssignmentStatus(assignment);
		
		if (checkAfterStatus && !afterStatus.isValid())
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
	 * Persist entry by entry
	 * @param assignment
	 * @param entries
	 * @param weekRange
	 * @param onlyLessThanExisting
	 */
	private void persistEntries(ProjectAssignment assignment, List<TimesheetEntry> entries, DateRange weekRange, boolean onlyLessThanExisting) throws OverBudgetException
	{
		List<TimesheetEntry> previousEntries = timesheetDAO.getTimesheetEntriesInRange(assignment, weekRange);
		
		for (TimesheetEntry entry : entries)
		{
			if (!entry.getEntryId().getProjectAssignment().equals(assignment))
			{
				LOGGER.error("Invalid entry in assignment list, skipping: " + entry);
				continue;
			}
			
			if (entry.isEmptyEntry())
			{
				deleteEntry(getEntry(previousEntries, entry));
			}
			else
			{
				persistEntry(onlyLessThanExisting, entry, getEntry(previousEntries, entry));
			}
			
			previousEntries.remove(entry);
		}
		
		removeOldEntries(previousEntries);
	}

	private void removeOldEntries(List<TimesheetEntry> previousEntries)
	{
		for (TimesheetEntry entry : previousEntries)
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("Deleting stale timesheet entry for assignment id " + entry.getEntryId().getProjectAssignment().getAssignmentId() +
						" for date " + entry.getEntryId().getEntryDate() + ", hours booked: " + entry.getHours());
			}
			
			timesheetDAO.delete(entry);
		}
	}
	
	/**
	 * Delete existing entry
	 * @param existingEntry
	 */
	private void deleteEntry(TimesheetEntry existingEntry)
	{
		if (existingEntry != null)
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("Deleting timesheet entry for assignment id " + existingEntry.getEntryId().getProjectAssignment().getAssignmentId() +
						" for date " + existingEntry.getEntryId().getEntryDate() + ", hours booked: " + existingEntry.getHours());
			}
			
			timesheetDAO.delete(existingEntry);
		}		
	}
	
	/**
	 * Persist entry
	 * @param onlyLessThanExisting
	 * @param newEntry
	 * @param existingEntry
	 * @throws OverBudgetException 
	 */
	private void persistEntry(boolean onlyLessThanExisting, TimesheetEntry newEntry, TimesheetEntry existingEntry) throws OverBudgetException
	{
		if (onlyLessThanExisting && 
				(existingEntry == null ||
				  (newEntry.getHours().compareTo(existingEntry.getHours()) > 0)))
		{
			throw new OverBudgetException();
		}
		
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Persisting timesheet entry for assignment id " + newEntry.getEntryId().getProjectAssignment().getAssignmentId() +
					" for date " + newEntry.getEntryId().getEntryDate() + ", hours booked: " + newEntry.getHours()
					+ ", comment: " + newEntry.getComment()
			);
		}
		
		newEntry.setUpdateDate(new Date());

		if (existingEntry != null)
		{
			timesheetDAO.merge(newEntry);
		}
		else
		{
			timesheetDAO.persist(newEntry);
		}		
	}
	
	/**
	 * Get entry from list
	 * @param entries
	 * @param entry
	 * @return
	 */
	private TimesheetEntry getEntry(List<TimesheetEntry> entries, TimesheetEntry entry)
	{
		int index = entries.indexOf(entry);
		
		if (index >= 0)
		{
			return entries.get(index);
		}
		else
		{
			return null;
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
		// in overrun - flex
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
	public void setTimesheetDAO(TimesheetDao timesheetDAO)
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
