package net.rrm.ehour.ui.timesheet.panel.renderer;

import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public interface SectionRenderFactory extends PrioritizedRenderFactory {
    Panel renderForId(String id, IModel<TimesheetContainer> timesheetContainer);
}
