package net.rrm.ehour.persistence.config.dao;

import static org.junit.Assert.assertNotNull;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class ConfigurationDaoHibernateImplTest extends AbstractAnnotationDaoTest
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
