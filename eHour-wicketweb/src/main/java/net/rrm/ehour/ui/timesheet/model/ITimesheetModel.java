package net.rrm.ehour.ui.timesheet.model;

import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import org.apache.wicket.model.IModel;

import java.util.List;

public interface ITimesheetModel<T extends TimesheetContainer> extends IModel<T> {
    List<ProjectAssignmentStatus> persist();
}
