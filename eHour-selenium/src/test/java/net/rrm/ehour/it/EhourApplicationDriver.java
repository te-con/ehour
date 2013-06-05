package net.rrm.ehour.it;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static org.junit.Assert.assertTrue;

public class EhourApplicationDriver {
    public static void loginAdmin() {
        login("admin", "admin");
    }

    public static void login(String username, String password) {
        Driver.manage().deleteAllCookies();

        Driver.get(BASE_URL + "/eh/login");
        Driver.findElement(WicketBy.wicketPath("loginform_username")).clear();
        Driver.findElement(WicketBy.wicketPath("loginform_username")).sendKeys(username);
        Driver.findElement(WicketBy.wicketPath("loginform_password")).sendKeys(password);
        Driver.findElement(By.cssSelector("button.submitButton")).click();

        assertTrue(Driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Logged in as[\\s\\S]*$"));
    }

    public static void logout() {
        Driver.get(BASE_URL + "/eh/logout");
        Driver.manage().deleteAllCookies();
    }

    public static void createCustomer(String name, String code) {
        Driver.get(BASE_URL + "/eh/admin/customer");

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_customerForm_customer.name")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_customerForm_customer.name")).sendKeys(name);
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_customerForm_customer.code")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_customerForm_customer.code")).sendKeys(code);

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_customerForm_submitButton")).click();

        assertTrue(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_customerForm_serverMessage")).getText().matches("^[\\s\\S]*Data saved[\\s\\S]*$"));
    }

    public static void createProject(String name, String code, String customerName, String customerCode) {
        Driver.get(BASE_URL + "/eh/admin/project");

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.name")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.name")).sendKeys(name);
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.projectCode")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.projectCode")).sendKeys(code);
        new Select(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.customer"))).selectByVisibleText(customerCode + " - " + customerName);

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_submitButton")).click();

        assertTrue(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_serverMessage")).getText().matches("^[\\s\\S]*Data saved[\\s\\S]*$"));
    }

    public static void createProjectAssignment(int userIndex, String customerName, String customerCode, String projectName, String projectCode) {
        Driver.get(BASE_URL + "/eh/admin/assignment");

        Driver.findElement(WicketBy.wicketPath("entrySelectorFrame_entrySelectorFrame__body_userSelector_entrySelectorFrame_blueBorder_blueBorder__body_itemListHolder_itemList_" + userIndex + "_itemLink_linkLabel")).click();

        new Select(Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_projectSelection_customer"))).selectByVisibleText(customerCode + " - " + customerName);
        new Select(Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_projectSelection_projectAssignment.project"))).selectByVisibleText(projectCode + " - " + projectName);
        new Select(Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_assignmentType_projectAssignment.assignmentType"))).selectByVisibleText("Date range");
        Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_rateRole_projectAssignment.hourlyRate")).clear();
        Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_rateRole_projectAssignment.hourlyRate")).sendKeys("120");
        Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_submitButton")).click();
    }


    public static void createUser(String username, String password) {
        Driver.get(BASE_URL + "/eh/admin/employee");

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.username")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.username")).sendKeys(username);
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.firstName")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.firstName")).sendKeys("Thies");
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.lastName")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.lastName")).sendKeys("Edeling");
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_email_user.email")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_email_user.email")).sendKeys("thies@te-con.nl");
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_password")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_password")).sendKeys(password);
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_confirmPassword")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_confirmPassword")).sendKeys(password);
        new Select(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.userDepartment"))).selectByVisibleText("Internal");
        new Select(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.userRoles"))).selectByVisibleText("User");

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_submitButton")).click();

        assertTrue(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_serverMessage")).getText().matches("^[\\s\\S]*Data saved[\\s\\S]*$"));
    }


}
