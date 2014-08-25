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

package net.rrm.ehour.ui.timesheet.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.dto.TimesheetCell;
import net.rrm.ehour.ui.timesheet.dto.TimesheetRow;
import net.rrm.ehour.util.DateUtil;

/**
 * Generates the timesheet backing object
 **/

public class TimesheetAssembler
{
	private EhourConfig 		config;
	private SimpleDateFormat	keyDateFormatter;
	
	/**
	 * 
	 * @param config
	 */
	public TimesheetAssembler(EhourConfig config)
	{
		this.config = config;
		
		keyDateFormatter = new SimpleDateFormat("yyyyMMdd");
	}
	
	/**
	 * Create timesheet form
	 * @param weekOverview
	 * @return
	 */
	public Timesheet createTimesheetForm(WeekOverview weekOverview)
	{
		Map<Activity, Map<String, TimesheetEntry>>	activityMap;
		List<Date>	 										dateSequence;
		List<TimesheetRow>									timesheetRows;
		Timesheet											timesheet;
		SortedMap<Customer, List<TimesheetRow>>				customerMap;
		
		dateSequence = DateUtil.createDateSequence(weekOverview.getWeekRange(), config);

		timesheet = new Timesheet();
		timesheet.setMaxHoursPerDay(config.getCompleteDayHours());
	
		activityMap = createActivityMap(weekOverview);
		mergeUnbookedActivities(weekOverview, activityMap);
		
		timesheetRows = createTimesheetRows(activityMap, dateSequence, weekOverview.getActivities(), timesheet);
		
		customerMap = structureRowsPerCustomer(timesheetRows);
		
		sortCustomerMap(customerMap);
		
		timesheet.setCustomers(customerMap);
		timesheet.setDateSequence(dateSequence.toArray(new Date[7]));
		timesheet.setWeekStart(weekOverview.getWeekRange().getDateStart());		
		timesheet.setWeekEnd(weekOverview.getWeekRange().getDateEnd());
	
		timesheet.setComment(weekOverview.getComment());
		timesheet.setUser(weekOverview.getUser());
		
		return timesheet;
	}
	
	/**
	 * Sort the timesheet rows in a customer map
	 * @param customerMap
	 */
	protected void sortCustomerMap(SortedMap<Customer, List<TimesheetRow>> customerMap)
	{
        for (Map.Entry<Customer, List<TimesheetRow>> entry : customerMap.entrySet())
        {
            Collections.sort(entry.getValue(), TimesheetRowComparator.INSTANCE);
		}
	}
	
	/**
	 * Structure timesheet rows per customers
	 * @param rows
	 * @return
	 */
	protected SortedMap<Customer, List<TimesheetRow>> structureRowsPerCustomer(List<TimesheetRow> rows)
	{
		SortedMap<Customer, List<TimesheetRow>>	customerMap = new TreeMap<Customer, List<TimesheetRow>>();
		Customer			customer;
		List<TimesheetRow>	timesheetRows;
		
		for (TimesheetRow timesheetRow : rows)
		{
			customer = timesheetRow.getActivity().getProject().getCustomer();
			
			if (customerMap.containsKey(customer))
			{
				timesheetRows = customerMap.get(customer);
			}
			else
			{
				timesheetRows = new ArrayList<TimesheetRow>();
			}

			timesheetRows.add(timesheetRow);
			
			customerMap.put(customer, timesheetRows);
		}
		
		return customerMap;
	}
	
	/**
	 * Create the timesheet rows
	 * @param activityMap
	 * @param dateSequence
	 * @return
	 */
	protected List<TimesheetRow> createTimesheetRows(Map<Activity, Map<String, TimesheetEntry>> activityMap, 
													List<Date> dateSequence,
													List<Activity> validActivities,
													Timesheet timesheet)
	{
		List<TimesheetRow> 	timesheetRows = new ArrayList<TimesheetRow>();
		TimesheetRow		timesheetRow;
		TimesheetEntry		entry;
		Calendar			calendar;
		Calendar			firstDate = DateUtil.getCalendar(config);
		firstDate.setTime(dateSequence.get(0));
		
		for (Activity activity : activityMap.keySet())
		{
			timesheetRow = new TimesheetRow(config);
			timesheetRow.setTimesheet(timesheet);
			timesheetRow.setActivity(activity);
			timesheetRow.setFirstDayOfWeekDate(firstDate);

			// create a cell for every requested date
			for (Date date : dateSequence)
			{
				entry = activityMap.get(activity).get(keyDateFormatter.format(date));
				calendar = DateUtil.getCalendar(config);
				calendar.setTime(date);
				
				timesheetRow.addTimesheetCell(calendar.get(Calendar.DAY_OF_WEEK) - 1,
												createTimesheetCell(activity, entry, date, validActivities));
			}
			
			timesheetRows.add(timesheetRow);
		}
		
		return timesheetRows;
	}
	
	/**
	 * Create timesheet cell, a cell is valid when the date is within the assignment valid range
	 * @param assignment
	 * @param entry
	 * @param date
	 * @return
	 */
	protected TimesheetCell createTimesheetCell(Activity activity,
												TimesheetEntry entry, Date date,
												List<Activity> validActivities)
	{
		TimesheetCell	cell = new TimesheetCell();
		
		cell.setTimesheetEntry(entry);
		cell.setValid(isCellValid(activity, validActivities, date));
		cell.setDate(date);
		
		return cell;
	}

	/**
	 * Check if the cell is still valid.
	 * @param assignment
	 * @param validProjectAssignments
	 * @param date
	 * @return
	 */
	protected boolean isCellValid(Activity activity, 
								List<Activity> validActivities,
								Date date)
	{
		// first check if it's in valid project assignments (time allotted can have values
		// but not be valid anymore)
		boolean isValid = validActivities.contains(activity);
		
		if (activity.getDateStart() != null && activity.getDateEnd() != null) {
			DateRange dateRange = new DateRange(activity.getDateStart(), activity.getDateEnd());
			isValid = isValid && DateUtil.isDateWithinRange(date, dateRange);
		}
		return isValid;
	}
	
	/**
	 * Merge unused project assignments into the week overview
	 * @param weekOverview
	 */
	protected void mergeUnbookedActivities(WeekOverview weekOverview,
											Map<Activity, Map<String, TimesheetEntry>> activityMap)
	{
		
		if (weekOverview.getActivities() != null)
		{
			for (Activity activity : weekOverview.getActivities()) {
				
				if (!activityMap.containsKey(activity)) {
					activityMap.put(activity, new HashMap<String, TimesheetEntry>());
				}
				
			}
		}
	}	
	
	/**
	 * @param weekOverview
	 * Create a map of the Activities and the timesheet entries
	 * @return
	 */
	protected Map<Activity, Map<String, TimesheetEntry>> createActivityMap(WeekOverview weekOverview)
	{
		Map<String, TimesheetEntry>	entryDateMap;	
		Map<Activity, Map<String, TimesheetEntry>>	activityMap;
		
		activityMap = new HashMap<Activity, Map<String, TimesheetEntry>>();	

		for (TimesheetEntry entry : weekOverview.getTimesheetEntries())
		{
			Activity activity = entry.getEntryId().getActivity();
			
			if (activityMap.containsKey(activity))
			{
				entryDateMap = activityMap.get(activity);
			}
			else
			{
				entryDateMap = new HashMap<String, TimesheetEntry>();
			}
			
			entryDateMap.put(keyDateFormatter.format(entry.getEntryId().getEntryDate()), entry);
			
			activityMap.put(activity, entryDateMap);
		}
		
		return activityMap;
	}
}
