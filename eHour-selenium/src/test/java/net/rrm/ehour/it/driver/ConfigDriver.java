package net.rrm.ehour.it.driver;

import org.openqa.selenium.WebElement;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static org.junit.Assert.assertEquals;

public abstract class ConfigDriver {
    public static void navigateToConfig() {
        Driver.get(BASE_URL + "/eh/admin");
    }

    public static void assertConfigLoaded() {
        assertEquals("eHour Configuration", Driver.getTitle());
    }

    public static void checkSplitAdminRole() {
        navigateToConfig();
        assertConfigLoaded();
        tickSplitAdminRole();
        submitGeneralConfigForm();
    }

    public static void tickSplitAdminRole() {
        WebElement check = findElement("configTabs_panel_border_greySquaredFrame_border__body_form_config.splitAdminRole");

        if (!check.isSelected()) {
            check.click();
        }
    }

    public static void submitGeneralConfigForm() {
        findElement("configTabs_panel_border_greySquaredFrame_border__body_form_submitButton").click();
    }
}
