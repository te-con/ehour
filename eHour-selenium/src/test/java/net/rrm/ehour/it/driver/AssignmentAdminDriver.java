package net.rrm.ehour.it.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.CustomerManagementDriver.ItCustomer;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.sleep;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static net.rrm.ehour.it.driver.ProjectDriver.ItProject;
import static net.rrm.ehour.it.driver.UserManagementDriver.ItUser;

public abstract class AssignmentAdminDriver {
    public static void assignToProject(ItUser user, ItCustomer customer, ItProject project) {
        Driver.get(BASE_URL + "/eh/admin/assignment");

        filterAssignmentUsers(user.name);

        findElement("entrySelectorFrame_entrySelectorFrame__body_userSelector_entrySelectorFrame_blueBorder_blueBorder__body_itemListHolder_itemList_0").click();
        sleep(500);

        WebElement cust = findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_projectSelection_customer");
        new Select(cust).selectByVisibleText(customer.code + " - " + customer.name);

        sleep(500);

        WebElement projectElement = findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_projectSelection_projectAssignment.project");
        new Select(projectElement).selectByVisibleText(project.code + " - " + project.name);
        new Select(findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_assignmentType_projectAssignment.assignmentType")).selectByVisibleText("Date range");
        findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_rateRole_projectAssignment.hourlyRate").clear();
        findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_rateRole_projectAssignment.hourlyRate").sendKeys("120");
        findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_submitButton").click();
    }

    public static void filterAssignmentUsers(String filterFor) {
        WebElement listFilter = Driver.findElement(By.id("listFilter"));
        listFilter.clear();
        listFilter.sendKeys(filterFor);
    }
}
