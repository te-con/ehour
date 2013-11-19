package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Ignore;
import org.junit.Test;

import static net.rrm.ehour.it.driver.EhourApplicationDriver.login;
import static net.rrm.ehour.it.driver.ReportApplicationDriver.assertCriteriaLoaded;
import static net.rrm.ehour.it.driver.ReportApplicationDriver.loadReport;
import static net.rrm.ehour.it.driver.TimesheetApplicationDriver.createUserAndAssign;

public class ReportScenario extends AbstractScenario {
    @Test
    public void should_access_report_as_individual_user() {
        createUserAndAssign();

        login("thies", "a");

        loadReport();

        assertCriteriaLoaded();
    }

    @Test
    @Ignore
    public void should_access_report_as_pm() {
    }

    @Test
    @Ignore
    public void should_access_report_as_global() {
    }
}
