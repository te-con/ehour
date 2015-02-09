package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.domain.ParserUtil;
import net.rrm.ehour.backup.service.backup.BackupEntity;
import net.rrm.ehour.backup.service.backup.BackupEntityLocator;
import net.rrm.ehour.domain.DomainObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Not thread safe
 *
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 11:18:59 PM
 */
public class DomainObjectParser {
    private DomainObjectParserDao parserDao;

    private XMLEventReader reader;

    private static final Logger LOG = Logger.getLogger(DomainObjectParser.class);

    private static final Map<Class<?>, TypeTransformer<?>> transformerMap = new HashMap<>();
    private ParseSession status;

    private PrimaryKeyCache keyCache;
    private final BackupEntityLocator entityLocator;

    static {
        transformerMap.put(Integer.class, new IntegerTransformer());
        transformerMap.put(Float.class, new FloatTransformer());
        transformerMap.put(Date.class, new DateTransformer());
        transformerMap.put(Boolean.class, new BooleanTransformer());
    }

    public DomainObjectParser(XMLEventReader reader, DomainObjectParserDao parserDao, PrimaryKeyCache keyCache, BackupEntityLocator entityLocator) {
        this.parserDao = parserDao;
        this.reader = reader;
        this.keyCache = keyCache;
        this.entityLocator = entityLocator;
    }

    public <PK extends Serializable, T extends DomainObject<PK, ?>>  List<T> parse(Class<T> clazz, ParseSession status) throws IllegalAccessException, InstantiationException, XMLStreamException {
        Map<String, FieldDefinition> fieldMap = extractFieldMapFromDomainObject(clazz);
        this.status = status;

        return parseDomainObjects(clazz, fieldMap, status);
    }

    /**
     * Parse domain object with reader pointing on the table name tag
     */
    private <PK extends Serializable, T extends DomainObject<PK, ?>>  List<T> parseDomainObjects(Class<T> clazz, Map<String, FieldDefinition> fieldMap, ParseSession status) throws XMLStreamException, IllegalAccessException, InstantiationException {
        List<T> domainObjects = new ArrayList<>();

        while (reader.hasNext()) {
            XMLEvent event = reader.nextTag();

            if (event.isStartElement()) {
                T domainObject = parseAndPersistDomainObject(clazz, fieldMap);

                domainObjects.add(domainObject);

                BackupEntity backupEntity = entityLocator.forClass(clazz);
                status.addInsertion(backupEntity);
            } else if (event.isEndElement()) {
                break;
            }
        }

        return domainObjects;
    }

    @SuppressWarnings("unchecked")
    private <PK extends Serializable, T extends DomainObject<PK, ?>> T parseAndPersistDomainObject(Class<T> clazz, Map<String, FieldDefinition> fieldMap) throws XMLStreamException, IllegalAccessException, InstantiationException {
        T targetObject = clazz.newInstance();

        Map<Class<?>, Object> embeddables = new HashMap<>();

        while (reader.hasNext()) {
            XMLEvent event = reader.nextTag();

            if (event.isEndElement()) {
                break;
            }

            StartElement startElement = event.asStartElement();
            String dbField = startElement.getName().getLocalPart();
            FieldDefinition fieldDefinition = fieldMap.get(dbField.toLowerCase());

            Field targetField = fieldDefinition.getField();
            Class<? extends Serializable> type = (Class<? extends Serializable>) targetField.getType();

            String columnValue = ParserUtil.parseNextEventAsCharacters(reader);
            Object parsedColumnValue = parseColumn(type, columnValue, fieldDefinition.isIgnorable());

            if (parsedColumnValue != null) {
                // check whether the field is part of a composite pk (ie. a different class)
                if (targetField.getDeclaringClass() != targetObject.getClass()) {
                    Object embeddable = resolveEmbeddable(embeddables, targetField);

                    targetField.set(embeddable, parsedColumnValue);
                } else {
                    targetField.set(targetObject, parsedColumnValue);
                }
            }
        }

        boolean hasCompositeKey = setEmbeddablesInDomainObject(fieldMap, targetObject, embeddables);

        PK originalKey = targetObject.getPK();

        resetId(fieldMap, targetObject);

        Serializable primaryKey = parserDao.persist(targetObject);

        if (!hasCompositeKey) {
            keyCache.putKey(targetObject.getClass(), originalKey, primaryKey);
        }

        return targetObject;
    }

    private Object resolveEmbeddable(Map<Class<?>, Object> embeddables, Field field)
            throws InstantiationException, IllegalAccessException {
        Object embeddable;

        if (!embeddables.containsKey(field.getDeclaringClass())) {
            embeddable = field.getDeclaringClass().newInstance();
            embeddables.put(field.getDeclaringClass(), embeddable);
        } else {
            embeddable = embeddables.get(field.getDeclaringClass());
        }
        return embeddable;
    }

