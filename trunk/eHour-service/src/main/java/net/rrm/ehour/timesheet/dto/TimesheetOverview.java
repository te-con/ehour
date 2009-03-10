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
