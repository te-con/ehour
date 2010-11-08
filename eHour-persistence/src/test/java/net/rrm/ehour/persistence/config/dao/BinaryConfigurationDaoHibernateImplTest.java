package net.rrm.ehour.persistence.config.dao;

import static org.junit.Assert.assertNotNull;
import net.rrm.ehour.domain.BinaryConfiguration;
import net.rrm.ehour.persistence.config.dao.BinaryConfigurationDao;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class BinaryConfigurationDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private BinaryConfigurationDao dao;
	
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
