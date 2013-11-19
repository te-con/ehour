package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static org.junit.Assert.assertEquals;


public abstract class ReportApplicationDriver {
    public static void loadReport() {
        Driver.get(BASE_URL + "/eh/report");
    }

    public static void assertCriteriaLoaded() {
        assertEquals("Report criteria", Driver.findElement(WicketBy.wicketPath("reportContainer_tabs-container_tabs_0_link_title")).getText());
    }
}
