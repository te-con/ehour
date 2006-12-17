package net.rrm.ehour.timesheet.dto;

import java.util.List;
import java.util.Map;

import net.rrm.ehour.report.dto.ProjectReport;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;


public class TimesheetOverview
{
	private	List<ProjectReport>	projectHours;
	private	Map<Integer, List<TimesheetEntry>>	timesheetEntries;



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

	public List<ProjectReport> getProjectHours()
	{
		return projectHours;
	}

	public void setProjectHours(List<ProjectReport> projectHours)
	{
		this.projectHours = projectHours;
	}
}
