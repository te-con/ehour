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

<<<<<<< HEAD
import com.google.common.collect.Lists;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Value object for timesheet entries of a week and corresponding comments
 */

public class WeekOverview implements Serializable {
    private static final long serialVersionUID = -3281374385102106958L;
    private final Map<ProjectAssignment,Map<String,TimesheetEntry>> assignmentMap;

    public final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    private List<TimesheetEntry> timesheetEntries;
    private TimesheetComment comment;
    private List<ProjectAssignment> projectAssignments;
    private DateRange weekRange;
    private User user;
    private List<Date> lockedDays;

    public WeekOverview(List<TimesheetEntry> timesheetEntries, List<ProjectAssignment> projectAssignments) {
        this.timesheetEntries = timesheetEntries;
        this.projectAssignments = projectAssignments;

        assignmentMap = mergeUnbookedAssignments(createAssignmentMap());
        weekRange = new DateRange(new Date(), new Date());
        lockedDays = Lists.newArrayList();
    }

    public WeekOverview(List<TimesheetEntry> timesheetEntries, TimesheetComment comment, List<ProjectAssignment> projectAssignments, DateRange weekRange, User user, List<Date> lockedDates) {
        this(timesheetEntries, projectAssignments);
        this.comment = comment;
        this.weekRange = weekRange;
        this.user = user;
        this.lockedDays = lockedDates;
    }

    public Map<ProjectAssignment, Map<String, TimesheetEntry>> getAssignmentMap() {
        return assignmentMap;
    }

    private Map<ProjectAssignment, Map<String, TimesheetEntry>> createAssignmentMap() {
        Map<ProjectAssignment, Map<String, TimesheetEntry>> assignmentMap = new HashMap<ProjectAssignment, Map<String, TimesheetEntry>>();

        for (TimesheetEntry entry : getTimesheetEntries()) {
            ProjectAssignment assignment = entry.getEntryId().getProjectAssignment();

            Map<String, TimesheetEntry> entryDateMap = assignmentMap.containsKey(assignment) ? assignmentMap.get(assignment) : new HashMap<String, TimesheetEntry>();

            entryDateMap.put(formatter.format(entry.getEntryId().getEntryDate()), entry);

            assignmentMap.put(assignment, entryDateMap);
        }

        return assignmentMap;
    }

    /**
     * Merge unused project assignments into the week overview
     */
    private Map<ProjectAssignment, Map<String, TimesheetEntry>> mergeUnbookedAssignments(Map<ProjectAssignment, Map<String, TimesheetEntry>> assignmentMap) {
        if (getProjectAssignments() != null) {
            for (ProjectAssignment assignment : getProjectAssignments()) {
                if (!assignmentMap.containsKey(assignment)) {
                    assignmentMap.put(assignment, new HashMap<String, TimesheetEntry>());
                }
            }
        }

        return assignmentMap;
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
=======
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;

/**
 * Value object for timesheet entries of a week and corresponding comments
 **/

public class WeekOverview implements Serializable
{
	private static final long serialVersionUID = -3281374385102106958L;
	private List<TimesheetEntry>	timesheetEntries;
	private TimesheetComment		comment;
	private	List<Activity>			activities;
	private	DateRange				weekRange;
	private	User					user;
	private	Set<Customer>			customers;

	/**
	 * 
	 */
	public void initCustomers()
	{
		customers = new HashSet<Customer>();
		
		for (Activity activity : activities) {
			customers.add(activity.getProject().getCustomer());
		}
		
		for (TimesheetEntry entry : timesheetEntries)
		{
			customers.add(entry.getEntryId().getActivity().getProject().getCustomer());
		}
	}
	
	
	/**
	 * Get customser
	 * @return
	 */
	public Set<Customer> getCustomers()
	{
		return customers;
	}
	
	/**
	 * @return the timesheetEntries
	 */
	public List<TimesheetEntry> getTimesheetEntries()
	{
		return timesheetEntries;
	}
	/**
	 * @param timesheetEntries the timesheetEntries to set
	 */
	public void setTimesheetEntries(List<TimesheetEntry> timesheetEntries)
	{
		this.timesheetEntries = timesheetEntries;
	}

	public List<Activity> getActivities() {
		return activities;
	}


	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}


	/**
	 * @return the weekRange
	 */
	public DateRange getWeekRange()
	{
		return weekRange;
	}
	/**
	 * @param weekRange the weekRange to set
	 */
	public void setWeekRange(DateRange weekRange)
	{
		this.weekRange = weekRange;
	}
	/**
	 * @return the comment
	 */
	public TimesheetComment getComment()
	{
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(TimesheetComment comment)
	{
		this.comment = comment;
	}

	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user)
	{
		this.user = user;
	}
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
}
