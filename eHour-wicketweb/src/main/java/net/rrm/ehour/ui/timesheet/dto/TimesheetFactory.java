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
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.ui.timesheet.util.TimesheetRowComparator;
import net.rrm.ehour.util.DateUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Generates the timesheet backing object
 **/

public class TimesheetFactory
{
    private EhourConfig 		config;
    private SimpleDateFormat	keyDateFormatter;

    public TimesheetFactory(EhourConfig config)
    {
        this.config = config;

        keyDateFormatter = new SimpleDateFormat("yyyyMMdd");
    }

    /**
     * Create timesheet form
     * @param weekOverview
     * @return
     */
    public Timesheet createTimesheet(WeekOverview weekOverview)
    {
        List<Date> dateSequence = DateUtil.createDateSequence(weekOverview.getWeekRange(), config);
        List<TimesheetDate> timesheetDateSequence = createDateSequence(dateSequence);

        Timesheet timesheet = new Timesheet();
        timesheet.setMaxHoursPerDay(config.getCompleteDayHours());

        Map<ProjectAssignment, Map<String, TimesheetEntry>> assignmentMap = createAssignmentMap(weekOverview);
        mergeUnbookedAssignments(weekOverview, assignmentMap);

        List<TimesheetRow> timesheetRows = createTimesheetRows(assignmentMap, timesheetDateSequence, weekOverview.getProjectAssignments(), timesheet);

        timesheet.setCustomers(structureRowsPerCustomer(timesheetRows));
        timesheet.setDateSequence(dateSequence.toArray(new Date[7]));
        timesheet.setWeekStart(weekOverview.getWeekRange().getDateStart());
        timesheet.setWeekEnd(weekOverview.getWeekRange().getDateEnd());

        timesheet.setComment(weekOverview.getComment());
        timesheet.setUser(weekOverview.getUser());

        return timesheet;
    }

    private List<TimesheetDate> createDateSequence(List<Date> dateSequence) {

        List<TimesheetDate> dates = new ArrayList<TimesheetDate>();

        for (Date date : dateSequence) {
            Calendar calendar = DateUtil.getCalendar(config);
            calendar.setTime(date);
            String formatted = keyDateFormatter.format(date);
            dates.add(new TimesheetDate(date, calendar.get(Calendar.DAY_OF_WEEK) - 1, formatted));
        }

        return dates;
    }

    /**
     * Structure timesheet rows per customers
     * @param rows
     * @return
     */
    private SortedMap<Customer, List<TimesheetRow>> structureRowsPerCustomer(List<TimesheetRow> rows)
    {
        SortedMap<Customer, List<TimesheetRow>> customerMap = new TreeMap<Customer, List<TimesheetRow>>();

        for (TimesheetRow timesheetRow : rows)
        {
            Customer customer = timesheetRow.getProjectAssignment().getProject().getCustomer();

            List<TimesheetRow>  timesheetRows = customerMap.containsKey(customer) ? customerMap.get(customer) : new ArrayList<TimesheetRow>();
            timesheetRows.add(timesheetRow);

            customerMap.put(customer, timesheetRows);
        }

        sortTimesheetRows(customerMap);

        return customerMap;
    }

    private void sortTimesheetRows(SortedMap<Customer, List<TimesheetRow>> rows) {
        Set<Map.Entry<Customer, List<TimesheetRow>>> entries = rows.entrySet();

        for (Map.Entry<Customer, List<TimesheetRow>> entry : entries) {
            Collections.sort(entry.getValue(), TimesheetRowComparator.INSTANCE);
        }
    }

