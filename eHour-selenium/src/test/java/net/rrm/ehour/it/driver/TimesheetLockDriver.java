package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.joda.time.LocalDate;
import org.openqa.selenium.WebElement;

import static com.thoughtworks.selenium.SeleneseTestBase.assertEquals;
import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.ItUtil.*;
import static org.junit.Assert.assertTrue;

public class TimesheetLockDriver {
    public static void navigateToAdminLocks() {
        Driver.get(BASE_URL + "/eh/op/lock");
    }

    public static void newLock(LocalDate startDate, LocalDate endDate) {
        navigateToAdminLocks();

        WebElement startDateElement = findElement(WicketBy.wicketPath("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_startDate"));
        startDateElement.clear();
        startDateElement.sendKeys(startDate.toString("MM/dd/YY"));
        WebElement endDateElement = findElement(WicketBy.wicketPath("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_endDate"));
        endDateElement.clear();
        endDateElement.sendKeys(endDate.toString("MM/dd/YY"));

        findElement(WicketBy.wicketPath("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_submit")).click();

        assertTrue(findElement("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_serverMessage").getText().matches("^[\\s\\S]*Data saved[\\s\\S]*$"));
    }

    public static void assertServerMessage(String msg) {
        assertEquals(msg, findElement(WicketBy.wicketPath("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_serverMessage")).getText());
    }
}
