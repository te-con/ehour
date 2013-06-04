package net.rrm.ehour.it.scenario.admin;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.ScenarioHelper;
import net.rrm.ehour.it.WicketBy;
import org.junit.Test;

public class ConfigCustomizeScenario extends AbstractScenario {
    @Test
    public void should_upload_excel() {
        ScenarioHelper.Login(Driver, baseUrl);

        Driver.findElement(WicketBy.wicketPath("configTabs_tabs-container_tabs_3_link_title")).click();

        // Warning: verifyTextPresent may require manual changes
//            assertTrue(Driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Upload a replacement logo[\\s\\S]*$"));
    }
}
