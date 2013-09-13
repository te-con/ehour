package net.rrm.ehour.persistence.config.dao

import net.rrm.ehour.domain.BinaryConfiguration
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertNotNull

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 12:20:34 AM
 */
class BinaryConfigurationDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private BinaryConfigurationDao dao;

	@Test
	void shouldSave()
	{
		BinaryConfiguration config = new BinaryConfiguration(configKey: "test")
		config.setConfigValue([0, 1, 2] as byte[])

		dao.persist config

		BinaryConfiguration configuration = dao.findById("test")
		assertNotNull(configuration)
	}
}
