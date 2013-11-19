package net.rrm.ehour.it;

import net.rrm.ehour.EhourServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

public abstract class AbstractScenario {
    public static final String BASE_URL = "http://localhost:18000";

    public static RemoteWebDriver Driver;
    public static DataSource dataSource;

    private static boolean initialized = false;

    @Rule
    public ScreenshotTestRule screenshotTestRule;

    @Before
    public void setUp() throws Exception {
        if (!initialized) {
            EhourServer ehourServer = EhourTestApplication.start();
            dataSource = ehourServer.getDataSource();
        }

        Driver = new FirefoxDriver();
        Driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        screenshotTestRule = new ScreenshotTestRule(Driver);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Driver.quit();
                } catch (Exception e) {
                    //
                }
            }
        });

        DatabaseTruncater.truncate(dataSource);

        initialized = true;
    }

    @After
    public void quitBrowser() {
        Driver.quit();
    }
}
