package net.rrm.ehour.ui.timesheet.panel.renderer;

import net.rrm.ehour.ui.timesheet.dto.TimesheetCell;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import net.rrm.ehour.ui.timesheet.panel.TimesheetRowList;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public interface TimesheetIconRenderFactory extends PrioritizedRenderFactory {
    Panel renderForId(String id, TimesheetCell cell, TimesheetRowList.DayStatus status, IModel<TimesheetContainer> timesheetContainerModel);
}
