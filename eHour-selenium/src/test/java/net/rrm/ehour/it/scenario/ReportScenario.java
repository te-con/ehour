package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Ignore;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.ReportDriver.assertCriteriaLoaded;
import static net.rrm.ehour.it.driver.ReportDriver.loadReportSection;
import static net.rrm.ehour.it.driver.TimesheetDriver.createUserAndAssign;
import static net.rrm.ehour.it.driver.UserManagementDriver.createReportUser;

public class ReportScenario extends AbstractScenario {
    @Test
    public void should_access_report_as_individual_user() {
        createUserAndAssign();

        loginRegularUser();

        loadReportSection();

        assertCriteriaLoaded();
    }

    @Test
    @Ignore
    public void should_access_report_as_pm() {
    }

    @Test
    public void should_access_report_as_global() {
        loginAdmin();
        createReportUser();
        logout();

        loginReportUser();
        loadReportSection();

        assertCriteriaLoaded();

        logout();
    }
}
