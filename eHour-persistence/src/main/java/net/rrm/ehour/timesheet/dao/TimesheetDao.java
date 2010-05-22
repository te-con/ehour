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

package net.rrm.ehour.timesheet.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.dao.GenericDao;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryId;
import net.rrm.ehour.timesheet.dto.BookedDay;

public interface TimesheetDao extends GenericDao<TimesheetEntry, TimesheetEntryId>
{
	/**
	 * Get timesheet entries within date range for a user
	 * @param userId
	 * @param dateStart
	 * @param dateEnd
	 * @return List with TimesheetEntry domain objects
	 */
	public List<TimesheetEntry> getTimesheetEntriesInRange(Integer userId, DateRange dateRange);
	
	/**
	 * Get timesheet entries within date range for an assignment
	 * @param assignment
	 * @param dateRange
	 * @return
	 */
	public List<TimesheetEntry> getTimesheetEntriesInRange(ProjectAssignment assignment, DateRange dateRange);
	
	/**
	 * Get timesheet entries before date
	 * @param userId
	 * @param date
	 * @param assignment 
	 * @return
	 */
	public List<TimesheetEntry> getTimesheetEntriesBefore(ProjectAssignment assignment, Date date);
	
	/**
	 * Get timesheet entries after date
	 * @param userId
	 * @param date
	 * @param assignment
	 * @return
	 */
	public List<TimesheetEntry> getTimesheetEntriesAfter(ProjectAssignment assignment, Date date);
	
	/**
	 * Get cumulated hours per day for a date range
	 * @param userId
	 * @param dateRange
	 * @return List with key values -> key = date, value = hours booked
	 */	
	public List<BookedDay> getBookedHoursperDayInRange(Integer userId, DateRange dateRange);

	/**
	 * Get latest timesheet entry for assignment
	 * @param assignmentId
	 * @return
	 */
	public TimesheetEntry getLatestTimesheetEntryForAssignment(Integer assignmentId);
	
	/**
	 * Delete timesheet entries for assignment
	 * @param assignmentIds
	 * @return entries deleted
	 */
	public int deleteTimesheetEntries(List<Serializable> assignmentIds);
}
