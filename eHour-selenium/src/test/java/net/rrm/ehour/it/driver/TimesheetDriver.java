package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.AbstractScenario;

import java.util.concurrent.TimeUnit;

import static net.rrm.ehour.it.driver.AssignmentAdminDriver.assignToProject;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.ACTIVE_CUSTOMER;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.createActiveCustomer;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.logout;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static net.rrm.ehour.it.driver.ProjectDriver.ACTIVE_PROJECT;
import static net.rrm.ehour.it.driver.ProjectDriver.createActiveProjectForActiveCustomer;
import static net.rrm.ehour.it.driver.UserManagementDriver.*;
import static org.junit.Assert.assertTrue;

public abstract class TimesheetDriver {
    public static void createUserAndAssign() {
        loginAdmin();
        loadUserAdmin();
        createRegularUser();
        createActiveCustomer();
        createActiveProjectForActiveCustomer();
        assignToProject(REGULAR_USER, ACTIVE_CUSTOMER, ACTIVE_PROJECT);

        logout();
    }

    public static void amIOnTheOverviewPage() {
        assertTrue(findElement("contentContainer_projectOverview_greyBorder_title").getText().contains("Aggregated hours"));
    }

    public static boolean isBookingHoursPossible(int day) {
        try {
            AbstractScenario.Driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            findElement("contentContainer_timesheetFrame_timesheetFrame__body_timesheetForm_blueFrame_blueFrame__body_customers_0_rows_0_day" + day + "_day");
            return true;
        } catch (org.openqa.selenium.NoSuchElementException nse) {
            return false;
        }
        finally {
            AbstractScenario.Driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }


    }

    // this can only navigate backwards
    public static void navigateToMonth(String month) {
        while (!findElement("sidePanel_calendarFrame_currentMonth").getText().equals(month)) {
            findElement("sidePanel_calendarFrame_previousMonthLink").click();
        }
    }

    public static void clickInWeek(int week) {
        findElement("sidePanel_calendarFrame_weeks_" + week).click();
    }

    public static void bookHours(int day, Float hours) {
        findElement("contentContainer_timesheetFrame_timesheetFrame__body_timesheetForm_blueFrame_blueFrame__body_customers_0_rows_0_day" + day + "_day").sendKeys(hours.toString());
    }

    public static void addDayComment(int day, String comment) {
        String base = openDayCommentModal(day);

        findElement(base + "_dayWin_content_comment").sendKeys(comment);

        findElement(base + "_dayWin_content_submit").click();

    }

    public static String openDayCommentModal(int day) {
        String base = String.format("contentContainer_timesheetFrame_timesheetFrame__body_timesheetForm_blueFrame_blueFrame__body_customers_0_rows_0_day%d", day);

        findElement(base + "_commentLink").click();

        return base;
    }

    public static void cancelDayCommentModal(int day) {
        String path = String.format("contentContainer_timesheetFrame_timesheetFrame__body_timesheetForm_blueFrame_blueFrame__body_customers_0_rows_0_day%d_dayWin_content_cancel", day);

        findElement(path).click();
    }

    public static void submitTimesheet() {
        findElement("contentContainer_timesheetFrame_timesheetFrame__body_timesheetForm_commentsFrame_commentsFrame__body_submitButton").click();
    }

    public static String getServerMessage() {
        return findElement("contentContainer_timesheetFrame_timesheetFrame__body_timesheetForm_commentsFrame_commentsFrame__body_serverMessage").getText();
    }
}
