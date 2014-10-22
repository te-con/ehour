package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static net.rrm.ehour.it.driver.ProjectDriver.ItProject;
import static net.rrm.ehour.it.driver.UserManagementDriver.ItUser;

public abstract class AssignmentAdminDriver {
    public static void assignToProjects(ItUser user, int... indices) {
        navigateToAssignmentAdmin();

        filterAssignmentUsers(user.name);

        clickFirstUser();

        WebElement element = findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_projectSelection_projectAssignment.project");

        Select select = new Select(element);

        for (int indice : indices) {
            select.selectByIndex(indice);
        }

        selectDateAndSetRate();
        submit();
    }

    public static boolean assignmentExists(int index) {
        WebElement element = findElement(String.format("assignmentPanel_assignmentList_border_border__body_assignments_%d_itemLink", index));

        return element != null;
    }

    private static void clickFirstUser() {
        findElement("entrySelectorFrame_entrySelectorFrame__body_userSelector_entrySelectorFrame_blueBorder_blueBorder__body_listScroll_itemList_0").click();
    }

    public static void assignToProject(ItUser user, ItProject project) {
        navigateToAssignmentAdmin();

        filterAssignmentUsers(user.name);

        clickFirstUser();

        WebElement projectElement = Driver.findElement(WicketBy.wicketPath("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_projectSelection_projectAssignment.project"));
        Select select = new Select(projectElement);
        select.selectByVisibleText(project.code + " - " + project.name);
        selectDateAndSetRate();
        submit();
    }

    private static void submit() {
        findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_submitButton").click();
    }

    private static void selectDateAndSetRate() {
        new Select(findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_assignmentType_projectAssignment.assignmentType")).selectByVisibleText("Date range");
        findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_rateRole_projectAssignment.hourlyRate").clear();
        findElement("assignmentPanel_assignmentTabs_panel_border_greySquaredFrame_border__body_assignmentForm_formComponents_rateRole_projectAssignment.hourlyRate").sendKeys("120");
    }

    public static void navigateToAssignmentAdmin() {
        Driver.get(BASE_URL + "/eh/admin/assignment");
    }

    public static void filterAssignmentUsers(String filterFor) {
        WebElement listFilter = findElement(By.id("listFilter"));
        listFilter.clear();
        listFilter.sendKeys(filterFor);
    }
}
