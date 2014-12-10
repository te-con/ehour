package net.rrm.ehour.ui.timesheet.panel.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimesheetIconRenderFactoryCollection extends  AbstractRenderFactoryCollection<TimesheetIconRenderFactory> {

    protected TimesheetIconRenderFactoryCollection() {
    }

    @Autowired
    public TimesheetIconRenderFactoryCollection(List<? extends TimesheetIconRenderFactory> renderFactories) {
        super(renderFactories);
    }
}
