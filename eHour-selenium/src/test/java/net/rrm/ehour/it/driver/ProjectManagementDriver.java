package net.rrm.ehour.it.driver;

import static com.thoughtworks.selenium.SeleneseTestBase.assertEquals;
import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;

public abstract class ProjectManagementDriver {
    public static void navigateToPm() {
        Driver.get(BASE_URL + "/eh/pm");
    }

    public static void assertPmLoaded() {
        assertEquals("Project Management", Driver.getTitle());
    }
}
