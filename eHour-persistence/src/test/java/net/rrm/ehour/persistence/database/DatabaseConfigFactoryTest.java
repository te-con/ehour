package net.rrm.ehour.persistence.database;

import net.rrm.ehour.persistence.database.ConfigurationException;
import net.rrm.ehour.persistence.database.Database;
import net.rrm.ehour.persistence.database.DatabaseConfig;
import net.rrm.ehour.persistence.database.DatabaseConfigFactory;
import org.junit.Test;

import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class DatabaseConfigFactoryTest {
    @Test
    public void should_extract_credentials_from_url() throws URISyntaxException {
        DatabaseConfigFactory factory = new DatabaseConfigFactory();
        DatabaseConfig config = factory.createDatabaseConfig("mysql",
                null,
                "mysql://user:pass@127.0.0.1:3306/ehour",
                null, null);

        assertEquals("user", config.username);
        assertEquals("pass", config.password);
        assertEquals("jdbc:mysql://127.0.0.1:3306/ehour", config.url);
    }

    @Test
    public void should_append_query_parameters() throws URISyntaxException {
        DatabaseConfigFactory factory = new DatabaseConfigFactory();
        DatabaseConfig config = factory.createDatabaseConfig("mysql",
                null,
                "mysql://user:pass@127.0.0.1:3306/ehour?ssl=true",
                null, null);

        assertEquals("jdbc:mysql://127.0.0.1:3306/ehour?ssl=true", config.url);
    }

    @Test(expected = ConfigurationException.class)
    public void should_fail_without_credentials() throws URISyntaxException {
        DatabaseConfigFactory factory = new DatabaseConfigFactory();
        factory.createDatabaseConfig("mysql",
                null,
                "mysql://27.0.0.1:3306/ehour",
                null, null);

        fail("Should have thrown ConfigurationException");
    }

    @Test
    public void should_use_default_driver_when_none_supplied() throws URISyntaxException {
        DatabaseConfigFactory factory = new DatabaseConfigFactory();
        DatabaseConfig config = factory.createDatabaseConfig("mysql",
                null,
                "mysql://user:pass@127.0.0.1:3306/ehour?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true",
                null, null);

        assertEquals(Database.MYSQL.defaultDriver, config.driver);

        assertEquals("pass", config.password);
    }
}