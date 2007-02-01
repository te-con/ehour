package net.rrm.ehour.timesheet.dto;

import java.util.List;
import java.util.Map;

import net.rrm.ehour.report.project.ProjectAssignmentAggregate;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;


public class TimesheetOverview
{
	private	List<ProjectAssignmentAggregate>	projectHours;
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

	public List<ProjectAssignmentAggregate> getProjectHours()
	{
		return projectHours;
	}

	public void setProjectHours(List<ProjectAssignmentAggregate> projectHours)
	{
		this.projectHours = projectHours;
	}
}
