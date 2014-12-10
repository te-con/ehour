package net.rrm.ehour.ui.timesheet.panel.weeklycomment;

import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.component.CommonModifiers;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class WeeklyCommentPanel extends AbstractBasePanel<TimesheetContainer> {
    public WeeklyCommentPanel(String id, IModel<TimesheetContainer> model) {
        super(id, model);

        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("commentsFrame");

        Timesheet timesheet = getPanelModelObject().getTimesheet();

        KeepAliveTextArea textArea = new KeepAliveTextArea("commentsArea", new PropertyModel<String>(timesheet, "comment.comment"));
        textArea.add(CommonModifiers.tabIndexModifier(2));
        blueBorder.add(textArea);

        add(blueBorder);
    }
}
