package net.rrm.ehour.ui.timesheet.panel;

import com.google.common.collect.Lists;
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

    public void addRenderFactory(TimesheetIconRenderFactory factory) {
        if (renderFactories == null) {
            renderFactories = Lists.newArrayList();
        }

        renderFactories.add(factory);
    }
}
