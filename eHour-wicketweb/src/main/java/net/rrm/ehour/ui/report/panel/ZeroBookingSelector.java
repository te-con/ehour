package net.rrm.ehour.ui.report.panel;

import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.report.ReportConfig;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

public class ZeroBookingSelector extends AbstractBasePanel<UserSelectedCriteria> {
    private final ReportConfig reportConfig;

    public ZeroBookingSelector(String id, ReportConfig reportConfig, UserSelectedCriteria criteria) {
        super(id, new Model<>(criteria));

        this.reportConfig = reportConfig;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Label("zeroBookingsLabel", new ResourceModel(reportConfig.getZeroBookingsMessageKey())));

        AjaxCheckBox bookingCheckbox = new AjaxCheckBox("toggleShowZeroBookings", new PropertyModel<Boolean>(getDefaultModel(), "showZeroBookings")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                send(ZeroBookingSelector.this, Broadcast.BUBBLE, new ZeroBookingSelectionChangedEvent(target));
            }
        };

        add(bookingCheckbox);
    }
}
