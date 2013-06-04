package net.rrm.ehour.it;

import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

public class EhourApplicationDriver {
    public static void Login() {
        AbstractScenario.Driver.manage().deleteAllCookies();

        AbstractScenario.Driver.get(AbstractScenario.BASE_URL + "/eh/login");
        AbstractScenario.Driver.findElement(WicketBy.wicketPath("loginform_username")).clear();
        AbstractScenario.Driver.findElement(WicketBy.wicketPath("loginform_username")).sendKeys("admin");
        AbstractScenario.Driver.findElement(WicketBy.wicketPath("loginform_password")).sendKeys("admin");
        AbstractScenario.Driver.findElement(By.cssSelector("button.submitButton")).click();

        assertTrue(AbstractScenario.Driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Logged in as Admin, eHour  -  log off[\\s\\S]*$"));
    }
}
