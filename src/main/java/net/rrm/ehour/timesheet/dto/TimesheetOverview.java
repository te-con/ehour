package net.rrm.ehour.timesheet.dto;

import java.util.List;
import java.util.Map;


public class TimesheetOverview
{
	private	List	projectHours;
	private	Map		timesheetEntries;



	/**
	 * Map of timesheet entries for a specific month, key is day in month, value = timesheet entry
	 * @return
	 */
	public Map getTimesheetEntries()
	{
		return timesheetEntries;
	}

	public void setTimesheetEntries(Map timesheetEntries)
	{
		this.timesheetEntries = timesheetEntries;
	}

	public List getProjectHours()
	{
		return projectHours;
	}

	public void setProjectHours(List projectHours)
	{
		this.projectHours = projectHours;
	}
}
