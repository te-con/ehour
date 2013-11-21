package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.Ignore;
import org.junit.Test;

import static com.thoughtworks.selenium.SeleneseTestBase.assertEquals;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.*;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.ProjectDriver.*;
import static net.rrm.ehour.it.driver.ReportDriver.*;
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

    @Test
    public void should_use_active_flags_and_filter_to_manipulate_customer_criteria() {
        loginAdmin();
        createReportUser();
        createActiveCustomer();
        createActiveProjectForActiveCustomer();
        createInActiveProjectForActiveCustomer();

        ItCustomer anotherActiveCustomer = createAnotherActiveCustomer();
        createInActiveProjectFor(anotherActiveCustomer);
        logout();

        loginReportUser();
        loadReportSection();

        assertEquals(1, countShownCustomers());

        toggleActiveProjects();
        sleep(500);

        assertEquals(2, countShownCustomers());

        toggleCustomerFilters();

        filterCustomers(anotherActiveCustomer.name.substring(0, 1));

        assertEquals(1, countShownCustomers());

        logout();
    }
}
