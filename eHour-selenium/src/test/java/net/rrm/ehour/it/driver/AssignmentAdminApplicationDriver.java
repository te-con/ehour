package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.sleep;

public abstract class AssignmentAdminApplicationDriver {
    public static void createProjectAssignment(String userName, String customerName, String customerCode, String projectName, String projectCode) {
        Driver.get(BASE_URL + "/eh/admin/assignment");

        filterAssignmentUsers(userName);

        Driver.findElement(WicketBy.wicketPath("entrySelectorFrame_entrySelectorFrame__body_userSelector_entrySelectorFrame_blueBorder_blueBorder__body_itemListHolder_itemList_0_itemLink")).click();
        sleep(500);

        WebElement cust = Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_projectSelection_customer"));
        new Select(cust).selectByVisibleText(customerCode + " - " + customerName);

        sleep(500);

        WebElement project = Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_projectSelection_projectAssignment.project"));
        new Select(project).selectByVisibleText(projectCode + " - " + projectName);
        new Select(Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_assignmentType_projectAssignment.assignmentType"))).selectByVisibleText("Date range");
        Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_rateRole_projectAssignment.hourlyRate")).clear();
        Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_rateRole_projectAssignment.hourlyRate")).sendKeys("120");
        Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_submitButton")).click();
    }

    public static void filterAssignmentUsers(String filterFor) {
        WebElement listFilter = Driver.findElement(By.id("listFilter"));
        listFilter.clear();
        listFilter.sendKeys(filterFor);
    }
}
