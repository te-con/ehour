package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import javax.annotation.Nullable;

import static net.rrm.ehour.it.AbstractScenario.Driver;

public abstract class CommonExpectedConditions {
    public static ExpectedCondition<Boolean> expectClearedDropdown(final String selectWicketPath) {
        return new ExpectedCondition<Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable WebDriver driver) {
                try {
                    WebElement selectElement = Driver.findElement(WicketBy.wicketPath(selectWicketPath));
                    final Select element = new Select(selectElement);
                    return element.getAllSelectedOptions().isEmpty();
                } catch (Exception e) {
                    return false;
                }
            }
        };
    }
}
