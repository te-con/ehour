package net.rrm.ehour.it;

import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

public class LoginTest extends AbstractIntegrationTest {
    @Test
    public void shouldLogin() throws Exception {
        driver.get(baseUrl + "/eh/login");
        driver.findElement(WicketBy.wicketPath("loginform_username")).clear();
        driver.findElement(WicketBy.wicketPath("loginform_username")).sendKeys("admin");
        driver.findElement(WicketBy.wicketPath("loginform_password")).sendKeys("admin");

        driver.findElement(By.cssSelector("button.submitButton")).click();

        try {
            assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Logged in as Admin, eHour  -  log off[\\s\\S]*$"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }

    }
}
