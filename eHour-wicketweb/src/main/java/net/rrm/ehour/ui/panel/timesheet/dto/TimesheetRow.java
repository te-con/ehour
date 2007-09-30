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

package net.rrm.ehour.ui.panel.timesheet.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.domain.TimesheetEntryId;

/**
 * Representation of a row in the timesheet form
 **/

public class TimesheetRow implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5800367771424869244L;
	private ProjectAssignment	projectAssignment;
	private	TimesheetCell[]		timesheetCells;
	private	Calendar			sundayDate;
	private Timesheet			timesheet; // parent timesheet

	/**
	 * 
	 */
	public void bookRemainingHoursOnRow()
	{
		// only for monday - friday
		for (int day = 1; day <= 5; day++)
		{
			float remaining = timesheet.getRemainingHoursForDay(day);
			
			if (remaining > 0)
			{
				TimesheetEntry  entry = timesheetCells[day].getTimesheetEntry();
				
				if (entry == null)
				{
					entry = new TimesheetEntry();
				}
				
				if (entry.getHours() == null)
				{
					entry.setHours(remaining);
				}
				else
				{
					entry.setHours(entry.getHours().floatValue() + remaining);
				}
				
				timesheetCells[day].setTimesheetEntry(entry);
			}
		}
	}
	
	/**
	 * @return the projectAssignment
	 */
	public ProjectAssignment getProjectAssignment()
	{
		return projectAssignment;
	}
	/**
	 * @param projectAssignment the projectAssignment to set
	 */
	public void setProjectAssignment(ProjectAssignment projectAssignment)
	{
		this.projectAssignment = projectAssignment;
	}
	/**
	 * @return the timesheetCells
	 */
	public TimesheetCell[] getTimesheetCells()
	{
		return timesheetCells;
	}
	/**
	 * @param timesheetCells the timesheetCells to set
	 */
	public void setTimesheetCells(TimesheetCell[] timesheetCells)
	{
		this.timesheetCells = timesheetCells;
	}
	
	/**
	 * Add timesheet cell on specific location
	 * @param dayInWeek
	 * @param cell
	 */
	public void addTimesheetCell(int dayInWeek, TimesheetCell cell)
	{
		if (timesheetCells == null)
		{
			timesheetCells = new TimesheetCell[7];
		}
		
		if (dayInWeek > timesheetCells.length)
		{
			throw new IllegalArgumentException("Trying to insert more than 7 days in a week: " + dayInWeek);
		}
		
		timesheetCells[dayInWeek] = cell;
	}
	
	
	/**
	 * Get all timesheet entries contained in this row
	 * @return
	 */
	public List<TimesheetEntry> getTimesheetEntries()
	{
		List<TimesheetEntry> entries = new ArrayList<TimesheetEntry>();;
		
		if (timesheetCells != null)
		{
			for (int i =0; i < timesheetCells.length; i++)
			{
				// as timesheet entry is lazy fetched in a subsequent http request assignment is not set
				if (timesheetCells[i] != null
						&& timesheetCells[i].getTimesheetEntry() != null) 
				{
					// new entries got empty entry id
					if (timesheetCells[i].getTimesheetEntry().getEntryId() == null)
					{
						TimesheetEntryId id = new TimesheetEntryId();
						Calendar		thisDate = (Calendar)sundayDate.clone();
						thisDate.add(Calendar.DATE, i);
						
						id.setProjectAssignment(getProjectAssignment());
						id.setEntryDate(thisDate.getTime());
						
						timesheetCells[i].getTimesheetEntry().setEntryId(id);
					}
					else
					{
						timesheetCells[i].getTimesheetEntry().getEntryId().setProjectAssignment(getProjectAssignment());
					}
					entries.add(timesheetCells[i].getTimesheetEntry());
				}
			}
		}
		
		return entries;
	}
	/**
	 * @return the sundayDate
	 */
	public Calendar getSundayDate()
	{
		return sundayDate;
	}
	/**
	 * @param sundayDate the sundayDate to set
	 */
	public void setSundayDate(Calendar sundayDate)
	{
		this.sundayDate = sundayDate;
	}
	public Timesheet getTimesheet()
	{
		return timesheet;
	}
	public void setTimesheet(Timesheet timesheet)
	{
		this.timesheet = timesheet;
	}


}
