package net.rrm.ehour.ui.report.panel;

import net.rrm.ehour.ui.common.wicket.Event;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class ZeroBookingSelectionChangedEvent extends Event {
    public ZeroBookingSelectionChangedEvent(AjaxRequestTarget target) {
        super(target);
    }
}
