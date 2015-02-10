package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.domain.DomainObject;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:43 AM
 */
public class EntityParserDaoValidatorImpl implements EntityParserDao {
    private static final Logger LOG = Logger.getLogger(EntityParserDaoValidatorImpl.class);

    private int id;
    private Map<Class<?>, Integer> persistCount;

    public EntityParserDaoValidatorImpl() {
        initialize();
    }

    private boolean initialize() {
        this.id = 1;
        this.persistCount = new HashMap<Class<?>, Integer>();
        return true;
    }

    @Override
    public <T extends DomainObject<?, ?>> Serializable persist(T object) {
        Integer count = 0;

        if (persistCount.containsKey(object.getClass())) {
            count = persistCount.get(object.getClass());
        }

        persistCount.put(object.getClass(), count + 1);

        return Integer.toString(id++);
    }

    @Override
    public <T extends Serializable> T find(Serializable primaryKey, Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            LOG.error(e);
            return null;
        }
    }

    int getTotalPersistCount() {
        int totalCount = 0;

        for (Integer count : persistCount.values()) {
            totalCount += count;
        }

        return totalCount;
    }
}
