package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static net.rrm.ehour.it.driver.ReportDriver.*;
import static net.rrm.ehour.it.driver.TimesheetDriver.createUserAndAssign;
import static net.rrm.ehour.it.driver.UserManagementDriver.createReportUser;
import static org.junit.Assert.assertEquals;

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
    public void should_show_zero_bookings_in_the_detailed_report() throws Exception {
        preloadDatabase("report_scenario.dbunit.xml");
        updatePassword("report", "a");

        loginReportUser();

        loadReportSection();

        setStartDate("1/1/2013");
        setEndDate("1/1/2014");

        createReport();

        navigateToDetails();

        toggleOptions();

        clickZeroBookings();

        assertEquals("0", findElement("reportContainer_panel_frame_reportTable_blueFrame_blueFrame__body_reportData_reportTableContainer_reportData_3_cell_6").getText());

        logout();
    }
}
