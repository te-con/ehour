package net.rrm.ehour.export.service;

import net.rrm.ehour.domain.DomainObject;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:43 AM
 */
public class DomainObjectParserDaoValidatorImpl implements DomainObjectParserDao
{
    private static final Logger LOG = Logger.getLogger(DomainObjectParserDaoValidatorImpl.class);

    private int id;
    private Map<Class<?>, Integer> persistCount;

    public DomainObjectParserDaoValidatorImpl()
    {
        initialize();
    }

    // because this class is partially mocked by Mockito in junit tests, the constructor is not called
    // this method provides a way to initialize the object
    public boolean initialize() {
        this.id = 1;
        this.persistCount = new HashMap<Class<?>, Integer>();
        return true;
    }

    @Override
    public <T extends DomainObject<?, ?>> Serializable persist(T object)
    {
        Integer count = 0;

        if (persistCount.containsKey(object.getClass()))
        {
            count = persistCount.get(object.getClass());
        }

        persistCount.put(object.getClass(), count + 1);

        return Integer.toString(id++);
    }

    @Override
    public <T> T find(Serializable primaryKey, Class<T> type)
    {
        try
        {
            return type.newInstance();
        } catch (Exception e)
        {
            LOG.error(e);
            return null;
        }
    }

    int getTotalPersistCount()
    {
        int totalCount = 0;
        System.out.println(persistCount);
        for (Integer count : persistCount.values())
        {

            totalCount += count;

        }

        return totalCount;
    }

    public Map<Class<?>, Integer> getPersistCount()
    {
        return persistCount;
    }
}
