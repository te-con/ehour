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
				// as timesheet entry is lazy fetched in a subsequent request assignment is not set
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


}
