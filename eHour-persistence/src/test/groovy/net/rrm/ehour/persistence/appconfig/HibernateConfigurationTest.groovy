package net.rrm.ehour.persistence.appconfig

import org.hibernate.SessionFactory
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import javax.sql.DataSource
import java.sql.Connection
import java.sql.DatabaseMetaData

import static org.junit.Assert.assertNotNull
import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 4:26:50 PM
 */
class HibernateConfigurationTest
{
  @Mock
  private DataSource dataSource

  @Mock
  private Connection connection

  @Mock
  private DatabaseMetaData metaData

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks this
    when(dataSource.connection).thenReturn connection
    when(connection.metaData).thenReturn metaData
  }

  @Test
  void createSessionFactoryForDerby() throws Exception
  {
    assertNotNull(createSessionFactoryForDb("derby"));
  }

  @Test(expected = IllegalArgumentException)
  void shouldNotCreateSessionFactoryForInvalidDatabase() throws Exception
  {
    assertNotNull(createSessionFactoryForDb("unknown"));
    fail()
  }

  private SessionFactory createSessionFactoryForDb(String db)
  {
    HibernateConfiguration configuration = new HibernateConfiguration(databaseName: db, dataSource: dataSource, caching: "true")

    return configuration.getSessionFactory()
  }


}
