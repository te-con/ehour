package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.openqa.selenium.By;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static net.rrm.ehour.it.driver.UserManagementDriver.*;
import static org.junit.Assert.assertTrue;

public class EhourApplicationDriver {
    private EhourApplicationDriver() {
    }

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
        findElement(WicketBy.wicketPath("loginform_username")).clear();
        findElement(WicketBy.wicketPath("loginform_username")).sendKeys(user.name);
        findElement(WicketBy.wicketPath("loginform_password")).sendKeys(user.password);
        findElement(By.id("loginSubmit")).click();

        assertTrue(findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Signed in as[\\s\\S]*$"));
    }

    public static void logout() {
        Driver.get(BASE_URL + "/eh/logout");
        Driver.manage().deleteAllCookies();
    }

    public static void reloadPage() {
        Driver.navigate().refresh();
    }
}
