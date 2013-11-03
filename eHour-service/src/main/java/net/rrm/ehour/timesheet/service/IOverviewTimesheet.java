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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.List;

/**
 * Provides services for displaying and manipulating timesheets.
 * Methods are organized by their functionality rather than technical impact. * @author Thies
 *
 */
public interface IOverviewTimesheet
{
	/**
	 * Fetch the timesheet overview for a user. This returns an object containing the project assignments for the
	 * requested month and a list with all timesheet entries for that month.
	 * @param userId
	 * @param requestedMonth only the month and year of the calendar is used
	 * @return TimesheetOverviewAction
	 */
	TimesheetOverview getTimesheetOverview(User user, Calendar requestedMonth);
	
	/**
	 * Get a list with all dates in a specific month that have been fully booked
	 * level). 
	 * @param userId
	 * @param requestedMonth
	 * @return List with dates with fully booked dates
	 */
	List<LocalDate> getBookedDaysMonthOverview(Integer userId, Calendar requestedMonth);
	
	/**
	 * Get week overview for a date. Weeknumber of supplied requested week is used
	 * @param userId
	 * @param requestedWeek
	 * @return
	 */
	WeekOverview getWeekOverview(User userId, Calendar requestedWeek, EhourConfig config);
}
