package net.rrm.ehour.it;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

import java.util.List;

public class WicketBy extends By {

    private final String xpathExpression;
    private final String wicketPath;

    /**
     * Creates a new instance of {@link WicketBy}.
     * @param wicketPath the wicket path (eg: "id1:id2:id3")
     */
    private WicketBy(String wicketPath) {
        this.wicketPath = wicketPath;
        this.xpathExpression = convert(wicketPath);
    }

    /**
     * Factory method to create this specific By.
     * @param wicketPath the wicket path (eg: "id1:id2:id3")
     * @return By of type WicketBy
     */
    public static By wicketPath(String wicketPath) {
        return new WicketBy(wicketPath);
    }

    private String convert(String wicketPath) {
        String renderedPathVal = wicketPath.replaceAll(":", "_");
        return String.format("//*[@wicketpath='%s']", renderedPathVal);
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return ((FindsByXPath) context).findElementsByXPath(xpathExpression);
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return ((FindsByXPath) context).findElementByXPath(xpathExpression);
    }

    @Override
    public String toString() {
        return "By.wicketPath: " + wicketPath + " (xpath: " + xpathExpression + ")" ;
    }

}