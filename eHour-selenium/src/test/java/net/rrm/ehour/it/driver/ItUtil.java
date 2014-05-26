package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static net.rrm.ehour.it.AbstractScenario.Driver;

public abstract class ItUtil {
    public static WebElement findElement(String path) {
        return findElement(WicketBy.wicketPath(path));
    }

    public static WebElement findElement(By by) {
        (new WebDriverWait(Driver, 30)).until(ExpectedConditions.presenceOfElementLocated(by));

        return Driver.findElement(by);
    }

    public static void waitForValue(String path, String expectedValue) {
        waitForValue(WicketBy.wicketPath(path), expectedValue);
    }

    public static void waitForValue(By by, String expectedValue) {
        (new WebDriverWait(Driver, 30)).until(ExpectedConditions.textToBePresentInElementValue(by, expectedValue));
    }

}
