package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.ui.timesheet.dto.TimesheetCell;
import org.apache.wicket.markup.html.panel.Panel;

public interface TimesheetOptionRenderFactory {
    Panel renderForId(String id, TimesheetCell cell, TimesheetRowList.DayStatus status);
}
