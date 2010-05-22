package net.rrm.ehour.config.dao;

import static org.junit.Assert.assertNotNull;
import net.rrm.ehour.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.domain.Configuration;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class ConfigurationDAOHibernateImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private ConfigurationDao dao;
	
	@Test
	public void shouldSaveNewConfig()
	{
		Configuration config = new Configuration();
		config.setConfigKey("test");
		config.setConfigValue("tester");
		
		dao.persist(config);

		Configuration configuration = dao.findById("test");
		assertNotNull(configuration);
	}
}
