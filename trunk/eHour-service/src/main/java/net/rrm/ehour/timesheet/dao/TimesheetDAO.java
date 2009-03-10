/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour.timesheet.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.dao.GenericDAO;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryId;
import net.rrm.ehour.timesheet.dto.BookedDay;

public interface TimesheetDAO extends GenericDAO<TimesheetEntry, TimesheetEntryId>
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
