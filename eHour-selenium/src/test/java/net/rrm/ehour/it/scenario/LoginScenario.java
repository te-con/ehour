package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.WicketBy;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

public class LoginScenario extends AbstractScenario {
    @Test
    public void shouldLogin() throws Exception {
        Driver.get(baseUrl + "/eh/login");
        Driver.findElement(WicketBy.wicketPath("loginform_username")).clear();
        Driver.findElement(WicketBy.wicketPath("loginform_username")).sendKeys("admin");
        Driver.findElement(WicketBy.wicketPath("loginform_password")).sendKeys("admin");

        Driver.findElement(By.cssSelector("button.submitButton")).click();

        assertTrue(Driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Logged in as Admin, eHour  -  log off[\\s\\S]*$"));
    }
}
