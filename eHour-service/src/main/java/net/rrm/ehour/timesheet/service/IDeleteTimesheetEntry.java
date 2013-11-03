package net.rrm.ehour.timesheet.service;

import net.rrm.ehour.domain.User;

public interface IDeleteTimesheetEntry {
    /**
     * Delete timesheet entries booked on assignments
     * @param assignments
     * @return
     */
    void deleteAllTimesheetDataForUser(User user);
}
