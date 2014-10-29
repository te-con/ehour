package net.rrm.ehour.it.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.ACTIVE_CUSTOMER;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.ItCustomer;
import static net.rrm.ehour.it.driver.ItUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class ProjectDriver {

    public static final ItProject ACTIVE_PROJECT = new ItProject("ET", "ET");
    public static final ItProject INACTIVE_PROJECT = new ItProject("VPRO", "VPRO");
    private static final String PREFIX_FORM = "tabs_panel_projectFormPanel_border_greySquaredFrame_border__body_projectForm_project";

    public static void loadProjectAdmin() {
        Driver.get(BASE_URL + "/eh/admin/project");
    }

    public static ItProject createActiveProjectForActiveCustomer() {
        return createActiveProjectFor(ACTIVE_CUSTOMER);
    }

    public static ItProject createActiveProjectFor(ItCustomer customer) {
        createProject(ACTIVE_PROJECT, customer, true);
        return ACTIVE_PROJECT;
    }

    public static ItProject createInActiveProjectForActiveCustomer() {
        return createInActiveProjectFor(ACTIVE_CUSTOMER);
    }

    public static ItProject createInActiveProjectFor(ItCustomer customer) {
        createProject(INACTIVE_PROJECT, customer, false);
        return INACTIVE_PROJECT;
    }

    public static void createProject(ItProject project, ItCustomer customer, boolean active) {
        loadProjectAdmin();

        fillProjectForm(project, customer, active);

        storeProject();
    }

    public static void fillProjectForm(ItProject project, ItCustomer customer, boolean active) {
        findElement(PREFIX_FORM + ".name").clear();
        findElement(PREFIX_FORM + ".name").sendKeys(project.name);
        findElement(PREFIX_FORM + ".projectCode").clear();
        findElement(PREFIX_FORM + ".projectCode").sendKeys(project.code);

        if (!active) {
            findElement(PREFIX_FORM + ".active").click();
        }

        String cust = String.format("%s - %s", customer.code, customer.name);
        new Select(findElement(PREFIX_FORM + ".customer")).selectByVisibleText(cust);
    }

    public static void storeProject() {
        findElement("tabs_panel_projectFormPanel_border_greySquaredFrame_border__body_projectForm_submitButton").click();

        assertTrue(findElement("tabs_panel_projectFormPanel_border_greySquaredFrame_border__body_projectForm_serverMessage").getText().matches("^[\\s\\S]*Data saved[\\s\\S]*$"));
    }

    public static void editProject(String projectName) {
        WebElement listFilter = findElement(By.id("listFilter"));
        listFilter.clear();
        listFilter.sendKeys(projectName);

        findElement("entrySelectorFrame_entrySelectorFrame__body_projectSelector_entrySelectorFrame_blueBorder_blueBorder__body_listScroll_itemList_0").click();
        sleep();
        waitForValue("tabs_panel_projectFormPanel_border_greySquaredFrame_border__body_projectForm_project.name", projectName);
    }

    public static void showOnlyActiveProjects() {
        findElement("entrySelectorFrame_entrySelectorFrame__body_projectSelector_entrySelectorFrame_filterForm_filterToggle").click();
    }

    public static void assertProjectLoaded(String projectName) {
        String input = findElement(PREFIX_FORM + ".name").getAttribute("value");

        assertEquals(projectName, input);
    }

    public static void newAssignment() {
        findElement("tabs_panel_assignedUserPanel_border_border__body_list_entrySelectorFrame_filterForm_filterInputContainer_addUsers").click();
    }

    public static void selectUser(int index) {
        findElement("tabs_panel_assignedUserPanel_border_border__body_list_allBorder_allBorder__body_users_" + index + "_name").click();
    }

    public static void setRateForUser(String rate) {
        String path = "tabs_panel_assignedUserPanel_border_border__body_form_border_assignmentForm_formComponents_rateRole_projectAssignment.hourlyRate";
        findElement(path).clear();
        findElement(path).sendKeys(rate);
    }

    public static void submitAssignment() {
        findElement("tabs_panel_assignedUserPanel_border_border__body_form_border_assignmentForm_submitButton").click();
    }

    public static void selectAssignment(int index) {
        findElement("tabs_panel_assignedUserPanel_border_border__body_list_entrySelectorFrame_blueBorder_blueBorder__body_listScroll_itemList_" + index + "_name").click();
    }

    public static void selectProjectManager(String name) {
        new Select(findElement(PREFIX_FORM + ".projectManager")).selectByVisibleText(name);
    }

    public static class ItProject {
        public String name;
        public String code;

        public ItProject(String name, String code) {
            this.name = name;
            this.code = code;
        }
    }
}
