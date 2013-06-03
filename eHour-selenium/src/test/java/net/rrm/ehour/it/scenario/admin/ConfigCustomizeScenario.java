package net.rrm.ehour.it.scenario.admin;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.ScenarioHelper;
import org.junit.Test;

public class ConfigCustomizeScenario extends AbstractScenario {
    @Test
    public void should_upload_excel() {
        ScenarioHelper.Login(Driver, baseUrl);
    }
}
