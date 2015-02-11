package net.rrm.ehour.backup.service.restore;

import com.google.common.collect.Maps;
import net.rrm.ehour.backup.service.restore.structure.FieldDefinition;
import net.rrm.ehour.backup.service.restore.structure.FieldMap;
import net.rrm.ehour.backup.service.restore.structure.FieldMapFactory;
import net.rrm.ehour.domain.DomainObject;
import org.apache.log4j.Logger;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:43 AM
 */
public class EntityParserDaoValidatorImpl implements EntityParserDao {
    private static final Logger LOG = Logger.getLogger(EntityParserDaoValidatorImpl.class);

    private int id;
    private Map<Class<?>, Integer> persistCount;
    private Map<Class<?>, Field> idCache;

    public EntityParserDaoValidatorImpl() {
        this.id = 1;
        this.idCache = Maps.newHashMap();
        this.persistCount = Maps.newHashMap();
    }

    @Override
    public <T extends DomainObject<?, ?>> Serializable persist(T object) {
        Class<? extends DomainObject> clazz = object.getClass();
        increasePersistenceCount(clazz);

        id++;

        FieldMap fieldMap = FieldMapFactory.buildFieldMapForEntity(clazz);
        FieldDefinition idFieldDef = fieldMap.getId();
        Field field = idFieldDef.getField();

        if (fieldMap.isCompositeId()) {
            return getCompositeId(object, field);
        }

        Class<?> fieldType = field.getType();

        if (fieldType == Integer.class) {
            return id;
        } else {
            return Integer.toString(id);
        }
    }

    private <T extends DomainObject<?, ?>> Serializable getCompositeId(T object, Field field) {
        try {
            return (Serializable)field.get(object);
        } catch (IllegalAccessException e) {
            return id;
        }
    }

    private synchronized void increasePersistenceCount(Class<? extends DomainObject> clazz) {
        Integer count = 0;

        if (persistCount.containsKey(clazz)) {
            count = persistCount.get(clazz);
        }

        persistCount.put(clazz, count + 1);
    }

    @Override
    public <T extends Serializable> T find(Serializable primaryKey, Class<T> type) {
        try {
            T t = type.newInstance();

            setPrimaryKey(primaryKey, type, t);

            return t;
        } catch (Exception e) {
            LOG.error(e);
            return null;
        }
    }

    private <T extends Serializable> void setPrimaryKey(Serializable primaryKey, Class<T> type, T t) throws IllegalAccessException {
        Field field = findPrimaryKeyField(type);

        if (field != null) {
            field.setAccessible(true);
            field.set(t, primaryKey);
        }
    }

    private <T extends Serializable> Field findPrimaryKeyField(Class<T> type) throws IllegalAccessException {
        if (idCache.containsKey(type)) {
            return idCache.get(type);
        }

        Field[] declaredFields = type.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getAnnotation(Id.class) != null) {
                idCache.put(type, declaredField);
                return declaredField;
            }
        }

        return null;
    }

    int getTotalPersistCount() {
        int totalCount = 0;

        for (Integer count : persistCount.values()) {
            totalCount += count;
        }

        return totalCount;
    }
}
