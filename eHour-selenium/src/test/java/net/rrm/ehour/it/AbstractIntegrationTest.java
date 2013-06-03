package net.rrm.ehour.it;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public abstract class AbstractIntegrationTest {
    WebDriver driver;
    String baseUrl;
    boolean acceptNextAlert = true;
    StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        startServer();
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:18000";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();

        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    void startServer() throws Exception {
        EhourTestApplication.start();
    }

}
