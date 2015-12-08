package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static net.rrm.ehour.it.driver.CustomerManagementDriver.*;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.ProjectDriver.*;
import static net.rrm.ehour.it.driver.ReportDriver.*;
import static net.rrm.ehour.it.driver.UserManagementDriver.createReportUser;
import static org.junit.Assert.assertEquals;

public class ReportCriteriaScenario extends AbstractScenario {

    private static ItCustomer anotherActiveCustomer;

    private static boolean initialized;
    private static ItProject inactiveProject;

    @Override
    protected void clearDatabase() throws SQLException {
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        if (!initialized) {
            super.clearDatabase();

            loginAdmin();
            createReportUser();
            createActiveCustomer();
            createActiveProjectForActiveCustomer();
            inactiveProject = createInActiveProjectForActiveCustomer();

            anotherActiveCustomer = createAnotherActiveCustomer();
            createInActiveProjectFor(anotherActiveCustomer);

            initialized = true;
            logout();
        }

        loginReportUser();
    }

    @After
    public void logoutAfter() {
        logout();
    }

    @Override
    protected boolean isTruncateBetweenTests() {
        return false;
    }

    @Test
    public void should_use_active_project_flags_and_customer_filter_to_manipulate_customer_criteria() {
        loadReportSection();

        assertEquals(1, countShownCustomers());

        toggleActiveProjects();

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

    @Test
    public void should_retain_customer_filter_after_modifying_sort_order() {
        loadReportSection();

        toggleActiveProjects();
        assertEquals(2, countShownCustomers());

        toggleCustomerFilters();

        filterCustomers(anotherActiveCustomer.name.substring(0, 1));

        assertEquals(1, countShownCustomers());

        sortCustomersOnCode();

        assertEquals(1, countShownCustomers());
    }

    @Test
    public void filter_projects_should_still_work_after_reloading_project_list() {
        loadReportSection();

        toggleActiveProjects();
        assertEquals(3, countShownProjects());

        toggleProjectFilters();

        filterProjects(inactiveProject.name.substring(0, 1));

        assertEquals(2, countShownProjects());
    }

    @Test
    public void should_retain_project_filter_after_modifying_sort_order() {
        loadReportSection();

        toggleActiveProjects();
        assertEquals(3, countShownProjects());

        toggleProjectFilters();

        filterProjects(inactiveProject.name.substring(0, 1));

        sortProjectsOnCode();

        assertEquals(2, countShownProjects());
    }

    @Test
    public void should_clear_customer_filter() {
        loadReportSection();

        toggleCustomerFilters();

        filterCustomers("none");

        clearCustomerCriterium();

        assertEquals(1, countShownCustomers());
    }

    @Test
    public void should_clear_project_filter() {
        loadReportSection();

        toggleProjectFilters();

        filterProjects("none");

        clearProjectCriterium();

        assertEquals(1, countShownProjects());
    }
}
