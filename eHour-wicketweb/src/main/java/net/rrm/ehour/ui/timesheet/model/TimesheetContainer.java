package net.rrm.ehour.ui.timesheet.model;

import net.rrm.ehour.ui.timesheet.dto.Timesheet;

import java.io.Serializable;

public class TimesheetContainer implements Serializable {
    private Timesheet timesheet;

    public TimesheetContainer(Timesheet timesheet) {
        this.timesheet = timesheet;
    }

    public Timesheet getTimesheet() {
        return timesheet;
    }
}
