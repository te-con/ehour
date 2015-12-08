package net.rrm.ehour.ui.timesheet.model;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import org.apache.wicket.model.IModel;

import java.util.Calendar;
import java.util.List;

public interface PersistableTimesheetModel<T extends TimesheetContainer> extends IModel<T> {
    List<ProjectAssignmentStatus> persist() throws TimesheetModel.UnknownPersistenceException;

    void init(User user, Calendar forWeek);
}
