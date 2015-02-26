package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.backup.common.BackupEntityType;
import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.domain.ParserUtil;
import net.rrm.ehour.backup.service.restore.structure.FieldDefinition;
import net.rrm.ehour.backup.service.restore.structure.FieldMap;
import net.rrm.ehour.backup.service.restore.structure.FieldMapFactory;
import net.rrm.ehour.domain.DomainObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
public class EntityParser {
    private EntityParserDao parserDao;

    private XMLEventReader reader;

    private static final Logger LOG = Logger.getLogger(EntityParser.class);

    private static final Map<Class<?>, TypeTransformer<?>> transformerMap = new HashMap<>();
    private ParseSession status;

    private PrimaryKeyCache keyCache;
    private final BackupConfig backupConfig;

    static {
        transformerMap.put(Integer.class, new IntegerTransformer());
        transformerMap.put(Float.class, new FloatTransformer());
        transformerMap.put(Date.class, new DateTransformer());
        transformerMap.put(Boolean.class, new BooleanTransformer());
    }

    public EntityParser(XMLEventReader reader, EntityParserDao parserDao, PrimaryKeyCache keyCache, BackupConfig backupConfig) {
        this.parserDao = parserDao;
        this.reader = reader;
        this.keyCache = keyCache;
        this.backupConfig = backupConfig;
    }

    public <PK extends Serializable, T extends DomainObject<PK, ?>> List<T> parse(Class<T> clazz, JoinTables joinTables, ParseSession status) throws IllegalAccessException, InstantiationException, XMLStreamException, ImportException {
        FieldMap fieldMap = FieldMapFactory.buildFieldMapForEntity(clazz);
        this.status = status;

        return parseDomainObjects(clazz, fieldMap, joinTables, status);
    }

    /**
     * Parse domain object with reader pointing on the table name tag
     */
    private <PK extends Serializable, T extends DomainObject<PK, ?>> List<T> parseDomainObjects(Class<T> clazz, FieldMap fieldMap, JoinTables joinTables, ParseSession status) throws XMLStreamException, IllegalAccessException, InstantiationException, ImportException {
        List<T> domainObjects = new ArrayList<>();

        while (reader.hasNext()) {
            XMLEvent event = reader.nextTag();

            if (event.isStartElement()) {
                T domainObject = parseAndPersistDomainObject(clazz, fieldMap, joinTables);

                domainObjects.add(domainObject);

                BackupEntityType backupEntityType = backupConfig.entityForClass(clazz);
                status.addInsertion(backupEntityType);
            } else if (event.isEndElement()) {
                break;
            }
        }

        return domainObjects;
    }

    @SuppressWarnings("unchecked")
    private <PK extends Serializable, T extends DomainObject<PK, ?>> T parseAndPersistDomainObject(Class<T> clazz, FieldMap fieldMap, JoinTables joinTables) throws XMLStreamException, IllegalAccessException, InstantiationException, ImportException {
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
                fieldDefinition.process(targetObject, embeddables, parsedColumnValue);
            }
        }

        boolean hasCompositeKey = replaceEmbeddablesInEntity(fieldMap, targetObject, embeddables);

        addManyToManies(fieldMap, targetObject, joinTables);

        PK originalKey = targetObject.getPK();

        resetId(fieldMap, targetObject);

        Serializable primaryKey = parserDao.persist(targetObject);

        if (!hasCompositeKey) {
            Serializable casted = castOrLog(originalKey, primaryKey);

            keyCache.putKey(targetObject.getClass(), originalKey, casted);
        }

        return targetObject;
    }

    private <PK extends Serializable> Serializable castOrLog(PK originalKey, Serializable primaryKey) throws ImportException {
        try {
            if (primaryKey.getClass().equals(originalKey.getClass())) {
                return originalKey.getClass().cast(primaryKey);
            } else {
                LOG.error("This should only happen while running junit tests.");
                return null;
            }
        } catch (ClassCastException cce) {
            throw new ImportException("can't cast " + primaryKey.toString() + " to " + originalKey.toString()+ ": " + cce.getMessage(), cce);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void addManyToManies(FieldMap fieldMap, T targetEntity, JoinTables joinTables) throws IllegalAccessException {
        for (FieldDefinition fieldDefinition : fieldMap.fieldDefinitions()) {
            if (!fieldDefinition.isPartOfXML()) {

                // find the original ID of this entity
                FieldDefinition idFieldDef = fieldMap.getId();
                Field idField = idFieldDef.getField();
                String id = idField.get(targetEntity).toString();

                // discover the table name
                Field field = fieldDefinition.getField();
                JoinTable joinTableAnnotation = field.getAnnotation(JoinTable.class);
                String tableName = joinTableAnnotation.name().toLowerCase();

                // find the type of the fk entity
                ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);
                Class fkType = manyToManyAnnotation.targetEntity();

                // discover the type of the fk id
                JoinColumn[] joinColumns = joinTableAnnotation.inverseJoinColumns();
                String fkIdDatabaseColumnName = joinColumns[0].name();

                FieldMap fkFieldMap = FieldMapFactory.buildFieldMapForEntity(fkType);
                FieldDefinition fkIdFieldDefinition = fkFieldMap.get(fkIdDatabaseColumnName.toLowerCase());

                // iterate over all the foreign tables this entity has a relation with
                List<String> targetFkIds = joinTables.getTarget(tableName, id);

                if (targetFkIds != null) {
                    for (String fkId : targetFkIds) {
                        Serializable fkTransformedId;
                        if (fkIdFieldDefinition.getField().getAnnotation(GeneratedValue.class) != null) {
                            Class<?> fkIdType = fkIdFieldDefinition.getField().getType();
                            fkTransformedId = keyCache.getKey(fkType, fkIdType == Integer.class ? Integer.parseInt(fkId) : fkId);
                        } else {
                            fkTransformedId = fkId;
                        }

                        Serializable fk = parserDao.find(fkTransformedId, fkType);

                        Collection o = (Collection) field.get(targetEntity);
                        o.add(fk);
                    }
                }
            }
        }
    }

    private <T> boolean replaceEmbeddablesInEntity(FieldMap fieldMap, T targetEntity, Map<Class<?>, Object> embeddables)
            throws IllegalAccessException {
        boolean hasCompositeKey = false;

        for (FieldDefinition fieldDefinition : fieldMap.fieldDefinitions()) {
            Field field = fieldDefinition.getField();
            Class<?> fieldType = field.getType();

            if (fieldType.isAnnotationPresent(Embeddable.class)) {
                field.set(targetEntity, embeddables.get(fieldType));

                hasCompositeKey = true;
            }
        }

        return hasCompositeKey;
    }

    private <T> void resetId(FieldMap fieldMap, T domainObject) throws IllegalAccessException {
        FieldDefinition fieldDef = fieldMap.getGeneratedId();

        if (fieldDef != null) {
            fieldDef.getField().setAccessible(true);
            fieldDef.getField().set(domainObject, null);
        }
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
                status.addError(backupConfig.entityForClass(columnType), "ManyToOne relation not resolved");
            }
        } else if (columnType == String.class) {
            parsedValue = value;
        } else if (columnType.isEnum()) {
            parsedValue = Enum.valueOf((Class<Enum>) columnType, value);
        } else {
            if (transformerMap.containsKey(columnType)) {
                parsedValue = transformerMap.get(columnType).transform(value);
            } else {
                status.addError(backupConfig.entityForClass(columnType), "unknown type: " + columnType);
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
