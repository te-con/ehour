package net.rrm.ehour.it.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.EhourApplicationDriver.sleep;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static org.junit.Assert.assertEquals;


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
        sleep(500);
    }

    public static void toggleActiveCustomers() {
        WebElement check = findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_reportCriteria.userSelectedCriteria.onlyActiveCustomers");

        check.click();
        sleep(500);
    }

    public static void sortCustomersOnCode() {
        Select order = new Select(findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_customerSort"));
        order.selectByVisibleText("Code");
        sleep(500);
    }

    public static void toggleCustomerFilters() {
        Driver.findElement(By.id("customerFold")).click();
    }

    public static void filterCustomers(String filter) {
        Driver.findElement(By.id("customerFilterInput")).sendKeys(filter);
    }

    public static void toggleProjectFilters() {
        Driver.findElement(By.id("projectFold")).click();
    }

    public static void filterProjects(String filter) {
        Driver.findElement(By.id("projectFilterInput")).sendKeys(filter);
    }

    public static void sortProjectsOnCode() {
        Select order = new Select(findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_projectSort"));
        order.selectByVisibleText("Code");
        sleep(500);
    }
}
