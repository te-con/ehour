package net.rrm.ehour.it.scenario;

import net.rrm.ehour.it.AbstractScenario;
import net.rrm.ehour.it.WicketBy;
import net.rrm.ehour.it.driver.EhourApplicationDriver;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertTrue;

public class ChangeExcelLogoScenario extends AbstractScenario {
    @Test
    public void should_upload_excel() {
        EhourApplicationDriver.loginAdmin();

        clickCustomizeTab();

        WebElement previewButton = findPreviewButton();
        assertTrue(previewButton.getText().matches("^[\\s\\S]*Preview in excel[\\s\\S]*$"));

        Driver.findElement(By.xpath("//input[@type='file']")).sendKeys("resources/replacement_logo.png");
        Driver.findElement(WicketBy.wicketPath("configTabs_panel_border_greySquaredFrame_border__body_form_uploadSubmit")).click();
    }

    private WebElement findPreviewButton() {
        return Driver.findElement(WicketBy.wicketPath("configTabs_panel_border_greySquaredFrame_border__body_form_excelPreview"));
    }

    private void clickCustomizeTab() {
        Driver.findElement(WicketBy.wicketPath("configTabs_tabs-container_tabs_3_link_title")).click();
    }
}
