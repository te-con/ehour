package net.rrm.ehour.ui.timesheet.panel.weeklycomment;

import net.rrm.ehour.timesheet.service.TimesheetLockService;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.component.CommonModifiers;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class WeeklyCommentPanel extends AbstractBasePanel<TimesheetContainer> {
    private static final String COMMENT_ID = "weeklyComment";

    @SpringBean
    private TimesheetLockService lockService;

    public WeeklyCommentPanel(String id, IModel<TimesheetContainer> model) {
        super(id, model);

        Timesheet timesheet = getPanelModelObject().getTimesheet();

        Boolean isWeekLocked = lockService.isRangeLocked(timesheet.getWeekStart(), timesheet.getWeekEnd(), timesheet.getUser());

        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("commentsFrame");

        PropertyModel<String> commentModel = new PropertyModel<>(timesheet, "comment.comment");

        if (isWeekLocked) {
            Fragment fragment = new Fragment(COMMENT_ID, "locked", this);
            Label commentLabel = new Label("lockedComment", commentModel);
            fragment.add(commentLabel);
            blueBorder.add(fragment);
        } else {
            Fragment fragment = new Fragment(COMMENT_ID, "input", this);
            KeepAliveTextArea textArea = new KeepAliveTextArea("commentsArea", commentModel);
            fragment.add(textArea);
            fragment.add(CommonModifiers.tabIndexModifier(2));
            blueBorder.add(fragment);
        }

        add(blueBorder);

        if (isWeekLocked && (timesheet.getComment() == null || StringUtils.isBlank(timesheet.getComment().getComment()))) {
            setVisible(false);
        }

    }
}
