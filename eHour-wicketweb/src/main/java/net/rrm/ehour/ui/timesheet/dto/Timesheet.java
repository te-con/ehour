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

import net.rrm.ehour.domain.*;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;

import java.io.Serializable;
import java.util.*;

/**
 * Representation of a timesheet
 */

public class Timesheet implements Serializable {
    private static final long serialVersionUID = -547682050331580675L;
    private SortedMap<Customer, List<TimesheetRow>> customers;
    private Date[] dateSequence;
    private Date weekStart;
    private Date weekEnd;
    private User user;
    private TimesheetComment comment;
    private float maxHoursPerDay;
    private List<Date> lockedDays;

    public boolean isAnyLocked() {
        for (int i = 0; i < dateSequence.length; i++) {
            if (isLocked(i)) {
                return true;
            }
        }

        return false;
    }

    public boolean isAllLocked() {
        boolean allLocked = true;

        for (int i = 0; i < dateSequence.length; i++) {
            allLocked &= isLocked(i);
        }

        return allLocked;

    }

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
    public void updateFailedProjects(List<ProjectAssignmentStatus> failedProjectStatusses) {
        clearAssignmentStatus();

        for (ProjectAssignmentStatus projectAssignmentStatus : failedProjectStatusses) {
            setAssignmentStatus(projectAssignmentStatus);
        }
    }

    /**
     * Set assignment status
     *
     * @param status
     */
    private void setAssignmentStatus(ProjectAssignmentStatus status) {
        for (Customer customer : customers.keySet()) {
            for (TimesheetRow row : customers.get(customer)) {
                if (row.getProjectAssignment().equals(status.getAggregate().getProjectAssignment())) {
                    row.setAssignmentStatus(status);
                    return;
                }
            }
        }
    }

    /**
     * Clear each assignment status
     */
    private void clearAssignmentStatus() {
        for (Customer customer : customers.keySet()) {
            for (TimesheetRow row : customers.get(customer)) {
                row.setAssignmentStatus(null);
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
        List<TimesheetEntry> timesheetEntries = new ArrayList<>();

        Collection<List<TimesheetRow>> rows = getCustomers().values();

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

        for (Customer customer : customers.keySet()) {
            for (TimesheetRow row : customers.get(customer)) {
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

        for (Customer customer : customers.keySet()) {
            for (TimesheetRow row : customers.get(customer)) {
                for (TimesheetCell cell : row.getTimesheetCells()) {
                    if (cell != null
                            && cell.getTimesheetEntry() != null
                            && cell.getTimesheetEntry().getHours() != null) {
                        totalHours += cell.getTimesheetEntry().getHours();
                    }
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

    /**
     * @return the customers
     */
    public SortedMap<Customer, List<TimesheetRow>> getCustomers() {
        return customers;
    }

    /**
     * @return
     */
    public List<Customer> getCustomerList() {
        return new ArrayList<>(getCustomers().keySet());
    }

    /**
     * @param customer
     * @return
     */
    public List<TimesheetRow> getTimesheetRows(Customer customer) {
        return customers.get(customer);
    }

    /**
     * @param customers the customers to set
     */
    public void setCustomers(SortedMap<Customer, List<TimesheetRow>> customers) {
        this.customers = customers;
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
