package net.rrm.ehour.it.driver;

import net.rrm.ehour.it.WicketBy;
import org.openqa.selenium.WebElement;

import static net.rrm.ehour.it.AbstractScenario.Driver;

public abstract class ItUtil {
    public static WebElement findElement(String path) {
        return Driver.findElement(WicketBy.wicketPath(path));
    }
}
