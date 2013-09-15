package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;

import static net.rrm.ehour.it.AbstractScenario.Driver;
import static org.junit.Assert.assertTrue;

public abstract class TimesheetApplicationDriver {
    public static void assertInOverviewPage() {
        assertTrue(Driver.findElement(WicketBy.wicketPath("contentContainer_projectOverview_greyBorder_title")).getText().contains("Aggregated hours"));
    }

    public static void clickInWeek(int week) {
        Driver.findElement(WicketBy.wicketPath("sidePanel_calendarFrame_weeks_" + week)).click();
        EhourApplicationDriver.sleep();
    }

    public static void bookHours(int day, Float hours) {
        Driver.findElement(WicketBy.wicketPath("contentContainer_timesheetFrame_timesheetFrame__body_timesheetForm_blueFrame_blueFrame__body_customers_0_rows_0_day" + day + "_day")).sendKeys(hours.toString());
    }

    public static void addDayComment(int day, String comment) {
        String base = String.format("contentContainer_timesheetFrame_timesheetFrame__body_timesheetForm_blueFrame_blueFrame__body_customers_0_rows_0_day%d", day);

        Driver.findElement(WicketBy.wicketPath(base + "_dayLink")).click();

        EhourApplicationDriver.sleep();

        Driver.findElement(WicketBy.wicketPath(base + "_dayWin_content_comment")).sendKeys(comment);

        Driver.findElement(WicketBy.wicketPath(base + "_dayWin_content_submit")).click();

        EhourApplicationDriver.sleep();
    }

    public static void submitTimesheet() {
        Driver.findElement(WicketBy.wicketPath("contentContainer_timesheetFrame_timesheetFrame__body_timesheetForm_commentsFrame_commentsFrame__body_submitButton")).click();

        EhourApplicationDriver.sleep();
    }

    public static String getServerMessage() {
        return Driver.findElement(WicketBy.wicketPath("contentContainer_timesheetFrame_timesheetFrame__body_timesheetForm_commentsFrame_commentsFrame__body_serverMessage")).getText();
    }
}
