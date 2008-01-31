/**
 * Created on Dec 29, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
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
		
		dateSequence = DateUtil.createDateSequence(weekOverview.getWeekRange(), config);

		timesheet = new Timesheet();
		timesheet.setMaxHoursPerDay(config.getCompleteDayHours());
	
		assignmentMap = createAssignmentMap(weekOverview);
		mergeUnbookedAssignments(weekOverview, assignmentMap);
		
		timesheetRows = createTimesheetRows(assignmentMap, dateSequence, weekOverview.getProjectAssignments(), timesheet);
		
		customerMap = structureRowsPerCustomer(timesheetRows);
		
		sortCustomerMap(customerMap);
		
		timesheet.setCustomers(customerMap);
		timesheet.setDateSequence((Date[])dateSequence.toArray(new Date[7]));
		timesheet.setWeekStart(weekOverview.getWeekRange().getDateStart());		
		timesheet.setWeekEnd(weekOverview.getWeekRange().getDateEnd());
	
		timesheet.setComment(weekOverview.getComment());
		timesheet.setUser(weekOverview.getUser());
		timesheet.setFoldPreferences(weekOverview.getFoldPreferences());
		
		return timesheet;
	}
	
	/**
	 * Sort the timesheet rows in a customer map
	 * @param customerMap
	 */
	protected void sortCustomerMap(SortedMap<Customer, List<TimesheetRow>> customerMap)
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
	protected SortedMap<Customer, List<TimesheetRow>> structureRowsPerCustomer(List<TimesheetRow> rows)
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
			}

			timesheetRows.add(timesheetRow);
			
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
	protected List<TimesheetRow> createTimesheetRows(Map<ProjectAssignment, Map<String, TimesheetEntry>> assignmentMap, 
													List<Date> dateSequence,
													List<ProjectAssignment> validProjectAssignments,
													Timesheet timesheet)
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
			timesheetRow.setTimesheet(timesheet);
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
	protected TimesheetCell createTimesheetCell(ProjectAssignment assignment,
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
	protected boolean isCellValid(ProjectAssignment assignment, 
								List<ProjectAssignment> validProjectAssignments,
								Date date)
	{
		boolean isValid = false;
		
		// first check if it's in valid project assignments (time allotted can have values
		// but not be valid anymore)
		isValid = validProjectAssignments.contains(assignment);
		
		DateRange dateRange = new DateRange(assignment.getDateStart(), assignment.getDateEnd());
		
		isValid = isValid && DateUtil.isDateWithinRange(date, dateRange);
		
		return isValid;
	}
	
	/**
	 * Merge unused project assignments into the week overview
	 * @param weekOverview
	 * @return
	 */
	protected void mergeUnbookedAssignments(WeekOverview weekOverview,
											Map<ProjectAssignment, Map<String, TimesheetEntry>> assignmentMap)
	{
		if (weekOverview.getProjectAssignments() != null)
		{
			for (ProjectAssignment assignment : weekOverview.getProjectAssignments())
			{
				if (!assignmentMap.containsKey(assignment))
				{
					assignmentMap.put(assignment, new HashMap<String, TimesheetEntry>());
				}
			}
		}
	}	
	
	/**
	 * @param weekOverview
	 * Create a map of the project assignments and the timesheet entries
	 * @return
	 */
	protected Map<ProjectAssignment, Map<String, TimesheetEntry>> createAssignmentMap(WeekOverview weekOverview)
	{
		Map<String, TimesheetEntry>	entryDateMap;	
		Map<ProjectAssignment, Map<String, TimesheetEntry>>	assignmentMap;
		
		assignmentMap = new HashMap<ProjectAssignment, Map<String, TimesheetEntry>>();	

		for (TimesheetEntry entry : weekOverview.getTimesheetEntries())
		{
			ProjectAssignment assignment = entry.getEntryId().getProjectAssignment();
			
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
