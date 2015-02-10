package net.rrm.ehour.backup.service.restore.structure;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class FieldProcessorManyToManyImpl implements FieldProcessor {
    @SuppressWarnings("unchecked")
    @Override
    public <PK extends Serializable, T extends DomainObject<PK, ?>> void process(Field targetField, T targetObject, Map<Class<?>, Object> embeddables, Object parsedColumnValue) throws IllegalAccessException, InstantiationException {
        Collection o = (Collection)targetField.get(targetObject);
        o.add(parsedColumnValue);
    }
}
