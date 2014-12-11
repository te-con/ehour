package net.rrm.ehour.ui.timesheet.panel.dailycomments;

import net.rrm.ehour.ui.timesheet.dto.TimesheetCell;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import net.rrm.ehour.ui.timesheet.panel.TimesheetRowList;
import net.rrm.ehour.ui.timesheet.panel.renderer.RenderPriority;
import net.rrm.ehour.ui.timesheet.panel.renderer.TimesheetIconRenderFactory;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.springframework.stereotype.Service;

@Service
public class DailyCommentPanelFactory implements TimesheetIconRenderFactory {
    @Override
    public Panel renderForId(String id, TimesheetCell cell, TimesheetRowList.DayStatus status, IModel<TimesheetContainer> timesheetContainer) {
        return new DailyCommentPanel(id, cell, status);
    }

    @Override
    public RenderPriority getPriority() {
        return RenderPriority.HIGH;
    }
}
