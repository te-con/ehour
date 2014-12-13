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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryId;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Representation of a row in the timesheet form
 **/

public class TimesheetRow implements Serializable
{
	private static final long serialVersionUID = -5800367771424869244L;

	private ProjectAssignment	projectAssignment;
	private	TimesheetCell[]		timesheetCells;
	private	Calendar			firstDayOfWeekDate;
	private Timesheet			timesheet; // parent timesheet
	private ProjectAssignmentStatus assignmentStatus;
	private EhourConfig			config;

	
	public TimesheetRow(EhourConfig config)
	{
		this.config= config;
	}
	
	/**
	 * @return the config
	 */
	public EhourConfig getConfig()
	{
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(EhourConfig config)
	{
		this.config = config;
	}

	/**
	 * @return the assignmentStatus
	 */
	public ProjectAssignmentStatus getAssignmentStatus()
	{
		return assignmentStatus;
	}

	/**
	 * @param assignmentStatus the assignmentStatus to set
	 */
	public void setAssignmentStatus(ProjectAssignmentStatus assignmentStatus)
	{
		this.assignmentStatus = assignmentStatus;
	}
	
	/**
	 * Get status
	 * @return
	 */
	public String getStatus()
	{
        if (assignmentStatus != null && assignmentStatus.getAggregate() != null && assignmentStatus.getAggregate().getAvailableHours() != null
                && assignmentStatus.getAggregate().getAvailableHours().or(0f) < 0) {
            AvailableHours hours = new AvailableHours((int) (getAssignmentStatus().getAggregate().getAvailableHours().or(0f) * -1));

            Localizer localizer = Application.get().getResourceSettings().getLocalizer();

            return localizer.getString("timesheet.errorNoHours", null, new Model<>(hours));
        }

        return "<br />";
    }

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
					entry.setHours(entry.getHours() + remaining);
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
		List<TimesheetEntry> entries = new ArrayList<>();

        if (timesheetCells != null)
		{
            for (TimesheetCell timesheetCell : timesheetCells) {
                // as timesheet entry is lazy fetched in a subsequent http requests, assignment is not set
                if (timesheetCell != null
                        && timesheetCell.getTimesheetEntry() != null) {
                    // new entries got empty entry id
                    if (timesheetCell.getTimesheetEntry().getEntryId() == null) {
                        TimesheetEntryId id = new TimesheetEntryId();

                        id.setProjectAssignment(getProjectAssignment());
                        id.setEntryDate(timesheetCell.getDate());

                        timesheetCell.getTimesheetEntry().setEntryId(id);
                    } else {
                        timesheetCell.getTimesheetEntry().getEntryId().setProjectAssignment(getProjectAssignment());
                    }

                    entries.add(timesheetCell.getTimesheetEntry());
                }
            }
		}
		
		return entries;
	}

	/**
	 * @return the firstDayOfWeekDate
	 */
	public Calendar getFirstDayOfWeekDate()
	{
		return firstDayOfWeekDate;
	}

	/**
	 * @param firstDayOfWeekDate the firstDayOfWeekDate to set
	 */
	public void setFirstDayOfWeekDate(Calendar firstDayOfWeekDate)
	{
		this.firstDayOfWeekDate = firstDayOfWeekDate;
	}

	public Timesheet getTimesheet()
	{
		return timesheet;
	}

	public void setTimesheet(Timesheet timesheet)
	{
		this.timesheet = timesheet;
	}
	
	@SuppressWarnings("serial")
	private static class AvailableHours implements Serializable
	{
		int hours;
		
		public AvailableHours(int hours)
		{
			this.hours = hours;
		}

		// reflection
		@SuppressWarnings("unused")
		public int getHours()
		{
			return hours;
		}
	}

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof TimesheetRow))
        {
            return false;
        }
        TimesheetRow castOther = (TimesheetRow) other;
        return new EqualsBuilder()
                .append(projectAssignment, castOther.projectAssignment)
                .isEquals();
    }


    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append(projectAssignment)
                .toHashCode();
    }
	
}
