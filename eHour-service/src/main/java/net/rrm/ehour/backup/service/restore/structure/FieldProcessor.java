package net.rrm.ehour.backup.service.restore.structure;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

public interface FieldProcessor {
    /**
     * Process the field by settting the @parsedColumnValue in the target object. When any @Embeddable is encountered, the instance
     * is either used from the embeddable cache or a new instance of the @Embeddable type is created and added to the cache.
     *
     * @param targetField
     * @param targetObject
     * @param embeddableCache
     * @param parsedColumnValue
     * @param <PK>
     * @param <T>
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    <PK extends Serializable, T extends DomainObject<PK, ?>> void process(Field targetField,
                                                                          T targetObject,
                                                                          Map<Class<?>, Object> embeddableCache,
                                                                          Object parsedColumnValue) throws IllegalAccessException, InstantiationException;
}