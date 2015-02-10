package net.rrm.ehour.backup.service.restore.structure;

import com.google.common.collect.Maps;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Build the FieldMap for a domain object, a map with the field names of the domain object mapped to FieldDefinitions
 */
public class FieldMapFactory {
    /**
     * Iterate over the domain object and extract any JPA annotated fields
     * @param clazz
     * @param <T>
     * @return a Map with lowercase annotated field names from domain object and matching field definitions
     */
    public static <T> Map<String, FieldDefinition> buildFieldMapForDomainObject(Class<T> clazz) {
        Map<String, FieldDefinition> fieldMap = new HashMap<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(Column.class)) {
                column(fieldMap, field);
            } else if (field.isAnnotationPresent(JoinColumn.class)) {
                manyToOne(fieldMap, field);
            } else if (field.isAnnotationPresent(ManyToMany.class) && field.isAnnotationPresent(JoinTable.class)) {
                manyToMany(fieldMap, field);
            } else {
                embeddable(fieldMap, field);
            }
        }

        return fieldMap;
    }

    private static void embeddable(Map<String, FieldDefinition> fieldMap, Field field) {
        Class<?> fieldType = field.getType();

        // do we need to go a level deeper with composite primary keys marked as Embeddable?
        if (fieldType.isAnnotationPresent(Embeddable.class)) {
            String prefix = fieldType.getName();
            fieldMap.put(field.getName(), new FieldDefinition(field));

            Map<String, FieldDefinition> embeddableFieldMap = buildFieldMapForDomainObject(fieldType);

            for (FieldDefinition fieldDefinition : embeddableFieldMap.values()) {
                fieldDefinition.setProcessor(new FieldProcessorEmbeddableImpl());
            }

            Map<String, FieldDefinition> prefixedMap = Maps.newHashMap();

            for (Map.Entry<String, FieldDefinition> entry : embeddableFieldMap.entrySet()) {
                prefixedMap.put(prefix + "@" + entry.getKey(), entry.getValue());
            }

            fieldMap.putAll(embeddableFieldMap);
        }
    }

    private static void column(Map<String, FieldDefinition> fieldMap, Field field) {
        Column column = field.getAnnotation(Column.class);
        String columnName = column.name();

        fieldMap.put(columnName.toLowerCase(), new FieldDefinition(field));
    }

    private static void manyToOne(Map<String, FieldDefinition> fieldMap, Field field) {
        JoinColumn column = field.getAnnotation(JoinColumn.class);
        String columnName = column.name();

        FieldDefinition definition;
        if (field.isAnnotationPresent(NotFound.class)) {
            NotFound notFoundAnnotation = field.getAnnotation(NotFound.class);
            definition = notFoundAnnotation.action() == NotFoundAction.IGNORE ? new IgnorableFieldDefinition(field) : new FieldDefinition(field);
        } else {
            definition = new FieldDefinition(field);
        }

        fieldMap.put(columnName.toLowerCase(), definition);
    }

    private static void manyToMany(Map<String, FieldDefinition> fieldMap, Field field) {
        JoinTable joinTable = field.getAnnotation(JoinTable.class);
        String columnName = joinTable.name();

        FieldDefinition definition = new FieldDefinition(field, new FieldProcessorAddImpl());
        fieldMap.put(columnName.toLowerCase(), definition);
    }
}
