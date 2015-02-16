package net.rrm.ehour.backup.service.restore.structure;

import com.google.common.collect.Maps;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Build the FieldMap for a domain object, a map with the field names of the domain object mapped to FieldDefinitions
 */
public abstract class FieldMapFactory {
    private static final Map<Class<?>, FieldMap> FIELD_MAP_CACHE = Maps.newHashMap();

    /**
     * Iterate over the domain object and extract any JPA annotated fields
     * @param clazz
     * @param <T>
     * @return a Map with lowercase annotated field names from domain object and matching field definitions
     */
    public static <T> FieldMap buildFieldMapForEntity(Class<T> clazz) {
        if (FIELD_MAP_CACHE.containsKey(clazz)) {
            return FIELD_MAP_CACHE.get(clazz);
        }

        FieldMap fieldMap = new FieldMap();

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

        fieldMap.afterFieldsSet();

        // this code is executed serially, no multithreading to parse XML
        // so no worries about synchronization
        FIELD_MAP_CACHE.put(clazz, fieldMap);

        return fieldMap;
    }

    private static void embeddable(FieldMap fieldMap, Field field) {
        Class<?> fieldType = field.getType();

        // do we need to go a level deeper with composite primary keys marked as Embeddable?
        if (fieldType.isAnnotationPresent(Embeddable.class)) {
            fieldMap.put(field.getName(), new FieldDefinition(field));

            FieldMap embeddableFieldMap = buildFieldMapForEntity(fieldType);

            for (FieldDefinition fieldDefinition : embeddableFieldMap.fieldDefinitions()) {
                fieldDefinition.setProcessor(new FieldProcessorEmbeddableImpl());
            }

            fieldMap.merge(embeddableFieldMap);
        }
    }

    private static void column(FieldMap fieldMap, Field field) {
        Column column = field.getAnnotation(Column.class);
        String columnName = column.name();

        fieldMap.put(columnName.toLowerCase(), new FieldDefinition(field));
    }

    private static void manyToOne(FieldMap fieldMap, Field field) {
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

    private static void manyToMany(FieldMap fieldMap, Field field) {
        JoinTable joinTable = field.getAnnotation(JoinTable.class);
        String columnName = joinTable.name();

        FieldDefinition definition = new FieldDefinitionManyToMany(field);
        fieldMap.put(columnName.toLowerCase(), definition);
    }
}
