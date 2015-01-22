package net.rrm.ehour.ui.timesheet.panel.weeklycomment;

import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeeklyCommentPanelTest extends BaseSpringWebAppTester {
    @Mock
    private TimesheetLockService lockService;
    private TimesheetContainer container;
    private Date e;
    private Date s;

    @Before
    public void setup() {
        getMockContext().putBean(lockService);

        Timesheet timesheet = new Timesheet();
        DateTime now = DateTime.now().withTimeAtStartOfDay();
        s = now.toDate();
        e = now.plusDays(2).toDate();

        timesheet.setWeekStart(s);
        timesheet.setWeekEnd(e);
        timesheet.setComment(new TimesheetComment(null, "hi"));

        container = new TimesheetContainer(timesheet);
    }

    @Test
    public void should_render_textarea_when_whole_week_is_not_locked() {
        when(lockService.isRangeLocked(eq(s), eq(e), any(User.class))).thenReturn(false);

        tester.startComponentInPage(new WeeklyCommentPanel("id", Model.of(container)));

        tester.assertComponent("id:commentsFrame:commentsFrame_body:weeklyComment:commentsArea", TextArea.class);
    }

    @Test
    public void should_render_label_when_whole_week_is_locked() {
        when(lockService.isRangeLocked(eq(s), eq(e), any(User.class))).thenReturn(true);

        WeeklyCommentPanel panel = tester.startComponentInPage(new WeeklyCommentPanel("id", Model.of(container)));

        tester.assertComponent("id:commentsFrame:commentsFrame_body:weeklyComment:lockedComment", Label.class);

        assertTrue(panel.isVisible());
    }

    @Test
    public void should_hide_comments_when_when_whole_week_is_locked_and_there_is_no_comment() {
        when(lockService.isRangeLocked(eq(s), eq(e), any(User.class))).thenReturn(true);

        container.getTimesheet().getComment().setComment("");

        WeeklyCommentPanel panel = tester.startComponentInPage(new WeeklyCommentPanel("id", Model.of(container)));

        assertFalse(panel.isVisible());
    }
}