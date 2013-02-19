package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.service.ParseSession;
import net.rrm.ehour.backup.service.ParserUtil;
import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.persistence.backup.dao.BackupEntityType;
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

    private static final Map<Class<?>, TypeTransformer<?>> transformerMap = new HashMap<Class<?>, TypeTransformer<?>>();
    private ParseSession status;

    private PrimaryKeyCache keyCache;

    static {
        transformerMap.put(Integer.class, new IntegerTransformer());
        transformerMap.put(Float.class, new FloatTransformer());
        transformerMap.put(Date.class, new DateTransformer());
        transformerMap.put(Boolean.class, new BooleanTransformer());
    }

    public DomainObjectParser(XMLEventReader reader, DomainObjectParserDao parserDao, PrimaryKeyCache keyCache) {
        this.parserDao = parserDao;
        this.reader = reader;
        this.keyCache = keyCache;
    }

    public <T extends DomainObject<?, ?>> List<T> parse(Class<T> clazz, ParseSession status) throws IllegalAccessException, InstantiationException, XMLStreamException {
        Map<String, FieldDefinition> fieldMap = createFieldMap(clazz);
        this.status = status;

        return parseDomainObjects(clazz, fieldMap, status);
    }

    /**
     * Parse domain object with reader pointing on the table name tag
     */
    private <T extends DomainObject<?, ?>> List<T> parseDomainObjects(Class<T> clazz, Map<String, FieldDefinition> fieldMap, ParseSession status) throws XMLStreamException, IllegalAccessException, InstantiationException {
        List<T> domainObjects = new ArrayList<T>();

        while (reader.hasNext()) {
            XMLEvent event = reader.nextTag();

            if (event.isStartElement()) {
                T domainObject = parseDomainObject(clazz, fieldMap);

                domainObjects.add(domainObject);

                status.addInsertion(BackupEntityType.forClass(clazz));
            } else if (event.isEndElement()) {
                break;
            }
        }

        return domainObjects;
    }

    @SuppressWarnings("unchecked")
    private <T extends DomainObject<?, ?>> T parseDomainObject(Class<T> clazz, Map<String, FieldDefinition> fieldMap) throws XMLStreamException, IllegalAccessException, InstantiationException {
        T domainObject = clazz.newInstance();

        Map<Class<?>, Object> embeddables = new HashMap<Class<?>, Object>();

        while (reader.hasNext()) {
            XMLEvent event = reader.nextTag();

            if (event.isEndElement()) {
                break;
            }

            StartElement startElement = event.asStartElement();
            String dbField = startElement.getName().getLocalPart();
            FieldDefinition fieldDefinition = fieldMap.get(dbField.toLowerCase());

            String data = ParserUtil.parseNextEventAsCharacters(reader);

            Field field = fieldDefinition.getField();
            Class<? extends Serializable> type = (Class<? extends Serializable>) field.getType();
            Object parsedValue = parseValue(type, data, fieldDefinition.isIgnorable());

            if (parsedValue != null) {
                // check whether the field is part of a composite pk (ie. a different class)
                if (field.getDeclaringClass() != domainObject.getClass()) {
                    Object embeddable = resolveEmbeddable(embeddables, field);

                    field.set(embeddable, parsedValue);
                } else {
                    field.set(domainObject, parsedValue);
                }
            }
        }

        boolean hasCompositeKey = setEmbeddablesInDomainObject(fieldMap, domainObject, embeddables);

        Serializable originalKey = domainObject.getPK();

        resetId(fieldMap, domainObject);

        Serializable primaryKey = parserDao.persist(domainObject);

        if (!hasCompositeKey) {
            keyCache.putKey(domainObject.getClass(), originalKey, primaryKey);
        }

        return domainObject;
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
    private Serializable parseValue(Class<? extends Serializable> type, String value, boolean canBeIgnored)
            throws IllegalAccessException, InstantiationException {
        Serializable parsedValue = null;

        if (type.isAnnotationPresent(Entity.class)) {
            Serializable castToFk = castToFkType(type, value);
            Serializable persistedKey = keyCache.getKey(type, castToFk);

            if (persistedKey != null) {
                parsedValue = parserDao.find(persistedKey, type);
            }

            if (parsedValue == null && !canBeIgnored) {
                status.addError(BackupEntityType.forClass(type), "ManyToOne relation not resolved");
            }
        } else if (type == String.class) {
            parsedValue = value;
        } else if (type.isEnum()) {
            parsedValue = Enum.valueOf((Class<Enum>) type, value);
        } else {
            if (transformerMap.containsKey(type)) {
                parsedValue = transformerMap.get(type).transform(value);
            } else {
                status.addError(BackupEntityType.forClass(type), "unknown type: " + type);
                LOG.error("no transformer for type " + type);
            }
        }
        return parsedValue;
    }

    @SuppressWarnings("unchecked")
    private Serializable castToFkType(Class<?> fkObjectType, String value) throws InstantiationException, IllegalAccessException {
        Field[] fields = fkObjectType.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return parseValue((Class<? extends Serializable>) field.getType(), value, false);
            }
        }

        return value;
    }

    private <T> Map<String, FieldDefinition> createFieldMap(Class<T> clazz) {
        Map<String, FieldDefinition> fieldMap = new HashMap<String, FieldDefinition>();

        Field[] fields = clazz.getDeclaredFields();
        int seq = 0;

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();

                fieldMap.put(columnName.toLowerCase(), new FieldDefinition(field));
            } else if (field.isAnnotationPresent(JoinColumn.class)) {
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
            } else {
                Class<?> fieldType = field.getType();

                // do we need to go a level deeper with composite primary keys marked as Embeddable?
                if (fieldType.isAnnotationPresent(Embeddable.class)) {
                    fieldMap.put(Integer.toString(seq++), new FieldDefinition(field));

                    Map<String, FieldDefinition> embeddableFieldMap = createFieldMap(fieldType);

                    fieldMap.putAll(embeddableFieldMap);
                }
            }
        }

        return fieldMap;
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
