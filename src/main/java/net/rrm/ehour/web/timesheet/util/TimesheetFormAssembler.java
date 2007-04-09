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

package net.rrm.ehour.web.timesheet.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.web.timesheet.dto.Timesheet;
import net.rrm.ehour.web.timesheet.dto.TimesheetCell;
import net.rrm.ehour.web.timesheet.dto.TimesheetRow;

/**
 * Generates the timesheet for the JSP
 **/

public class TimesheetFormAssembler
{
	/**
	 * Create timesheet form
	 * @param weekOverview
	 * @return
	 */
	public Timesheet createTimesheetForm(WeekOverview weekOverview)
	{
		Map<ProjectAssignment, Map<Date, TimesheetEntry>>	assignmentMap;
		List<Date> 											dateSequence;
		List<TimesheetRow>									timesheetRows = null;
		Timesheet											timesheet;
		
		dateSequence = DateUtil.createDateSequence(weekOverview.getWeekRange());
		
		assignmentMap = createAssignmentMap(weekOverview);
		mergeUnbookedAssignments(weekOverview, assignmentMap);
		
		timesheetRows = createTimesheetRows(assignmentMap, dateSequence, weekOverview.getProjectAssignments());
		
		Collections.sort(timesheetRows, new TimesheetRowComparator());
		
		timesheet = new Timesheet();
		timesheet.setTimesheetRows(timesheetRows);
		timesheet.setDateSequence(dateSequence);
		timesheet.setWeekStart(weekOverview.getWeekRange().getDateStart());		
	
		timesheet.setComment(weekOverview.getComment());
		return timesheet;
	}
	
	
	/**
	 * Create the timesheet rows
	 * @param assignmentMap
	 * @param dateSequence
	 * @return
	 */
	private List<TimesheetRow> createTimesheetRows(Map<ProjectAssignment, Map<Date, TimesheetEntry>> assignmentMap, 
													List<Date> dateSequence,
													List<ProjectAssignment> validProjectAssignments)
	{
		List<TimesheetRow> 	timesheetRows = new ArrayList<TimesheetRow>();
		TimesheetRow		timesheetRow;
		TimesheetEntry		entry;
		
		for (ProjectAssignment assignment : assignmentMap.keySet())
		{
			timesheetRow = new TimesheetRow();
			timesheetRow.setProjectAssignment(assignment);
			
			// create a cell for every requested date
			for (Date date : dateSequence)
			{
				entry = assignmentMap.get(assignment).get(date);
				timesheetRow.addTimesheetCell(createTimesheetCell(assignment, entry, date, validProjectAssignments));
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
		cell.setCellDate(date);
		
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
	protected void mergeUnbookedAssignments(WeekOverview weekOverview,
											Map<ProjectAssignment, Map<Date, TimesheetEntry>> assignmentMap)
	{
		for (ProjectAssignment assignment : weekOverview.getProjectAssignments())
		{
			if (!assignmentMap.containsKey(assignment))
			{
				assignmentMap.put(assignment, new HashMap<Date, TimesheetEntry>());
			}
		}
	}	
	
	/**
	 * Create a map of the project assignments and the timesheet entries
	 * @param weekOverview
	 * @return
	 */
	protected Map<ProjectAssignment, Map<Date, TimesheetEntry>> createAssignmentMap(WeekOverview weekOverview)
	{
		ProjectAssignment assignment;
		Map<Date, TimesheetEntry>	entryDateMap;	
		Map<ProjectAssignment, Map<Date, TimesheetEntry>>	assignmentMap;
		Calendar	cal;
		
		assignmentMap = new HashMap<ProjectAssignment, Map<Date, TimesheetEntry>>();	
		
		for (TimesheetEntry entry : weekOverview.getTimesheetEntries())
		{
			assignment = entry.getEntryId().getProjectAssignment();
			
			if (assignmentMap.containsKey(assignment))
			{
				entryDateMap = assignmentMap.get(assignment);
			}
			else
			{
				entryDateMap = new HashMap<Date, TimesheetEntry>();
			}
			
			// as it's used as the key, make sure it's a java.util.Date and not a java.sql.Date
			cal = new GregorianCalendar();
			cal.setTime(entry.getEntryId().getEntryDate());
			

			entryDateMap.put(cal.getTime(), entry);
			
			assignmentMap.put(assignment, entryDateMap);
		}
		
		return assignmentMap;
	}
	
}
