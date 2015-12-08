package net.rrm.ehour.it.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static net.rrm.ehour.it.driver.ItUtil.waitForValue;
import static org.junit.Assert.assertEquals;

public abstract class UserManagementDriver {


    public static final ItUser REGULAR_USER = new ItUser("thies", "a");
    public static final ItUser REPORTING_USER = new ItUser("report", "a");

    public static ItUser createRegularUser() {
        createUser(REGULAR_USER, "Edeling", "User");
        assertUserDataSaved();

        return REGULAR_USER;
    }

    public static ItUser createReportUser() {
        createUser(REPORTING_USER, "Report", "Reporting");
        assertUserDataSaved();

        return REPORTING_USER;
    }

    public static void createUser(ItUser user, String lastName, String role) {
        loadUserManagement();

        fillUserForm(user, lastName, role);

        submitUserForm();
    }

    public static void submitUserForm() {
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_submitButton").click();
    }

    public static void fillUserForm(ItUser user, String lastName, String role) {
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user.username").clear();
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user.username").sendKeys(user.name);
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user.firstName").clear();
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user.firstName").sendKeys("Thies");
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user.lastName").clear();
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user.lastName").sendKeys(lastName);
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user.email").clear();
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user.email").sendKeys("thies@te-con.nl");
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_password").clear();
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_password").sendKeys(user.password);
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_confirmPassword").clear();
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_confirmPassword").sendKeys(user.password);
        new Select(findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_dept_user.userDepartment")).selectByVisibleText("Internal");
        new Select(findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user.userRoles")).selectByVisibleText(role);
    }

    public static void editUser(String name) {
        WebElement listFilter = findElement(By.id("listFilter"));
        listFilter.clear();
        listFilter.sendKeys(name);

        findElement("userSelection_border_border__body_entrySelectorFrame_entrySelectorFrame_blueBorder_blueBorder__body_listScroll_itemList_1").click();
    }

    public static void loadUserManagement() {
        Driver.get(BASE_URL + "/eh/admin/employee");
    }

    public static java.util.List<WebElement> getUserRoles() {
        Select userRoles = new Select(findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user.userRoles"));
        return userRoles.getOptions();
    }

    public static void showOnlyActiveUsers() {
        findElement("userSelection_border_border__body_entrySelectorFrame_entrySelectorFrame_filterForm_filterToggle").click();
    }

    public static void assertUserDataSaved() {
        assertUserServerMessage("Data saved");
    }

    public static void assertUserServerMessage(String expected) {
        assertEquals(expected, findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_serverMessage").getText());
    }

    public static void setFormFieldTo(String field, String value) {
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_user." + field).sendKeys(value);
    }

    public static void waitForUsernameToBe(String username) {
        waitForValue("tabs_panel_border_greySquaredFrame_border__body_userForm_user.username", username);
    }

    public static void tickShowAssignments() {
        findElement("tabs_panel_border_greySquaredFrame_border__body_userForm_showAssignments").click();
    }

    public static class ItUser {
        public String name;
        public String password;

        public ItUser(String name, String password) {
            this.name = name;
            this.password = password;
        }
    }
}
