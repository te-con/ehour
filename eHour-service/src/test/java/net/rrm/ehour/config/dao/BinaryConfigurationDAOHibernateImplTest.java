package net.rrm.ehour.config.dao;

import static org.junit.Assert.assertNotNull;
import net.rrm.ehour.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.domain.BinaryConfiguration;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class BinaryConfigurationDAOHibernateImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private BinaryConfigurationDAO dao;
	
	@Test
	public void shouldSave()
	{
		BinaryConfiguration config = new BinaryConfiguration();
		config.setConfigKey("test");
		config.setConfigValue(new byte[]{0, 1, 2});
		
		dao.persist(config);

		BinaryConfiguration configuration = dao.findById("test");
		assertNotNull(configuration);
	}
}
