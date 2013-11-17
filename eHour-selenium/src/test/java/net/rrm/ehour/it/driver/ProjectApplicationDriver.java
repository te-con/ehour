package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class ProjectApplicationDriver {
    public static void loadProjectAdmin() {
        Driver.get(BASE_URL + "/eh/admin/project");
    }

    public static void createProject(String name, String code, String customerName, String customerCode) {
        loadProjectAdmin();

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.name")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.name")).sendKeys(name);
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.projectCode")).clear();
        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.projectCode")).sendKeys(code);
        new Select(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.customer"))).selectByVisibleText(customerCode + " - " + customerName);

        Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_submitButton")).click();

        assertTrue(Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_serverMessage")).getText().matches("^[\\s\\S]*Data saved[\\s\\S]*$"));
    }

    public static void editProject(String projectName) {
        WebElement listFilter = Driver.findElement(By.id("listFilter"));
        listFilter.clear();
        listFilter.sendKeys(projectName);

        Driver.findElement(WicketBy.wicketPath("entrySelectorFrame_entrySelectorFrame__body_projectSelector_entrySelectorFrame_blueBorder_blueBorder__body_itemListHolder_itemList_0")).click();
        EhourApplicationDriver.sleep(500);
    }

    public static void showOnlyActiveProjects() {
        Driver.findElement(WicketBy.wicketPath("entrySelectorFrame_entrySelectorFrame__body_projectSelector_entrySelectorFrame_filterForm_filterToggle")).click();
        EhourApplicationDriver.sleep(500);
    }

    public static void assertProjectLoaded(String projectName) {
        String input = Driver.findElement(WicketBy.wicketPath("tabs_panel_border_greySquaredFrame_border__body_projectForm_project.name")).getAttribute("value");

        assertEquals(projectName, input);
    }
}
