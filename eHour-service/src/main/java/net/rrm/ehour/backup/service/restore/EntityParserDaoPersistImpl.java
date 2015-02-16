package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.persistence.backup.dao.RestoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/6/10 - 4:05 PM
 */
@Service("domainObjectParserDao")
public class EntityParserDaoPersistImpl implements EntityParserDao {
    @Autowired
    private RestoreDao restoreDao;

    @Override
    public <T extends DomainObject<?, ?>> Serializable persist(T object) {
        return restoreDao.persist(object);
    }

    @Override
    public <T extends Serializable> T find(Serializable primaryKey, Class<T> type) {
        return restoreDao.find(primaryKey, type);
    }
}
