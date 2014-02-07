package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.openqa.selenium.By;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.UserManagementDriver.*;
import static org.junit.Assert.assertTrue;

public class EhourApplicationDriver {
    public static void loginAdmin() {
        login(new ItUser("admin", "admin"));
    }

    public static void loginRegularUser() {
        login(REGULAR_USER);
    }

    public static void loginReportUser() {
        login(REPORTING_USER);
    }

    public static void login(ItUser user) {
        Driver.manage().deleteAllCookies();

        Driver.get(BASE_URL + "/eh/login");
        Driver.findElement(WicketBy.wicketPath("loginform_username")).clear();
        Driver.findElement(WicketBy.wicketPath("loginform_username")).sendKeys(user.name);
        Driver.findElement(WicketBy.wicketPath("loginform_password")).sendKeys(user.password);
        Driver.findElement(By.cssSelector("button.submitButton")).click();

        assertTrue(Driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Logged in as[\\s\\S]*$"));
    }

    public static void logout() {
        Driver.get(BASE_URL + "/eh/logout");
        Driver.manage().deleteAllCookies();
    }

    public static void sleepFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep() {
        sleepFor(1250);
    }
}
