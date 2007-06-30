/**
 * Created on Dec 29, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.timesheet.util;

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
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.ui.panel.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.panel.timesheet.dto.TimesheetCell;
import net.rrm.ehour.ui.panel.timesheet.dto.TimesheetRow;
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
		Map<ProjectAssignment, Map<String, TimesheetEntry>>	assignmentMap;
		List<Date>	 										dateSequence;
		List<TimesheetRow>									timesheetRows = null;
		Timesheet											timesheet;
		SortedMap<Customer, List<TimesheetRow>>				customerMap;
		
		// TODO move to joda
		dateSequence = DateUtil.createDateSequence(weekOverview.getWeekRange(), config);
		
		assignmentMap = createAssignmentMap(weekOverview);
		mergeUnbookedAssignments(weekOverview, assignmentMap);
		
		timesheetRows = createTimesheetRows(assignmentMap, dateSequence, weekOverview.getProjectAssignments());
		
		customerMap = structureRowsPerCustomer(timesheetRows);
		
		sortCustomerMap(customerMap);
		
		timesheet = new Timesheet();
		timesheet.setCustomers(customerMap);
		timesheet.setDateSequence((Date[])dateSequence.toArray(new Date[7]));
		timesheet.setWeekStart(weekOverview.getWeekRange().getDateStart());		
	
		timesheet.setComment(weekOverview.getComment());
		timesheet.setUser(weekOverview.getUser());
		timesheet.setFoldPreferences(weekOverview.getFoldPreferences());
		
		return timesheet;
	}
	
	/**
	 * Sort the timesheet rows in a customer map
	 * @param customerMap
	 */
	private void sortCustomerMap(SortedMap<Customer, List<TimesheetRow>> customerMap)
	{
		for (Customer customer : customerMap.keySet())
		{
			Collections.sort(customerMap.get(customer), new TimesheetRowComparator());
		}
	}
	
	/**
	 * Structure timesheet rows per customers
	 * @param rows
	 * @return
	 */
	private SortedMap<Customer, List<TimesheetRow>> structureRowsPerCustomer(List<TimesheetRow> rows)
	{
		SortedMap<Customer, List<TimesheetRow>>	customerMap = new TreeMap<Customer, List<TimesheetRow>>();
		Customer			customer;
		List<TimesheetRow>	timesheetRows;
		
		for (TimesheetRow timesheetRow : rows)
		{
			customer = timesheetRow.getProjectAssignment().getProject().getCustomer();
			
			if (customerMap.containsKey(customer))
			{
				timesheetRows = customerMap.get(customer);
			}
			else
			{
				timesheetRows = new ArrayList<TimesheetRow>();
				timesheetRows.add(timesheetRow);
			}
			
			customerMap.put(customer, timesheetRows);
		}
		
		return customerMap;
	}
	
	/**
	 * Create the timesheet rows
	 * @param assignmentMap
	 * @param dateSequence
	 * @return
	 */
	private List<TimesheetRow> createTimesheetRows(Map<ProjectAssignment, Map<String, TimesheetEntry>> assignmentMap, 
													List<Date> dateSequence,
													List<ProjectAssignment> validProjectAssignments)
	{
		List<TimesheetRow> 	timesheetRows = new ArrayList<TimesheetRow>();
		TimesheetRow		timesheetRow;
		TimesheetEntry		entry;
		Calendar			calendar;
		Calendar			sundayDate = DateUtil.getCalendar(config);
		sundayDate.setTime(dateSequence.get(0));
		
		for (ProjectAssignment assignment : assignmentMap.keySet())
		{
			timesheetRow = new TimesheetRow();
			timesheetRow.setProjectAssignment(assignment);
			timesheetRow.setSundayDate(sundayDate);
			
			// create a cell for every requested date
			for (Date date : dateSequence)
			{
				entry = assignmentMap.get(assignment).get(keyDateFormatter.format(date));
				calendar = DateUtil.getCalendar(config);
				calendar.setTime(date);
				
				timesheetRow.addTimesheetCell(calendar.get(Calendar.DAY_OF_WEEK) - 1,
												createTimesheetCell(assignment, entry, date, validProjectAssignments));
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
	private TimesheetCell createTimesheetCell(ProjectAssignment assignment,
												TimesheetEntry entry, Date date,
												List<ProjectAssignment> validProjectAssignments)
	{
		TimesheetCell	cell = new TimesheetCell();
		
		cell.setTimesheetEntry(entry);
		cell.setValid(isCellValid(assignment, validProjectAssignments, date));
		
		return cell;
	}

	/**
	 * Check if the cell is still valid. Even if they're in the timesheet entries it can be that time allotted
	 * assignments are over their budget or default assignments are de-activated
	 * @param assignment
	 * @param validProjectAssignments
	 * @param date
	 * @return
	 */
	private boolean isCellValid(ProjectAssignment assignment, 
								List<ProjectAssignment> validProjectAssignments,
								Date date)
	{
		boolean isValid = false;
		
		// first check if it's in valid project assignments (time allotted can have values
		// but not be valid anymore)
		isValid = validProjectAssignments.contains(assignment);
		
		isValid = isValid && DateUtil.isDateWithinRange(date, assignment.getDateRange());
		
		return isValid;
	}
	
	/**
	 * Merge unused project assignments into the week overview
	 * @param weekOverview
	 * @return
	 */
	private void mergeUnbookedAssignments(WeekOverview weekOverview,
											Map<ProjectAssignment, Map<String, TimesheetEntry>> assignmentMap)
	{
		for (ProjectAssignment assignment : weekOverview.getProjectAssignments())
		{
			if (!assignmentMap.containsKey(assignment))
			{
				assignmentMap.put(assignment, new HashMap<String, TimesheetEntry>());
			}
		}
	}	
	
	/**
	 * Create a map of the project assignments and the timesheet entries
	 * @param weekOverview
	 * @return
	 */
	private Map<ProjectAssignment, Map<String, TimesheetEntry>> createAssignmentMap(WeekOverview weekOverview)
	{
		ProjectAssignment assignment;
		Map<String, TimesheetEntry>	entryDateMap;	
		Map<ProjectAssignment, Map<String, TimesheetEntry>>	assignmentMap;
		
		assignmentMap = new HashMap<ProjectAssignment, Map<String, TimesheetEntry>>();	
		
		for (TimesheetEntry entry : weekOverview.getTimesheetEntries())
		{
			assignment = entry.getEntryId().getProjectAssignment();
			
			if (assignmentMap.containsKey(assignment))
			{
				entryDateMap = assignmentMap.get(assignment);
			}
			else
			{
				entryDateMap = new HashMap<String, TimesheetEntry>();
			}
			
			entryDateMap.put(keyDateFormatter.format(entry.getEntryId().getEntryDate()), entry);
			
			assignmentMap.put(assignment, entryDateMap);
		}
		
		return assignmentMap;
	}
	
}
