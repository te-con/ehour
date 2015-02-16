package net.rrm.ehour.backup.service.restore.structure;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

public class FieldProcessorColumnImpl implements FieldProcessor {
    @Override
    public <PK extends Serializable, T extends DomainObject<PK, ?>> void process(Field targetField, T targetObject, Map<Class<?>, Object> embeddables, Object parsedColumnValue) throws IllegalAccessException, InstantiationException {
        // ignore embeddables
        if (targetField.getType() == parsedColumnValue.getClass()) {
            targetField.setAccessible(true);
            targetField.set(targetObject, parsedColumnValue);
        }
    }
}
