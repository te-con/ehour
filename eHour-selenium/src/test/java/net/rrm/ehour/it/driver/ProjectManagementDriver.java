package net.rrm.ehour.it.driver;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.createActiveCustomer;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.loginAdmin;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.logout;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static net.rrm.ehour.it.driver.ProjectDriver.*;
import static net.rrm.ehour.it.driver.UserManagementDriver.createRegularUser;
import static org.junit.Assert.assertEquals;

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

    public static void clickFirstProject() {
        findElement("entrySelectorFrame_entrySelectorFrame__body_projectSelector_entrySelectorFrame_blueBorder_blueBorder__body_listScroll_itemList_0").click();
    }

    public static void makeFirstAssignmentInActive() {
        findElement("content_border_border__body_assignments_assignmentContainer_assignments_0_container").click();
        findElement("content_border_border__body_assignments_assignmentContainer_assignments_0_container_editForm_active").click();
        findElement("content_border_border__body_assignments_assignmentContainer_assignments_0_container_editForm_submit").click();
    }

    public static void submit() {
        findElement("content_border_border__body_submitButton").click();
    }

    public static void assertAssignmentsUpdated() {
        assertEquals("assignments updated", findElement("content_border_border__body_serverMessage").getText());
    }
}
