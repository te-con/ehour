package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.WicketBy;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.ItUtil.waitUntil;
import static net.rrm.ehour.it.driver.ReportDriver.*;
import static net.rrm.ehour.it.driver.TimesheetDriver.createUserAndAssign;
import static net.rrm.ehour.it.driver.UserManagementDriver.createReportUser;

public class ReportScenario extends AbstractScenario {
    @Test
    public void should_access_report_as_individual_user() {
        createUserAndAssign();

        loginRegularUser();

        loadReportSection();

        assertUserCriteriaLoaded();
        logout();
    }

    @Test
    public void should_go_back_to_criteria_screen_after_report() {
        createUserAndAssign();

        loginRegularUser();

        loadReportSection();
        assertUserCriteriaLoaded();
        createReport();

        reloadPage();

        assertUserCriteriaLoaded();

        logout();
    }

    @Test
    public void should_access_report_as_global() {
        loginAdmin();
        createReportUser();
        logout();

        loginReportUser();

        loadReportSection();

        assertGlobalCriteriaLoaded();
        logout();
    }

    @Test
    public void should_show_zero_bookings() throws Exception {
        preloadDatabase("report_scenario.dbunit.xml");
        updatePassword("report", "a");

        loginReportUser();

        loadReportSection();

        setStartDate("1/1/2013");
        setEndDate("1/1/2014");

        createReport();

        clickZeroBookings();

        waitUntil(ExpectedConditions.textToBePresentInElementLocated(WicketBy.wicketPath("reportContainer_panel_reportTable_greyFrame_greyFrame__body_reportContent_reportFrame_reportFrame__body_reportFrameContainer_reportTable_3_cell_7"), "0"));

        logout();
    }
}
