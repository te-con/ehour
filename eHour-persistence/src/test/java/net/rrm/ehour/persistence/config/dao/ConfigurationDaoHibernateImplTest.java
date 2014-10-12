package net.rrm.ehour.persistence.config.dao;

import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 12:08:04 AM
 */
public class ConfigurationDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Test
    public void shouldSaveNewConfig() {
        Configuration config = new Configuration();

        config.setConfigKey("test");
        config.setConfigValue("tester");

        dao.persist(config);

        Configuration configuration = dao.findById("test");
        assertNotNull(configuration);
    }

    @Autowired
    private ConfigurationDao dao;
}
