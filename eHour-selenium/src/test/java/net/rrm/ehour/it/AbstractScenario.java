package net.rrm.ehour.it;

import net.rrm.ehour.EhourServer;
import net.rrm.ehour.persistence.datasource.SpringContext;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
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
            EhourTestApplication.start();
            dataSource = SpringContext.getApplicationContext().getBean(DataSource.class);
        }

        Driver = new FirefoxDriver();
        Driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        screenshotTestRule = new ScreenshotTestRule(Driver);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                quitBrowser();
            }
        });

        clearDatabase();

        initialized = true;
    }

    protected void clearDatabase() throws SQLException {
        DatabaseTruncater.truncate(dataSource);
    }

    protected final void preloadDatabase(String dataSetFileName) throws Exception {
        Connection con = DataSourceUtils.getConnection(dataSource);
        IDatabaseConnection connection = new DatabaseConnection(con);

        FlatXmlDataSet additionalDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/datasets/" + dataSetFileName));
        DatabaseOperation.INSERT.execute(connection, additionalDataSet);
    }

    protected final void updatePassword(String username, String password) throws SQLException {
        ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(1);
        int salt = (int) (Math.random() * 10000);

        String encodedPassword = passwordEncoder.encodePassword(password, salt);

        Connection connection = dataSource.getConnection();
        String sql = String.format("UPDATE USERS SET PASSWORD = '%s', SALT = %d WHERE USERNAME = '%s'", encodedPassword, salt, username);

        connection.createStatement().execute(sql);
    }

    @After
    public void quitBrowser() {
        if (Driver != null) {
            try {
                Driver.quit();
            } catch (Exception e) {
                //
            }
        }
    }
}