    /**
     * Create the timesheet rows
     * @param assignmentMap
     * @param dateSequence
     * @return
     */
    private List<TimesheetRow> createTimesheetRows(Map<ProjectAssignment, Map<String, TimesheetEntry>> assignmentMap,
                                                   List<TimesheetDate> dateSequence,
                                                   List<ProjectAssignment> validProjectAssignments,
                                                   Timesheet timesheet)
    {
        List<TimesheetRow> 	timesheetRows = new ArrayList<TimesheetRow>();
        Calendar firstDate = DateUtil.getCalendar(config);
        firstDate.setTime(dateSequence.get(0).date);

        for (ProjectAssignment assignment : assignmentMap.keySet())
        {
            TimesheetRow timesheetRow = new TimesheetRow(config);
            timesheetRow.setTimesheet(timesheet);
            timesheetRow.setProjectAssignment(assignment);
            timesheetRow.setFirstDayOfWeekDate(firstDate);

            // create a cell for every requested timesheetDate
            for (TimesheetDate timesheetDate : dateSequence)
            {
                TimesheetEntry entry = assignmentMap.get(assignment).get(timesheetDate.formatted);

                timesheetRow.addTimesheetCell(timesheetDate.dayInWeek,
                        createTimesheetCell(assignment, entry, timesheetDate.date, validProjectAssignments));
            }

            timesheetRows.add(timesheetRow);
        }

        return timesheetRows;
    }

    /**
     * Create timesheet cell, a cell is valid when the timesheetDate is within the assignment valid range
     * @param assignment
     * @param entry
     * @param date
     * @return
     */
    private TimesheetCell createTimesheetCell(ProjectAssignment assignment,
                                              TimesheetEntry entry, Date date,
                                              List<ProjectAssignment> validProjectAssignments)
    {
        TimesheetCell cell = new TimesheetCell();

        cell.setTimesheetEntry(entry);
        cell.setValid(isCellValid(assignment, validProjectAssignments, date));

        // TODO: EHO-62
//        cell.setLocked();
        cell.setDate(date);

        return cell;
    }

    /**
     * Check if the cell is still valid. Even if they're in the timesheet entries it can be that time allotted
     * assignments are over their budget or default assignments are de-activated
     * @param assignment
     * @param validProjectAssignments
     * @param date
     * @return
     */
    private boolean isCellValid(ProjectAssignment assignment,
                                List<ProjectAssignment> validProjectAssignments,
                                Date date)
    {
        // first check if it's in valid project assignments (time allotted can have values
        // but not be valid anymore)
        boolean isValid = validProjectAssignments.contains(assignment);

        DateRange dateRange = new DateRange(assignment.getDateStart(), assignment.getDateEnd());

        isValid = isValid && DateUtil.isDateWithinRange(date, dateRange);

        return isValid;
    }

    /**
     * Merge unused project assignments into the week overview
     * @param weekOverview
     */
    private void mergeUnbookedAssignments(WeekOverview weekOverview,
                                          Map<ProjectAssignment, Map<String, TimesheetEntry>> assignmentMap)
    {
        if (weekOverview.getProjectAssignments() != null)
        {
            for (ProjectAssignment assignment : weekOverview.getProjectAssignments())
            {
                if (!assignmentMap.containsKey(assignment))
                {
                    assignmentMap.put(assignment, new HashMap<String, TimesheetEntry>());
                }
            }
        }
    }

    /**
     * @param weekOverview
     * Create a map of the project assignments and the timesheet entries
     * @return
     */
    private Map<ProjectAssignment, Map<String, TimesheetEntry>> createAssignmentMap(WeekOverview weekOverview)
    {
        Map<ProjectAssignment, Map<String, TimesheetEntry>>	assignmentMap = new HashMap<ProjectAssignment, Map<String, TimesheetEntry>>();

        for (TimesheetEntry entry : weekOverview.getTimesheetEntries())
        {
            ProjectAssignment assignment = entry.getEntryId().getProjectAssignment();

            Map<String, TimesheetEntry> entryDateMap = assignmentMap.containsKey(assignment) ? assignmentMap.get(assignment) : new HashMap<String, TimesheetEntry>();

            entryDateMap.put(keyDateFormatter.format(entry.getEntryId().getEntryDate()), entry);

            assignmentMap.put(assignment, entryDateMap);
        }

        return assignmentMap;
    }

    private class TimesheetDate implements Serializable {
        final Date date;
        final int dayInWeek;
        final String formatted;

        private TimesheetDate(Date date, int dayInWeek, String formatted) {
            this.date = date;
            this.dayInWeek = dayInWeek;
            this.formatted = formatted;
        }
    }
}
