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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Value object for timesheet entries of a week and corresponding comments
 */

public class WeekOverview implements Serializable {
    private static final long serialVersionUID = -3281374385102106958L;
    private List<TimesheetEntry> timesheetEntries;
    private TimesheetComment comment;
    private List<ProjectAssignment> projectAssignments;
    private DateRange weekRange;
    private User user;
    private List<Date> lockedDays;

    public WeekOverview(List<TimesheetEntry> timesheetEntries, TimesheetComment comment, List<ProjectAssignment> projectAssignments, DateRange weekRange, User user, List<Date> lockedDays) {
        this.timesheetEntries = timesheetEntries;
        this.comment = comment;
        this.projectAssignments = projectAssignments;
        this.weekRange = weekRange;
        this.user = user;
        this.lockedDays = lockedDays;
    }

    public List<TimesheetEntry> getTimesheetEntries() {
        return timesheetEntries;
    }

    public TimesheetComment getComment() {
        return comment;
    }

    public List<ProjectAssignment> getProjectAssignments() {
        return projectAssignments;
    }

    public DateRange getWeekRange() {
        return weekRange;
    }

    public User getUser() {
        return user;
    }

    public List<Date> getLockedDays() {
        return lockedDays;
    }
}
