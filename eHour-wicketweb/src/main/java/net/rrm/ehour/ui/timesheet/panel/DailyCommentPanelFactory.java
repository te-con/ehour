package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.ui.timesheet.dto.TimesheetCell;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.stereotype.Service;

@Service
public class DailyCommentPanelFactory implements TimesheetIconRenderFactory {
    @Override
    public Panel renderForId(String id, TimesheetCell cell, TimesheetRowList.DayStatus status, TimesheetContainer timesheetContainer) {
        return new DailyCommentPanel(id, cell, status);
    }
}
