package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.domain.ProjectAssignment;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class TimesheetInputModifiedEvent {
    private final AjaxRequestTarget target;
    private final int forDayOfWeek;
    private final ProjectAssignment forAssignment;

    public TimesheetInputModifiedEvent(AjaxRequestTarget target, int forDayOfWeek, ProjectAssignment forAssignment) {
        this.target = target;
        this.forDayOfWeek = forDayOfWeek;
        this.forAssignment = forAssignment;
    }

    public AjaxRequestTarget getTarget() {
        return target;
    }

    public int getForDayOfWeek() {
        return forDayOfWeek;
    }

    public ProjectAssignment getForAssignment() {
        return forAssignment;
    }
}
