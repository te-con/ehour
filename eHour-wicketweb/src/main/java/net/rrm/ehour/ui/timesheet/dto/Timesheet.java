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

import net.rrm.ehour.activity.status.ActivityStatus;
import net.rrm.ehour.domain.*;

import java.io.Serializable;
import java.util.*;

/**
 * Representation of a timesheet
 */

public class Timesheet implements Serializable {
    private static final long serialVersionUID = -547682050331580675L;
    private SortedMap<Project, List<TimesheetRow>> projects;
    private Date[] dateSequence;
    private Date weekStart;
    private Date weekEnd;
    private User user;
    private TimesheetComment comment;
    private float maxHoursPerDay;
    private List<Date> lockedDays;

    public boolean isLocked(int seq) {
        return lockedDays.contains(dateSequence[seq]);
    }

    public void setLockedDays(List<Date> lockedDays) {
        this.lockedDays = lockedDays;
    }

    /**
     * Update failed projects
     *
     * @param failedProjectStatusses
     */
    public void updateFailedProjects(List<ActivityStatus> failedProjectStatusses) {
        clearAssignmentStatus();

        for (ActivityStatus activityStatus : failedProjectStatusses) {
            setAssignmentStatus(activityStatus);
        }
    }

    /**
     * Set assignment status
     *
     * @param status
     */
    private void setAssignmentStatus(ActivityStatus status) {
        for (Project project : projects.keySet()) {
            for (TimesheetRow row : projects.get(project)) {
                if (row.getActivity().equals(status.getAggregate().getActivity())) {
                    row.setActivityStatus(status);
                    return;
                }
            }
        }
    }

    /**
     * Clear each assignment status
     */
    private void clearAssignmentStatus() {
        for (Project project : projects.keySet()) {
            for (TimesheetRow row : projects.get(project)) {
                row.setActivityStatus(null);
            }
        }
    }


    /**
     * Get comment for persist
     *
     * @return
     */
    public TimesheetComment getCommentForPersist() {
        // check comment id
        if (getComment().getCommentId() == null) {
            TimesheetCommentId id = new TimesheetCommentId();
            id.setUserId(getUser().getUserId());
            id.setCommentDate(getWeekStart());

            getComment().setCommentId(id);
        }

        return getComment();
    }

    /**
     * Get the timesheet entries of this timesheet
     *
     * @return
     */
    public List<TimesheetEntry> getTimesheetEntries() {
        List<TimesheetEntry> timesheetEntries = new ArrayList<TimesheetEntry>();

        Collection<List<TimesheetRow>> rows = getProjects().values();

        for (List<TimesheetRow> list : rows) {
            for (TimesheetRow timesheetRow : list) {
                timesheetEntries.addAll(timesheetRow.getTimesheetEntries());
            }
        }

        return timesheetEntries;
    }

    /**
     * Get remaining hours for a day based on maxHoursPerDay
     *
     * @param day
     * @return
     */
    public Float getRemainingHoursForDay(int day) {
        float remainingHours = maxHoursPerDay;

        for (Project project : projects.keySet()) {
            for (TimesheetRow row : projects.get(project)) {
                TimesheetCell cell = row.getTimesheetCells()[day];

                if (cell != null && cell.getTimesheetEntry() != null && cell.getTimesheetEntry().getHours() != null) {
                    remainingHours -= cell.getTimesheetEntry().getHours();
                }
            }
        }

        return remainingHours;
    }

    /**
     * Get total booked hours
     *
     * @return
     */
    public Float getTotalBookedHours() {
        float totalHours = 0;

        for (Project project : projects.keySet()) {
            for (TimesheetRow row : projects.get(project))
                for (TimesheetCell cell : row.getTimesheetCells()) {
                    if (cell != null
                            && cell.getTimesheetEntry() != null
                            && cell.getTimesheetEntry().getHours() != null) {
                        totalHours += cell.getTimesheetEntry().getHours();
                    }
                }
        }


        return totalHours;
    }

    /**
     * @return the weekStart
     */
    public Date getWeekStart() {
        return weekStart;
    }

    /**
     * @param weekStart the weekStart to set
     */
    public void setWeekStart(Date weekStart) {
        this.weekStart = weekStart;
    }

    /**
     * @return the comment
     */
    public TimesheetComment getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(TimesheetComment comment) {
        this.comment = comment;
    }

    public SortedMap<Project, List<TimesheetRow>> getProjects() {
        return projects;
    }

    /**
     * @return
     */
    public Collection<Project> getProjectList() {
        return new ArrayList<Project>(getProjects().keySet());
    }

    /**
     * @param project
     * @return
     */
    public List<TimesheetRow> getTimesheetRows(Project project) {
        return projects.get(project);
    }

    /**
     * @param projects the customers to set
     */
    public void setCustomers(SortedMap<Project, List<TimesheetRow>> projects) {
        this.projects = projects;
    }

    /**
     * @return the dateSequence
     */
    public Date[] getDateSequence() {
        return dateSequence;
    }

    /**
     * @param dateSequence the dateSequence to set
     */
    public void setDateSequence(Date[] dateSequence) {
        this.dateSequence = dateSequence.clone();
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    public float getMaxHoursPerDay() {
        return maxHoursPerDay;
    }

    public void setMaxHoursPerDay(float maxHoursPerDay) {
        this.maxHoursPerDay = maxHoursPerDay;
    }

    public Date getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(Date weekEnd) {
        this.weekEnd = weekEnd;
    }

}
