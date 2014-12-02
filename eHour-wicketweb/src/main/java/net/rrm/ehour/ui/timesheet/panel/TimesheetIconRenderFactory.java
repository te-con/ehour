package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.ui.timesheet.dto.TimesheetCell;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import org.apache.wicket.markup.html.panel.Panel;

public interface TimesheetIconRenderFactory {
    Panel renderForId(String id, TimesheetCell cell, TimesheetRowList.DayStatus status, TimesheetContainer timesheetContainer);
}
