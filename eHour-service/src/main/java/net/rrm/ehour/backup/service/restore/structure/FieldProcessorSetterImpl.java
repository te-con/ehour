package net.rrm.ehour.backup.service.restore.structure;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

public class FieldProcessorSetterImpl implements FieldProcessor {
    @Override
    public <PK extends Serializable, T extends DomainObject<PK, ?>> void process(Field targetField, T targetObject, Map<Class<?>, Object> embeddables, Object parsedColumnValue) throws IllegalAccessException, InstantiationException {
        if (targetField.getType() != parsedColumnValue.getClass()) {
            Object embeddable = resolveEmbeddable(embeddables, targetField);

            targetField.set(targetObject, embeddable);
        } else {
            targetField.set(targetObject, parsedColumnValue);
        }

    }

    private Object resolveEmbeddable(Map<Class<?>, Object> embeddables, Field field) throws InstantiationException, IllegalAccessException {
        Class<?> type = field.getType();

        if (embeddables.containsKey(type)) {
            return embeddables.get(type);
        } else {
            Object embeddable = instantiateEmbeddable(field);
            embeddables.put(type, embeddable);
            return embeddable;
        }
    }

    private Object instantiateEmbeddable(Field field) throws InstantiationException, IllegalAccessException {
        return field.getDeclaringClass().newInstance();
    }
}
