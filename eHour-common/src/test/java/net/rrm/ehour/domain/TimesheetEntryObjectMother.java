package net.rrm.ehour.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created on Feb 7, 2010 2:47:20 PM
 *
 * @author thies (www.te-con.nl)
 */
public class TimesheetEntryObjectMother {
    @Deprecated
    public static TimesheetEntry createTimesheetEntry(int prjId, Date date, float hours) {
        return createTimesheetEntry(prjId, date, new BigDecimal((double) hours));
    }

    public static TimesheetEntry createTimesheetEntry(int prjId, Date date, BigDecimal hours) {
        TimesheetEntryId id = new TimesheetEntryId(
                date, ProjectAssignmentObjectMother.createProjectAssignment(prjId, prjId, 1));
        TimesheetEntry entry = new TimesheetEntry(id, hours);

        return entry;
    }

    @Deprecated
    public static TimesheetEntry createTimesheetEntry(User user, Date date, float hours) {
        return createTimesheetEntry(user, date, new BigDecimal((double) hours));
    }

    public static TimesheetEntry createTimesheetEntry(User user, Date date, BigDecimal hours) {
        ProjectAssignment projectAssignment = ProjectAssignmentObjectMother.createProjectAssignment(1, 1, 1);
        projectAssignment.setUser(user);
        TimesheetEntryId id = new TimesheetEntryId(date, projectAssignment);

        id.setProjectAssignment(projectAssignment);

        TimesheetEntry entry = new TimesheetEntry();
        entry.setEntryId(id);
        entry.setHours(hours);

        return entry;
    }

}
