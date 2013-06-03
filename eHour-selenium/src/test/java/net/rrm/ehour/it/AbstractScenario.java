package net.rrm.ehour.it;

import org.junit.Before;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

public abstract class AbstractScenario {
    private static boolean initialized = false;
    public static RemoteWebDriver Driver;
    protected final String baseUrl = "http://localhost:18000";

    @Before
    public void setUp() throws Exception {
        if (!initialized) {
            EhourTestApplication.start();
            Driver = new FirefoxDriver();
            Driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        Driver.quit();
                    } catch (Exception e) {

                    }
                }
            });
        }

        initialized = true;
    }

}
