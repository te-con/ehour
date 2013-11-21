package net.rrm.ehour.it.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static org.junit.Assert.assertEquals;


public abstract class ReportDriver {
    public static void loadReportSection() {
        Driver.get(BASE_URL + "/eh/report");
    }

    public static void assertCriteriaLoaded() {
        assertEquals("Report criteria", findElement("reportContainer_tabs-container_tabs_0_link_title").getText());
    }

    public static int countShownCustomers() {
        Select customers = new Select(findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_reportCriteria.userSelectedCriteria.customers"));

        return customers.getOptions().size();
    }

    public static void toggleActiveProjects() {
        WebElement check = findElement("reportContainer_panel_border_greySquaredFrame_border__body_criteriaForm_customerProjectsBorder_customerProjectsBorder__body_reportCriteria.userSelectedCriteria.onlyActiveProjects");

        check.click();
    }

    public static void toggleCustomerFilters() {
        Driver.findElement(By.id("customerFold")).click();
    }

    public static void filterCustomers(String filter) {
        Driver.findElement(By.id("customerFilterInput")).sendKeys(filter);
    }
}
