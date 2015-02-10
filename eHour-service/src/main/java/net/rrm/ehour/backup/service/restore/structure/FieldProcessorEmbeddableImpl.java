package net.rrm.ehour.backup.service.restore.structure;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

public class FieldProcessorEmbeddableImpl  implements FieldProcessor {
    @SuppressWarnings("unchecked")
    @Override
    public <PK extends Serializable, T extends DomainObject<PK, ?>> void process(Field targetField, T targetObject, Map<Class<?>, Object> embeddables, Object parsedColumnValue) throws IllegalAccessException, InstantiationException {
        Class<?> type = targetField.getDeclaringClass();

        Object embeddable;

        if (embeddables.containsKey(type)) {
            embeddable = embeddables.get(type);
        } else {
            embeddable = type.newInstance();
            embeddables.put(type, embeddable);
        }

        targetField.set(embeddable, parsedColumnValue);
    }
}
