package net.rrm.ehour.it;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.junit.Assert.assertTrue;

public class ScenarioHelper {
    public static void Login(RemoteWebDriver driver, String baseUrl) {
        driver.manage().deleteAllCookies();

        driver.get(baseUrl + "/eh/login");
        driver.findElement(WicketBy.wicketPath("loginform_username")).clear();
        driver.findElement(WicketBy.wicketPath("loginform_username")).sendKeys("admin");
        driver.findElement(WicketBy.wicketPath("loginform_password")).sendKeys("admin");

        driver.findElement(By.cssSelector("button.submitButton")).click();

        assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Logged in as Admin, eHour  -  log off[\\s\\S]*$"));
    }
}
