package net.rrm.ehour.persistence.appconfig;

import net.rrm.ehour.persistence.datasource.DerbyDataSourceFactory;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 4:26:50 PM
 */
public class HibernateConfigurationTest {
    private DataSource dataSource;

    @Before
    public void setUp() throws IOException {
        dataSource = DerbyDataSourceFactory.createDataSource("memory:db");
    }

    @Test
    public void createSessionFactoryForDerby() throws Exception {
        SessionFactory db = createSessionFactoryForDb("derby");
        assertNotNull(db);
        db.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateSessionFactoryForInvalidDatabase() throws Exception {
        assertNotNull(createSessionFactoryForDb("unknown"));
        fail();
    }

    private SessionFactory createSessionFactoryForDb(String db) throws Exception {
        HibernateConfiguration configuration = new HibernateConfiguration(dataSource, db, "true");

        return configuration.getSessionFactory();
    }
}
