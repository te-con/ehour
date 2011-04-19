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

package net.rrm.ehour.ui.timesheet.dto;

import java.io.Serializable;
import java.util.Date;

import net.rrm.ehour.domain.TimesheetEntry;

/**
 * Representation of a cell (day in a project) in the timesheet
 **/

public class TimesheetCell implements Comparable<TimesheetCell>, Serializable
{
	private static final long serialVersionUID = -2708559856313387714L;
	private TimesheetEntry	timesheetEntry;
	private	boolean			isValid;
	private Date			date;

	/**
	 * @return the isValid
	 */
	public boolean isValid()
	{
		return isValid;
	}
	/**
	 * @param isValid the isValid to set
	 */
	public void setValid(boolean isValid)
	{
		this.isValid = isValid;
	}
	/**
	 * @return the timesheetEntry
	 */
	public TimesheetEntry getTimesheetEntry()
	{
		return timesheetEntry;
	}
	/**
	 * @param timesheetEntry the timesheetEntry to set
	 */
	public void setTimesheetEntry(TimesheetEntry timesheetEntry)
	{
		this.timesheetEntry = timesheetEntry;
	}
	
	
	public int compareTo(TimesheetCell o)
	{
		return getTimesheetEntry().getEntryId().getEntryDate().compareTo(o.getTimesheetEntry().getEntryId().getEntryDate());
	}
	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}
}
