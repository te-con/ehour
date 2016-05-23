package net.rrm.ehour.it;

import net.rrm.ehour.persistence.database.SpringContext;
import net.rrm.ehour.persistence.hibernate.HibernateCache;
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public abstract class AbstractScenario {
    public static final String BASE_URL = "http://localhost:18000";

    public static RemoteWebDriver Driver;
    public static DataSource dataSource;

    private static boolean initialized = false;

    @Rule
    public ScreenshotTestRule screenshotTestRule;
    private static SessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {
        if (!initialized) {
            createInMemoryDb();

            EhourTestApplication.start();
            sessionFactory = SpringContext.getApplicationContext().getBean(SessionFactory.class);

            dataSource = SessionFactoryUtils.getDataSource(sessionFactory);
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

    private void createInMemoryDb() throws SQLException {
        EmbeddedConnectionPoolDataSource csDataSource = new EmbeddedConnectionPoolDataSource();
        csDataSource.setDatabaseName("memory:ehourDb;create=true");
        csDataSource.getPooledConnection().close();
    }

    protected void clearDatabase() throws SQLException {
        DatabaseTruncater.truncate(dataSource);
        HibernateCache.clearHibernateCache(sessionFactory);
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

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @After
    public void quitBrowser() {
        if (Driver != null) {
            try {
                if (isTruncateBetweenTests())
                    clearDatabase();
                Driver.quit();
            } catch (Exception e) {
                //
            }
        }
    }

    protected boolean isTruncateBetweenTests() {
        return true;
    }
}
