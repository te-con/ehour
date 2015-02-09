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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Representation of a timesheet
 */
public class Timesheet implements Serializable {
    private static final long serialVersionUID = -547682050331580675L;
    @Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE)
    private SortedMap<Customer, List<TimesheetRow>> customers;
    @Setter
    private Date[] dateSequence;
    @Getter @Setter
    private Date weekStart;
    @Getter @Setter
    private Date weekEnd;
    @Getter @Setter
    private User user;
    @Getter @Setter
    private TimesheetComment comment;
    @Setter
    private float maxHoursPerDay;
    private List<Date> lockedDays;

    /**
     * Set assignment status
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

    public void setMappedTimesheetRows(List<TimesheetRow> rows, Comparator<TimesheetRow> comparator) {
        this.setCustomers(new TimeSheetMapBuilder(comparator).buildMap(rows));
    }

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
     * @param failedProjectStatusses
     */
    public void updateFailedProjects(List<ProjectAssignmentStatus> failedProjectStatusses) {
        clearAssignmentStatus();

        for (ProjectAssignmentStatus projectAssignmentStatus : failedProjectStatusses) {
            setAssignmentStatus(projectAssignmentStatus);
        }
    }

    /**
     * Get comment for persist
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
     * @return
     */
    public List<TimesheetEntry> getTimesheetEntries() {
        List<TimesheetEntry> timesheetEntries = new ArrayList<TimesheetEntry>();

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
     * @param day
     * @return
     */
    public Float getRemainingHoursForDay(int day) {
        BigDecimal remainingHours = BigDecimal.valueOf(maxHoursPerDay);

        for (Customer customer : customers.keySet()) {
            for (TimesheetRow row : customers.get(customer)) {
                TimesheetCell cell = row.getTimesheetCells()[day];

                if (cell != null && cell.getTimesheetEntry() != null && cell.getTimesheetEntry().getHours() != null) {
                    remainingHours = remainingHours.subtract(cell.getTimesheetEntry().getHours());
                }
            }
        }

        return remainingHours.floatValue();
    }

    /**
     * Get total booked hours
     * @return
     */
    @SuppressWarnings({ "unused" })
    public Float getTotalBookedHours() {
        BigDecimal totalHours = BigDecimal.ZERO;

        for (Customer customer : customers.keySet()) {
            for (TimesheetRow row : customers.get(customer)) {
                for (TimesheetCell cell : row.getTimesheetCells()) {
                    if (cell != null
                            && cell.getTimesheetEntry() != null
                            && cell.getTimesheetEntry().getHours() != null) {
                        totalHours = totalHours.add(cell.getTimesheetEntry().getHours());
                    }
                }
            }
        }

        return totalHours.floatValue();
    }

    @SuppressWarnings({ "unused" })
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

    private static class TimeSheetMapBuilder {
        private final Comparator<TimesheetRow> comparator;

        TimeSheetMapBuilder(final Comparator<TimesheetRow> comparator) {
            this.comparator = comparator;
        }

        private SortedMap<Customer, List<TimesheetRow>> buildMap(List<TimesheetRow> rows) {
            SortedMap<Customer, List<TimesheetRow>> customerMap = new TreeMap<Customer, List<TimesheetRow>>();

            for (TimesheetRow timesheetRow : rows) {
                Customer customer = timesheetRow.getProjectAssignment().getProject().getCustomer();

                List<TimesheetRow> timesheetRows = customerMap.containsKey(customer) ? customerMap.get(customer) : new ArrayList<TimesheetRow>();
                timesheetRows.add(timesheetRow);

                customerMap.put(customer, timesheetRows);
            }

            sortRows(customerMap);

            return customerMap;
        }

        private void sortRows(SortedMap<Customer, List<TimesheetRow>> rows) {
            Set<Map.Entry<Customer, List<TimesheetRow>>> entries = rows.entrySet();

            for (Map.Entry<Customer, List<TimesheetRow>> entry : entries) {
                Collections.sort(entry.getValue(), comparator);
            }
        }
    }
}
