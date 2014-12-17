package net.rrm.ehour.it.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.ItUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public abstract class ReportDriver {
    public static void loadReportSection() {
        Driver.get(BASE_URL + "/eh/report");
    }

    public static void assertGlobalCriteriaLoaded() {
        assertEquals("Global report", findElement("reportContainer_tabs-container_tabs_0_link_title").getText());
    }

    public static void assertUserCriteriaLoaded() {
        assertEquals("Edeling, Thies report", findElement("reportContainer_tabs-container_tabs_0_link_title").getText());
    }

    public static int countShownCustomers() {
        Select customers = new Select(findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_reportCriteria.userSelectedCriteria.customers"));
        return customers.getOptions().size();
    }

    public static int countShownProjects() {
        Select projects = new Select(findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_reportCriteria.userSelectedCriteria.projects"));
        return projects.getOptions().size();
    }

    public static void toggleActiveProjects() {
        WebElement check = findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_reportCriteria.userSelectedCriteria.onlyActiveProjects");

        check.click();

        sleep();
    }

    public static void toggleActiveCustomers() {
        WebElement check = findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_reportCriteria.userSelectedCriteria.onlyActiveCustomers");

        check.click();
    }

    public static void sortCustomersOnCode() {
        Select order = new Select(findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_customerSort"));
        order.selectByVisibleText("Code");
    }

    public static void toggleCustomerFilters() {
        findElement(By.id("customerFold")).click();
    }

    public static void filterCustomers(String filter) {
        findElement(By.id("customerFilterInput")).sendKeys(filter);
    }

    public static void toggleProjectFilters() {
        Driver.findElement(By.id("projectFold")).click();
    }

    public static void filterProjects(String filter) {
        findElement(By.id("projectFilterInput")).sendKeys(filter);
    }

    public static void sortProjectsOnCode() {
        Select order = new Select(findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_projectSort"));
        order.selectByVisibleText("Code");
    }

    public static void clearCustomerCriterium() {
        findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_clearCustomer").click();
        sleep();

        waitUntil(CommonExpectedConditions.expectClearedDropdown("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_reportCriteria.userSelectedCriteria.customers"));
    }

    public static void clearProjectCriterium() {
        findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_clearProject").click();
        sleep();

        waitUntil(CommonExpectedConditions.expectClearedDropdown("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_reportCriteria.userSelectedCriteria.projects"));
    }

    public static void createReport() {
        findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_createReport").click();
    }

    public static void setStartDate(String startDate) {
        WebElement element = findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_reportCriteria.userSelectedCriteria.reportRange.dateStart");
        element.clear();
        element.sendKeys(startDate);
    }

    public static void setEndDate(String endDate) {
        WebElement element = findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_reportCriteria.userSelectedCriteria.reportRange.dateEnd");
        element.clear();
        element.sendKeys(endDate);
    }

    public static void navigateToDetails() {
        findElement("reportContainer_tabs-container_tabs_4_link").click();

        String txt = findElement("reportContainer_panel_frame_reportTable_greyFrame_greyFrame__body_reportHeader").getText();
        assertTrue(txt.startsWith("Report period"));

        sleep();
    }

    public static void toggleOptions() {
        findElement(By.id("optionsFold")).click();
    }

    public static void clickZeroBookings() {
        findElement("reportContainer_panel_reportTable_greyFrame_greyFrame__body_reportContent_reportOptionsPlaceholder_toggleShowZeroBookings").click();
    }

}
