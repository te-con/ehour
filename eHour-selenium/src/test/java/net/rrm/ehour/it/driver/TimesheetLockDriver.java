package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.joda.time.LocalDate;
import org.openqa.selenium.WebElement;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.ItUtil.*;
import static org.junit.Assert.assertEquals;

public class TimesheetLockDriver {
    private TimesheetLockDriver() {
    }

    public static void navigateToAdminLocks() {
        Driver.get(BASE_URL + "/eh/op/lock");
    }

    public static void newLock(LocalDate startDate, LocalDate endDate) {
        navigateToAdminLocks();

        setStartDate(startDate);
        setEndDate(endDate);
    }

    public static void excludeUser(int index) {
        findElement(WicketBy.wicketPath("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_excludedUsers_modify")).click();
        waitForPresence("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_excludedUsers_hide");

        findElement(WicketBy.wicketPath("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_excludedUsers_userSelect_allBorder_allBorder__body_users_" + index)).click();
        waitForPresence("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_excludedUsers_userSelect_selectedContainer_selectedUsers_0_name");
    }

    public static void editLock(int index, String name) {
        findElement(WicketBy.wicketPath("entrySelectorFrame_entrySelectorFrame__body_lockSelector_entrySelectorFrame_blueBorder_blueBorder__body_listScroll_itemList_" + index)).click();
        waitForValue("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_name", name);
    }

    public static void assertDataSaved() {
        assertServerMessage("Data saved");
    }

    public static void submit() {
        findElement(WicketBy.wicketPath("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_submit")).click();
    }

    public static void setEndDate(LocalDate endDate) {
        WebElement endDateElement = findElement(WicketBy.wicketPath("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_endDate"));
        endDateElement.clear();
        endDateElement.sendKeys(endDate.toString("MM/dd/YY"));
    }

    public static void setStartDate(LocalDate startDate) {
        WebElement startDateElement = findElement(WicketBy.wicketPath("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_startDate"));
        startDateElement.clear();
        startDateElement.sendKeys(startDate.toString("MM/dd/YY"));
    }

    public static void assertServerMessage(String msg) {
        assertEquals(msg, findElement(WicketBy.wicketPath("tabs_panel_outerBorder_greySquaredFrame_outerBorder__body_lockForm_serverMessage")).getText());
    }
}
