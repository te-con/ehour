package net.rrm.ehour.backup.service.restore.structure;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

public class FieldDefinition {
    private Field field;
    private FieldProcessor processor;

    public FieldDefinition(Field field) {
        this(field, new FieldProcessorSetterImpl());
    }

    public FieldDefinition(Field field, FieldProcessor processor) {
        this.field = field;
        this.processor = processor;
    }

    void setProcessor(FieldProcessor processor) {
        this.processor = processor;
    }

    public <PK extends Serializable, T extends DomainObject<PK, ?>> void process(T targetObject,
                                                                          Map<Class<?>, Object> embeddableCache,
                                                                          Object parsedColumnValue) throws IllegalAccessException, InstantiationException {
        processor.process(field, targetObject, embeddableCache, parsedColumnValue);
    }

    public Field getField() {
        return field;
    }

    public boolean isIgnorable() {
        return false;
    }
}
