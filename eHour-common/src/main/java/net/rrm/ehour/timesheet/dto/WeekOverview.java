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

import com.google.common.collect.Lists;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
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
    private final Map<Activity, Map<String, TimesheetEntry>> activityMap;

    public final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    private List<TimesheetEntry> timesheetEntries;
    private TimesheetComment comment;
    private DateRange weekRange;
    private User user;
    private List<Date> lockedDays;

    private List<Activity> activities;

    public WeekOverview(List<TimesheetEntry> timesheetEntries, List<Activity> activities) {
        this.timesheetEntries = timesheetEntries;
        this.activities = activities;

        activityMap = mergeUnbookedActivities(createActivityMap());
        weekRange = new DateRange(new Date(), new Date());
        lockedDays = Lists.newArrayList();
    }

    public WeekOverview(List<TimesheetEntry> timesheetEntries, TimesheetComment comment, List<Activity> activities, DateRange weekRange, User user, List<Date> lockedDates) {
        this(timesheetEntries, activities);
        this.comment = comment;
        this.weekRange = weekRange;
        this.user = user;
        this.lockedDays = lockedDates;
    }

    public Map<Activity, Map<String, TimesheetEntry>> getActivityMap() {
        return activityMap;
    }

    private Map<Activity, Map<String, TimesheetEntry>> createActivityMap() {
        Map<Activity, Map<String, TimesheetEntry>> activityMap = new HashMap<Activity, Map<String, TimesheetEntry>>();

        for (TimesheetEntry entry : getTimesheetEntries()) {
            Activity activity = entry.getEntryId().getActivity();

            Map<String, TimesheetEntry> entryDateMap = activityMap.containsKey(activity) ? activityMap.get(activity) : new HashMap<String, TimesheetEntry>();

            entryDateMap.put(formatter.format(entry.getEntryId().getEntryDate()), entry);

            activityMap.put(activity, entryDateMap);
        }

        return activityMap;
    }

    /**
     * Merge unused project assignments into the week overview
     */
    private Map<Activity, Map<String, TimesheetEntry>> mergeUnbookedActivities(Map<Activity, Map<String, TimesheetEntry>> assignmentMap) {
        if (getActivities() != null) {
            for (Activity activity : getActivities()) {
                if (!assignmentMap.containsKey(activity)) {
                    assignmentMap.put(activity, new HashMap<String, TimesheetEntry>());
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

    public List<Activity> getActivities() {
        return activities;
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
