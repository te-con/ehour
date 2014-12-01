package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.ui.timesheet.dto.TimesheetCell;
import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.stereotype.Service;

@Service("timesheetOptionRenderFactory")
public class DailyCommentPanelFactory implements TimesheetOptionRenderFactory {
    @Override
    public Panel renderForId(String id, TimesheetCell cell, TimesheetRowList.DayStatus status) {
        return new DailyCommentPanel(id, cell, status);
    }
}
