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
    public void shouldUseActiveProjectFlagsAndCustomerFilterToManipulateCustomerCriteria() {
        loadReportSection();

        assertEquals(1, countShownCustomers());

        toggleActiveProjects();

        toggleCustomerFilters();

        filterCustomers(anotherActiveCustomer.name.substring(0, 1));

        assertEquals(1, countShownCustomers());
    }

    @Test
    public void filterCustomersShouldStillWorkAfterReloadingCustomerList() {
        loadReportSection();

        toggleActiveProjects();
        assertEquals(2, countShownCustomers());

        toggleActiveCustomers();

        toggleCustomerFilters();

        filterCustomers(anotherActiveCustomer.name.substring(0, 1));

        assertEquals(1, countShownCustomers());
    }

    @Test
    public void shouldRetainCustomerFilterAfterModifyingSortOrder() {
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
    public void filterProjectsShouldStillWorkAfterReloadingProjectList() {
        loadReportSection();

        toggleActiveProjects();
        assertEquals(3, countShownProjects());

        toggleProjectFilters();

        filterProjects(inactiveProject.name.substring(0, 1));

        assertEquals(2, countShownProjects());
    }

    @Test
    public void shouldRetainProjectFilterAfterModifyingSortOrder() {
        loadReportSection();

        toggleActiveProjects();
        assertEquals(3, countShownProjects());

        toggleProjectFilters();

        filterProjects(inactiveProject.name.substring(0, 1));

        sortProjectsOnCode();

        assertEquals(2, countShownProjects());
    }

    @Test
    public void shouldClearCustomerFilter() {
        loadReportSection();

        toggleCustomerFilters();

        filterCustomers("none");

        clearCustomerCriterium();

        assertEquals(1, countShownCustomers());
    }

    @Test
    public void shouldClearProjectFilter() {
        loadReportSection();

        toggleProjectFilters();

        filterProjects("none");

        clearProjectCriterium();

        assertEquals(1, countShownProjects());
    }
}
