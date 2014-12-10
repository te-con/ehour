package net.rrm.ehour.ui.timesheet.panel.weeklycomment;

import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import net.rrm.ehour.ui.timesheet.panel.renderer.RenderPriority;
import net.rrm.ehour.ui.timesheet.panel.renderer.SectionRenderFactory;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.springframework.stereotype.Service;

@Service
public class WeeklyCommentPanelRenderFactory implements SectionRenderFactory {
    @Override
    public Panel renderForId(String id, IModel<TimesheetContainer> model) {
        return new WeeklyCommentPanel(id, model);
    }

    @Override
    public RenderPriority getPriority() {
        return RenderPriority.LOW;
    }
}
