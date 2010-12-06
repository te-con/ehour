package net.rrm.ehour.export.service.importer;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.persistence.export.dao.ImportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/6/10 - 4:05 PM
 */
@Component("domainObjectParserDao")
public class DomainObjectParserDaoPersistImpl implements DomainObjectParserDao
{
    @Autowired
    private ImportDao importDao;

    @Override
    public <T extends DomainObject<?, ?>> Serializable persist(T object)
    {
        return importDao.persist(object);
    }

    @Override
    public <T extends Serializable> T find(Serializable primaryKey, Class<T> type)
    {
        return importDao.find(primaryKey, type);
    }
}
