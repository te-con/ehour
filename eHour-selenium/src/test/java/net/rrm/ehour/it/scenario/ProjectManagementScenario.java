package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.driver.CustomerManagementDriver;
import org.junit.Test;

import static net.rrm.ehour.it.driver.CustomerManagementDriver.createActiveCustomer;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.ProjectDriver.*;
import static net.rrm.ehour.it.driver.ProjectManagementDriver.assertPmLoaded;
import static net.rrm.ehour.it.driver.ProjectManagementDriver.navigateToPm;
import static net.rrm.ehour.it.driver.UserManagementDriver.ItUser;
import static net.rrm.ehour.it.driver.UserManagementDriver.createRegularUser;

public class ProjectManagementScenario  extends AbstractScenario {
    @Test
    public void access_pm_report() {
        loginAdmin();

        ItUser regularUser = createRegularUser();
        CustomerManagementDriver.ItCustomer customer = createActiveCustomer();

        loadProjectAdmin();
        fillProjectForm(ACTIVE_PROJECT, customer, true);
        selectProjectManager("Edeling, Thies");
        storeProject();

        logout();

        login(regularUser);

        navigateToPm();

        assertPmLoaded();
    }
}