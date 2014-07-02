package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.driver.CustomerManagementDriver;
import org.junit.Ignore;
import org.junit.Test;

import static net.rrm.ehour.it.driver.AssignmentAdminDriver.assignToProject;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.createActiveCustomer;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.*;
import static net.rrm.ehour.it.driver.ProjectDriver.*;
import static net.rrm.ehour.it.driver.ProjectManagementDriver.*;
import static net.rrm.ehour.it.driver.UserManagementDriver.*;

public class ProjectManagementScenario  extends AbstractScenario {
    @Test
    public void access_pm_report() {
        ItUser pm = createProjectWithRegularUserAsPM();

        login(pm);
        navigateToPm();
        assertPmLoaded();
    }

    @Test
    @Ignore
    public void modify_assignment() {
        loginAdmin();
        loadUserManagement();
        createRegularUser();

        ItUser pmUser = new ItUser("anton", "pom");
        createUser(pmUser, "Anton", "User");

        CustomerManagementDriver.ItCustomer customer = createActiveCustomer();

        loadProjectAdmin();
        fillProjectForm(ACTIVE_PROJECT, customer, true);
        selectProjectManager("Anton, Thies");
        storeProject();

        assignToProject(REGULAR_USER, ACTIVE_PROJECT);

        logout();

        login(pmUser);

        navigateToPm();

        clickFirstProject();

        makeFirstAssignmentInActive();

        submit();

        assertAssignmentsUpdated();
    }
}