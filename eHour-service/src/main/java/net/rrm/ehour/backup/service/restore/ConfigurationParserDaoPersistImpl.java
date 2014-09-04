package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:32 AM
 */
@Service("configurationParserDao")
public class ConfigurationParserDaoPersistImpl implements ConfigurationParserDao
{
    @Autowired
    private ConfigurationDao configurationDao;

    @Override
    public void persist(Configuration config)
    {
        configurationDao.merge(config);
    }
}
