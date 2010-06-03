package net.rrm.ehour.appconfig;

import static org.junit.Assert.assertNotNull;

import org.hibernate.SessionFactory;
import org.junit.Test;


public class HibernateConfigurationTest
{
	@Test
	public void createSessionFactory() throws Exception {
		HibernateConfiguration configuration = new HibernateConfiguration();
		configuration.setDatabaseName("derby");
		SessionFactory sessionFactory = configuration.getSessionFactory();
		
		assertNotNull(sessionFactory);
	
	}
}
