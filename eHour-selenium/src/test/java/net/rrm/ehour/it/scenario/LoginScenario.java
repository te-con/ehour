package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.EhourApplicationDriver;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

public class LoginScenario extends AbstractScenario {
    @Test
    public void shouldLogin() throws Exception {
        EhourApplicationDriver.Login();

        assertTrue(Driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Logged in as Admin, eHour  -  log off[\\s\\S]*$"));
    }
}
