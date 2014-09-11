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

import net.rrm.ehour.activity.status.ActivityStatus;
import net.rrm.ehour.activity.status.ActivityStatusService;
import net.rrm.ehour.approvalstatus.service.ApprovalStatusService;
import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ApprovalStatus;
import net.rrm.ehour.domain.ApprovalStatusType;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.util.DateUtil;

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
	private	TimesheetDao timesheetDAO;

	@Autowired
	private ActivityStatusService activityStatusService;
	
	@Autowired
	private ApprovalStatusService approvalStatusService;
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.timesheet.service.TimesheetPersister#validateAndPersist(net.rrm.ehour.persistence.persistence.domain.ProjectAssignment, java.util.List, net.rrm.ehour.persistence.persistence.data.DateRange)
	 */
	@Transactional(rollbackFor=OverBudgetException.class,
					propagation=Propagation.REQUIRES_NEW)
	public void validateAndPersist(Activity activity, 
									List<TimesheetEntry> entries,
									DateRange weekRange) throws OverBudgetException
	{
		ActivityStatus beforeStatus = activityStatusService.getActivityStatus(activity);
		
		boolean checkAfterStatus = beforeStatus.isValid();
		
		try
		{
			persistEntries(activity, entries, weekRange, !beforeStatus.isValid());
		}
		catch (OverBudgetException obe)
		{
			// make sure it's re-thrown by checking the after status
			checkAfterStatus = true;
		}

		ActivityStatus afterStatus = activityStatusService.getActivityStatus(activity);
		
		if (checkAfterStatus && !afterStatus.isValid()) {
			throw new OverBudgetException(afterStatus);
		}

		 updateApprovalStatusForActivityWithinWeekRange(activity, weekRange);
		
	}

	private void updateApprovalStatusForActivityWithinWeekRange(Activity activity, DateRange weekRange) {
		DateRange dateRangeForCurrentMonth = DateUtil.getDateRangeForMonth(weekRange.getDateStart());
		
		if (checkThatWeekSpansMonths(weekRange)) {
			updateApprovalStatusForWeekSpanningMonths(activity, weekRange);
		} else {
			updateApprovalStatusForMonth(activity, dateRangeForCurrentMonth);
		}
	}

	private void updateApprovalStatusForWeekSpanningMonths(Activity activity, DateRange weekRange) {
		DateRange currentMonthRange = DateUtil.getDateRangeForMonth(weekRange.getDateStart());
		DateRange nextMonthRange = DateUtil.getDateRangeForMonth(weekRange.getDateEnd());
		
		updateApprovalStatusForMonth(activity, currentMonthRange);
		updateApprovalStatusForMonth(activity, nextMonthRange);
	}

	private void updateApprovalStatusForMonth(Activity activity, DateRange monthRange) {
		List<ApprovalStatus> allApprovalStatuses = approvalStatusService.getApprovalStatusForActivity(activity, monthRange);
		
		if (allApprovalStatuses == null || allApprovalStatuses.isEmpty()) {
			ApprovalStatus approvalStatus = new ApprovalStatus();
			approvalStatus.setActivity(activity);
			approvalStatus.setStatus(ApprovalStatusType.IN_PROGRESS);
			approvalStatus.setStartDate(monthRange.getDateStart());
			approvalStatus.setEndDate(monthRange.getDateEnd());
			approvalStatusService.persist(approvalStatus);
		}

	}

	private Boolean checkThatWeekSpansMonths(DateRange weekRange) {
		Boolean doesWeekSpanMonths = false;
		
		DateRange dateRangeForCurrentMonth = DateUtil.getDateRangeForMonth(weekRange.getDateStart());
		DateRange dateRangeForNextMonth = DateUtil.getDateRangeForMonth(weekRange.getDateEnd());
		
		if(dateRangeForCurrentMonth.getDateStart().before(dateRangeForNextMonth.getDateStart())) {
			doesWeekSpanMonths = true;
		}
		return doesWeekSpanMonths;
	}

	/**
	 * Persist entry by entry
	 * @param assignment
	 * @param entries
	 * @param weekRange
	 * @param onlyLessThanExisting
	 */
	private void persistEntries(Activity activity, List<TimesheetEntry> entries, DateRange weekRange, boolean onlyLessThanExisting) throws OverBudgetException
	{
		List<TimesheetEntry> previousEntries = timesheetDAO.getTimesheetEntriesInRange(activity, weekRange);
		
		for (TimesheetEntry entry : entries)
		{
			if (!entry.getEntryId().getActivity().equals(activity))
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
				LOGGER.debug("Deleting stale timesheet entry for activity id " + entry.getEntryId().getActivity().getId() +
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
				LOGGER.debug("Deleting timesheet entry for activity id " + existingEntry.getEntryId().getActivity().getId() +
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
			LOGGER.debug("Persisting timesheet entry for activity id " + newEntry.getEntryId().getActivity().getId() +
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
	 * @param timesheetDAO the timesheetDAO to set
	 */
	public void setTimesheetDAO(TimesheetDao timesheetDAO)
	{
		this.timesheetDAO = timesheetDAO;
	}

	public void setActivityStatusService(ActivityStatusService activityStatusService) {
		this.activityStatusService = activityStatusService;
	}

}
