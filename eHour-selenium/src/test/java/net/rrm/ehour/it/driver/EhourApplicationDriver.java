package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.openqa.selenium.By;

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





    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep() {
        sleep(1250);
    }
}
