package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.WicketBy;
import org.junit.Ignore;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginRegularUser;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static net.rrm.ehour.it.driver.ItUtil.sleep;
import static net.rrm.ehour.it.driver.TimesheetDriver.*;
import static org.junit.Assert.assertEquals;

// TODO should be re-enabled
@Ignore
public abstract class TimesheetScenario extends AbstractScenario {
    @Test
    public void should_book_hours() {
        createUserAndAssign();

        loginRegularUser();

        amIOnTheTimesheet();

        book8Hours();
    }

    @Test
    public void should_not_delete_previous_day_comment_when_cancelling_edit_comment() {
        createUserAndAssign();

        loginRegularUser();

        clickInWeek(1);
        String comment = "this is a comment";
        addDayComment(2, comment);
        sleep();

        openDayCommentModal(2);
        cancelDayCommentModal(2);

        String base = openDayCommentModal(2);// now this should be filled by the previous booking
        assertEquals(comment, findElement(WicketBy.wicketPath(base + "_dayWin_content_comment")).getText());
    }

    private void book8Hours() {
        clickInWeek(1);

        bookHours(2, 8f);

        addDayComment(2, "some comment");

        submitTimesheet();

        assertServerMessage("8 hours booked");
    }
}
