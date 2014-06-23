package net.rrm.ehour.persistence.appconfig

import net.rrm.ehour.persistence.datasource.DerbyDataSourceFactory
import org.hibernate.SessionFactory
import org.junit.Before
import org.junit.Test

import javax.sql.DataSource

import static org.junit.Assert.assertNotNull

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 4:26:50 PM
 */

class HibernateConfigurationTest {
    private DataSource dataSource

    @Before
    public void setUp() {
        def sourceFactory = new DerbyDataSourceFactory()

        dataSource = sourceFactory.createDataSource("memory:db")
    }

    @Test
    void createSessionFactoryForDerby() throws Exception {
        def db = createSessionFactoryForDb("derby")
        assertNotNull(db);
        db.close()
    }

    @Test(expected = IllegalArgumentException)
    void shouldNotCreateSessionFactoryForInvalidDatabase() throws Exception {
        assertNotNull(createSessionFactoryForDb("unknown"));
        fail()
    }

    private SessionFactory createSessionFactoryForDb(String db) {
        HibernateConfiguration configuration = new HibernateConfiguration(databaseName: db, dataSource: dataSource, caching: "true")

        return configuration.getSessionFactory()
    }
}
