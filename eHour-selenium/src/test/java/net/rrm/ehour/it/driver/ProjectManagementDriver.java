package net.rrm.ehour.it.driver;

import static com.thoughtworks.selenium.SeleneseTestBase.assertEquals;
import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.createActiveCustomer;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.logout;
import static net.rrm.ehour.it.driver.ProjectDriver.*;
import static net.rrm.ehour.it.driver.UserManagementDriver.createRegularUser;

public abstract class ProjectManagementDriver {
    public static void navigateToPm() {
        Driver.get(BASE_URL + "/eh/pm");
    }

    public static void assertPmLoaded() {
        assertEquals("Project Management", Driver.getTitle());
    }

    public static UserManagementDriver.ItUser createProjectWithRegularUserAsPM() {
        loginAdmin();

        UserManagementDriver.ItUser user = createRegularUser();
        CustomerManagementDriver.ItCustomer customer = createActiveCustomer();

        loadProjectAdmin();
        fillProjectForm(ACTIVE_PROJECT, customer, true);
        selectProjectManager("Edeling, Thies");
        storeProject();

        logout();

        return user;
    }
}
