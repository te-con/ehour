package net.rrm.ehour.domain;

import java.util.Date;

/**
 * Created on Feb 7, 2010 2:47:20 PM
 *
 * @author thies (www.te-con.nl)
 */
public class TimesheetEntryObjectMother {
    public static TimesheetEntry createTimesheetEntry(int prjId, Date date, float hours) {
        TimesheetEntryId id = new TimesheetEntryId();
        id.setEntryDate(date);
        id.setActivity(ActivityMother.createActivity(prjId, prjId, 1));

        TimesheetEntry entry = new TimesheetEntry();
        entry.setEntryId(id);
        entry.setHours(hours);

        return entry;
    }

    public static TimesheetEntry createTimesheetEntry(User user, Date date, float hours) {
        TimesheetEntryId id = new TimesheetEntryId();
        id.setEntryDate(date);

        Activity activity = ActivityMother.createActivity(1, 1, 1);
        activity.setAssignedUser(user);
        id.setActivity(activity);

        TimesheetEntry entry = new TimesheetEntry();
        entry.setEntryId(id);
        entry.setHours(hours);

        return entry;
    }

}
