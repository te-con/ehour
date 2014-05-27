package net.rrm.ehour.it;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileOutputStream;

class ScreenshotTestRule implements MethodRule {
    private final RemoteWebDriver driver;

    public ScreenshotTestRule(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public Statement apply(final Statement statement, final FrameworkMethod frameworkMethod, final Object o) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    statement.evaluate();
                } catch (Throwable t) {
                    captureScreenshot(frameworkMethod.getName());
                    throw t;
                }
            }

            public void captureScreenshot(String fileName) {
                try {
                    new File("target/surefire-reports/").mkdirs();

                    FileOutputStream out = new FileOutputStream("target/surefire-reports/screenshot-" + fileName + ".png");
                    out.write(driver.getScreenshotAs(OutputType.BYTES));
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}