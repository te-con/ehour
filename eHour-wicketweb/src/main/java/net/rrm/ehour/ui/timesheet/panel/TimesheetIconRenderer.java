package net.rrm.ehour.ui.timesheet.panel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimesheetIconRenderer {
    @Autowired
    private List<TimesheetIconRenderFactory> renderFactories;

    public List<TimesheetIconRenderFactory> getRenderFactories() {
        return renderFactories;
    }
}
