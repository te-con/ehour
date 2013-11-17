package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static org.junit.Assert.assertEquals;

public abstract class UserManagementApplicationDriver {

    public static void createUser(String username, String password, String lastName) {
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.username")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.username")).sendKeys(username);
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.firstName")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.firstName")).sendKeys("Thies");
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.lastName")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.lastName")).sendKeys(lastName);
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_email_user.email")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_email_user.email")).sendKeys("thies@te-con.nl");
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_password")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_password")).sendKeys(password);
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_confirmPassword")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_confirmPassword")).sendKeys(password);
        new Select(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.userDepartment"))).selectByVisibleText("Internal");
        new Select(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user.userRoles"))).selectByVisibleText("User");

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_submitButton")).click();
    }

    public static void editUser(String name) {
        WebElement listFilter = Driver.findElement(By.id("listFilter"));
        listFilter.clear();
        listFilter.sendKeys(name);

        Driver.findElement(WicketBy.wicketPath("entrySelectorFrame_entrySelectorFrame__body_userSelector_entrySelectorFrame_blueBorder_blueBorder__body_itemListHolder_itemList_1_itemLink")).click();
        EhourApplicationDriver.sleep(500);
    }

    public static void loadUserAdmin() {
        Driver.get(BASE_URL + "/eh/admin/employee");
    }

    public static void showOnlyActiveUsers() {
        Driver.findElement(WicketBy.wicketPath("entrySelectorFrame_entrySelectorFrame__body_userSelector_entrySelectorFrame_filterForm_filterToggle")).click();
        EhourApplicationDriver.sleep(500);
    }

    public static void submitUserForm() {
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_submitButton")).click();
    }

    public static void assertUserDataSaved() {
        assertUserServerMessage("Data saved");
    }

    public static void assertUserServerMessage(String expected) {
        assertEquals(expected, Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_serverMessage")).getText());
    }

    public static void setFormFieldTo(String field, String value) {
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_userForm_user." + field)).sendKeys(value);
    }
}
