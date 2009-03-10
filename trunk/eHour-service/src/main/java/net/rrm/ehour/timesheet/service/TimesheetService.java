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

package net.rrm.ehour.timesheet.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.WeekOverview;

/**
 * Provides services for displaying and manipulating timesheets.
 * Methods are organized by their functionality rather than technical impact. * @author Thies
 *
 */
public interface TimesheetService
{
	/**
	 * Fetch the timesheet overview for a user. This returns an object containing the project assignments for the
	 * requested month and a list with all timesheet entries for that month.
	 * @param userId
	 * @param requestedMonth only the month and year of the calendar is used
	 * @return TimesheetOverviewAction
	 */
	public TimesheetOverview getTimesheetOverview(User user, Calendar requestedMonth);
	
	/**
	 * Get a list with all day numbers in this month that has complete booked days (config defines the completion
	 * level). 
	 * @param userId
	 * @param requestedMonth
	 * @return List with Integers of complete booked days
	 * @throws ObjectNotFoundException
	 */
	public List<BookedDay> getBookedDaysMonthOverview(Integer userId, Calendar requestedMonth);
	
	/**
	 * Get week overview for a date. Weeknumber of supplied requested week is used
	 * @param userId
	 * @param requestedWeek
	 * @return
	 */
	public WeekOverview getWeekOverview(User userId, Calendar requestedWeek, EhourConfig config);
	
	/**
	 * Persist timesheet entries and comment
	 * @param timesheetEntries
	 * @param timesheetComment
	 * @param weekRange
	 */
	public List<ProjectAssignmentStatus> persistTimesheetWeek(Collection<TimesheetEntry> timesheetEntries, 
																TimesheetComment timesheetComment,
																DateRange weekRange);
	
	/**
	 * Delete timesheet entries booked on assignments
	 * @param assignments
	 * @return
	 */
	public void deleteTimesheetEntries(User user);
}