    private <T> void resetId(Map<String, FieldDefinition> fieldMap, T domainObject) throws IllegalAccessException {
        for (FieldDefinition fieldDefinition : fieldMap.values()) {
            Field field = fieldDefinition.getField();
            if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(GeneratedValue.class)) {
                field.set(domainObject, null);
            }
        }
    }

    private <T> boolean setEmbeddablesInDomainObject(Map<String, FieldDefinition> fieldMap, T domainObject, Map<Class<?>, Object> embeddables)
            throws IllegalAccessException {
        boolean hasCompositeKey = false;

        for (FieldDefinition fieldDefinition : fieldMap.values()) {
            Field field = fieldDefinition.getField();
            Class<?> fieldType = field.getType();

            if (fieldType.isAnnotationPresent(Embeddable.class)) {
                field.set(domainObject, embeddables.get(fieldType));

                hasCompositeKey = true;
            }
        }

        return hasCompositeKey;
    }

    @SuppressWarnings("unchecked")
    private Serializable parseColumn(Class<? extends Serializable> columnType, String value, boolean canBeIgnored)
            throws IllegalAccessException, InstantiationException {
        Serializable parsedValue = null;

        if (columnType.isAnnotationPresent(Entity.class)) {
            Serializable castToFk = castToFkType(columnType, value);
            Serializable persistedKey = keyCache.getKey(columnType, castToFk);

            if (persistedKey != null) {
                parsedValue = parserDao.find(persistedKey, columnType);
            }

            if (parsedValue == null && !canBeIgnored) {
                status.addError(entityLocator.forClass(columnType), "ManyToOne relation not resolved");
            }
        } else if (columnType == String.class) {
            parsedValue = value;
        } else if (columnType.isEnum()) {
            parsedValue = Enum.valueOf((Class<Enum>) columnType, value);
        } else {
            if (transformerMap.containsKey(columnType)) {
                parsedValue = transformerMap.get(columnType).transform(value);
            } else {
                status.addError(entityLocator.forClass(columnType), "unknown type: " + columnType);
                LOG.error("no transformer for type " + columnType);
            }
        }
        return parsedValue;
    }

    @SuppressWarnings("unchecked")
    private Serializable castToFkType(Class<?> fkObjectType, String value) throws InstantiationException, IllegalAccessException {
        Field[] fields = fkObjectType.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return parseColumn((Class<? extends Serializable>) field.getType(), value, false);
            }
        }

        return value;
    }

    /**
     * Iterate over the domain object and extract any JPA annotated fields
     * @param clazz
     * @param <T>
     * @return a Map with lowercase annotated field names from domain object and matching
     */
    private <T> Map<String, FieldDefinition> extractFieldMapFromDomainObject(Class<T> clazz) {
        Map<String, FieldDefinition> fieldMap = new HashMap<>();

        Field[] fields = clazz.getDeclaredFields();
        int seq = 0;

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(Column.class)) {
                parseColumn(fieldMap, field);
            } else if (field.isAnnotationPresent(JoinColumn.class)) {
                parseManyToOne(fieldMap, field);
            } else if (field.isAnnotationPresent(ManyToMany.class)) {

            } else {
                Class<?> fieldType = field.getType();

                // do we need to go a level deeper with composite primary keys marked as Embeddable?
                if (fieldType.isAnnotationPresent(Embeddable.class)) {
                    fieldMap.put(Integer.toString(seq++), new FieldDefinition(field));

                    Map<String, FieldDefinition> embeddableFieldMap = extractFieldMapFromDomainObject(fieldType);

                    fieldMap.putAll(embeddableFieldMap);
                }
            }
        }

        return fieldMap;
    }

    private void parseColumn(Map<String, FieldDefinition> fieldMap, Field field) {
        Column column = field.getAnnotation(Column.class);
        String columnName = column.name();

        fieldMap.put(columnName.toLowerCase(), new FieldDefinition(field));
    }

    private void parseManyToOne(Map<String, FieldDefinition> fieldMap, Field field) {
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

    private void parseManyToMany(Map<String, FieldDefinition> fieldMap, Field field) {
//        JoinTable joinTable = field.getAnnotation(JoinTable.class);
//        String joinTableName = joinTable.name();
//
//        BackupEntity backupEntity = entityLocator.forName(joinTableName);
//
//
//
////        String columnName = column.name();
//
//        FieldDefinition definition;
//        if (field.isAnnotationPresent(NotFound.class)) {
//            NotFound notFoundAnnotation = field.getAnnotation(NotFound.class);
//            definition = notFoundAnnotation.action() == NotFoundAction.IGNORE ? new IgnorableFieldDefinition(field) : new FieldDefinition(field);
//        } else {
//            definition = new FieldDefinition(field);
//        }
//
//        fieldMap.put(columnName.toLowerCase(), definition);
    }

    PrimaryKeyCache getKeyCache() {
        return keyCache;
    }

    private interface TypeTransformer<T extends Serializable> {
        T transform(String value);
    }

    private static class IntegerTransformer implements TypeTransformer<Integer> {
        @Override
        public Integer transform(String value) {
            return StringUtils.isNotBlank(value) ? Integer.parseInt(value) : null;
        }
    }

    private static class BooleanTransformer implements TypeTransformer<Boolean> {
        @Override
        public Boolean transform(String value) {
            return "y".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "1".equals(value);
        }
    }

    private static class FloatTransformer implements TypeTransformer<Float> {
        @Override
        public Float transform(String value) {
            return StringUtils.isNotBlank(value) ? Float.parseFloat(value) : null;
        }
    }

    private static class DateTransformer implements TypeTransformer<Date> {
        @Override
        public Date transform(String value) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(value);
            } catch (ParseException e) {
                LOG.error("Failed to parse date: " + value);
                return null;
            }
        }
    }
}
