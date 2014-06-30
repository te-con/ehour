package net.rrm.ehour.persistence.config.dao

import net.rrm.ehour.domain.Configuration
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertNotNull

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 12:08:04 AM
 */
class ConfigurationDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private ConfigurationDao dao;

    @Test
    void shouldSaveNewConfig() {
        Configuration config = new Configuration(configKey: "test", configValue: "tester")

        dao.persist config

        def configuration = dao.findById("test")
        assertNotNull(configuration)
    }
}
