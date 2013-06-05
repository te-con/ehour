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
