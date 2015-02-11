package net.rrm.ehour.backup.service.restore.structure;

import com.google.common.collect.Maps;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class FieldMap {
    private Map<String, FieldDefinition> fields;

    private FieldDefinition generatedId;
    private FieldDefinition id;
    private boolean compositeId;

    public FieldMap() {
        fields = Maps.newHashMap();
    }

    void afterFieldsSet() {
        for (FieldDefinition fieldDefinition : fieldDefinitions()) {
            Field field = fieldDefinition.getField();

            if (field.isAnnotationPresent(Id.class)) {
                id = fieldDefinition;

                if (field.isAnnotationPresent(GeneratedValue.class)) {
                    generatedId = fieldDefinition;
                } else {
                    compositeId = field.getType().isAnnotationPresent(Embeddable.class);
                }

                break;
            }
        }
    }

    void put(String field, FieldDefinition fieldDefinition) {
        fields.put(field, fieldDefinition);
    }

    void merge(FieldMap other) {
        fields.putAll(other.fields);
    }

    public Collection<FieldDefinition> fieldDefinitions() {
        return fields.values();
    }

    public FieldDefinition get(String field) {
        return fields.get(field);
    }

    public FieldDefinition getGeneratedId() {
        return generatedId;
    }

    public FieldDefinition getId() {
        return id;
    }

    public boolean isCompositeId() {
        return compositeId;
    }
}
