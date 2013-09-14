package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.driver.EhourApplicationDriver;
import org.junit.Test;

public class LoginScenario extends AbstractScenario {
    @Test
    public void shouldLogin() throws Exception {
        EhourApplicationDriver.loginAdmin();
    }
}
