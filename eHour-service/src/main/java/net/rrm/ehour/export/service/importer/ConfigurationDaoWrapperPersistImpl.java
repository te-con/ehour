package net.rrm.ehour.export.service.importer;

import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:32 AM
 */
@Component("configurationDaoWrapperPersist")
public class ConfigurationDaoWrapperPersistImpl implements ConfigurationDaoWrapper
{
    @Autowired
    private ConfigurationDao configurationDao;

    @Override
    public void persist(Configuration config)
    {
        configurationDao.persist(config);
    }
}
