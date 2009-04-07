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

package net.rrm.ehour.timesheet.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import net.rrm.ehour.domain.TimesheetEntry;


public class TimesheetOverview implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 294150320597773932L;
	
	private	SortedSet<UserProjectStatus>	projectStatus;
	private	Map<Integer, List<TimesheetEntry>>		timesheetEntries;



	/**
	 * Map of timesheet entries for a specific month, key is day in month, value = timesheet entry
	 * @return
	 */
	public Map<Integer, List<TimesheetEntry>> getTimesheetEntries()
	{
		return timesheetEntries;
	}

	public void setTimesheetEntries(Map<Integer, List<TimesheetEntry>> timesheetEntries)
	{
		this.timesheetEntries = timesheetEntries;
	}

	/**
	 * @return the projectStatus
	 */
	public SortedSet<UserProjectStatus> getProjectStatus()
	{
		return projectStatus;
	}

	/**
	 * @param projectStatus the projectStatus to set
	 */
	public void setProjectStatus(SortedSet<UserProjectStatus> projectStatus)
	{
		this.projectStatus = projectStatus;
	}
}
