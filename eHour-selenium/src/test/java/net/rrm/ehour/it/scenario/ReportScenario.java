package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
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

        assertGlobalCriteriaLoaded();
        logout();
    }
}
