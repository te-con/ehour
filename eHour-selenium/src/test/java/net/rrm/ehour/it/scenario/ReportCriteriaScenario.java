package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static com.thoughtworks.selenium.SeleneseTestBase.assertEquals;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.*;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.ProjectDriver.*;
import static net.rrm.ehour.it.driver.ReportDriver.*;
import static net.rrm.ehour.it.driver.UserManagementDriver.createReportUser;

public class ReportCriteriaScenario extends AbstractScenario {

    private static ItCustomer anotherActiveCustomer;

    private static boolean initialized;

    @Override
    protected void clearDatabase() throws SQLException {
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        if (!initialized) {
            loginAdmin();
            createReportUser();
            createActiveCustomer();
            createActiveProjectForActiveCustomer();
            createInActiveProjectForActiveCustomer();

            anotherActiveCustomer = createAnotherActiveCustomer();
            createInActiveProjectFor(anotherActiveCustomer);
            logout();

            initialized = true;
        }

        loginReportUser();
    }

    @After
    public void after() {
        logout();
    }

    @Test
    public void should_use_active_project_flags_and_customer_filter_to_manipulate_customer_criteria() {
        loadReportSection();

        assertEquals(1, countShownCustomers());

        toggleActiveProjects();

        assertEquals(2, countShownCustomers());

        toggleCustomerFilters();

        filterCustomers(anotherActiveCustomer.name.substring(0, 1));

        assertEquals(1, countShownCustomers());
    }

    @Test
    public void filter_customers_should_still_work_after_reloading_customer_list() {
        loadReportSection();

        toggleActiveProjects();
        assertEquals(2, countShownCustomers());

        toggleActiveCustomers();

        toggleCustomerFilters();

        filterCustomers(anotherActiveCustomer.name.substring(0, 1));

        assertEquals(1, countShownCustomers());
    }

}
