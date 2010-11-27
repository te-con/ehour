package net.rrm.ehour.export.service;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.persistence.export.dao.ImportDao;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 11:18:59 PM
 */
public class DomainObjectResolver
{
    private ImportDao importDao;
    private XMLEventReader reader;
    private PrimaryKeyCache keyCache;

    private static final Logger LOG = Logger.getLogger(DomainObjectResolver.class);

    private static final Map<Class<?>, TypeTransformer<?>> transformerMap = new HashMap<Class<?>, TypeTransformer<?>>();

    static
    {
        transformerMap.put(Integer.class, new IntegerTransformer());
        transformerMap.put(Float.class, new FloatTransformer());
        transformerMap.put(Date.class, new DateTransformer());
        transformerMap.put(Boolean.class, new BooleanTransformer());
    }


    public DomainObjectResolver(XMLEventReader reader, ImportDao importDao)
    {
        this.importDao = importDao;
        this.reader = reader;

        keyCache = new PrimaryKeyCache();
    }

    public <T extends DomainObject<?, ?>> List<T> parse(Class<T> clazz) throws IllegalAccessException, InstantiationException, XMLStreamException
    {
        Map<String, Field> fieldMap = createFieldMap(clazz);

        return parseDomainObjects(clazz, fieldMap);
    }

    /**
     * Parse domain object with reader pointing on the table name tag
     */
    private <T extends DomainObject<?, ?>> List<T> parseDomainObjects(Class<T> clazz, Map<String, Field> fieldMap) throws XMLStreamException, IllegalAccessException, InstantiationException
    {
        List<T> domainObjects = new ArrayList<T>();

        while (reader.hasNext())
        {
            XMLEvent event = reader.nextTag();

            if (event.isStartElement())
            {
                T domainObject = parseDomainObject(clazz, fieldMap);

                domainObjects.add(domainObject);
            } else if (event.isEndElement())
            {
                break;
            }
        }

        return domainObjects;
    }

    private <T extends DomainObject<?, ?>> T parseDomainObject(Class<T> clazz, Map<String, Field> fieldMap) throws XMLStreamException, IllegalAccessException, InstantiationException
    {
        T domainObject = clazz.newInstance();

        Map<Class<?>, Object> embeddables = new HashMap<Class<?>, Object>();

        while (reader.hasNext())
        {
            XMLEvent event = reader.nextTag();

            if (event.isEndElement())
            {
                break;
            }

            StartElement startElement = event.asStartElement();
            String dbField = startElement.getName().getLocalPart();
            Field field = fieldMap.get(dbField);

            XMLEvent charEvent;

            // no chars can only mean an end element
            StringBuffer data = new StringBuffer();

            while ((charEvent = reader.nextEvent()).isCharacters())
            {
                data.append(charEvent.asCharacters().getData());
            }

            Object parsedValue = parseValue(domainObject, field, data.toString());

            if (parsedValue != null)
            {
                // check whether the field is part of a composite pk (ie. a different class)
                if (field.getDeclaringClass() != domainObject.getClass())
                {
                    Object embeddable;

                    if (!embeddables.containsKey(field.getDeclaringClass()))
                    {
                        embeddable = field.getDeclaringClass().newInstance();
                        embeddables.put(field.getDeclaringClass(), embeddable);
                    } else
                    {
                        embeddable = embeddables.get(field.getDeclaringClass());
                    }

                    field.set(embeddable, parsedValue);
                } else
                {
                    field.set(domainObject, parsedValue);
                }
            }
        }

        boolean hasCompositeKey = setEmbeddablesInDomainObject(fieldMap, domainObject, embeddables);

        Serializable primaryKey = importDao.persist(domainObject);

        if (!hasCompositeKey)

        {
            keyCache.putKey(domainObject.getClass(), domainObject.getPK(), primaryKey);
        }

        return domainObject;
    }

    private <T> boolean setEmbeddablesInDomainObject(Map<String, Field> fieldMap, T domainObject, Map<Class<?>, Object> embeddables)
            throws IllegalAccessException
    {
        boolean hasCompositeKey = false;

        for (Field field : fieldMap.values())
        {
            Class<?> fieldType = field.getType();

            if (fieldType.isAnnotationPresent(Embeddable.class))
            {
                field.set(domainObject, embeddables.get(fieldType));

                hasCompositeKey = true;
            }
        }

        return hasCompositeKey;
    }

    private <T> Object parseValue(T domainObject, Field field, String value)
            throws IllegalAccessException, InstantiationException
    {
        Class<?> type = field.getType();
        Object parsedValue = null;

        if (type.isAnnotationPresent(Entity.class))
        {
            // we're dealing with a ManyToOne, resolve the thing
            parsedValue = importDao.find(1, type);
        } else if (type == String.class)
        {
            parsedValue = value;

            field.set(domainObject, value);
        } else if (type.isEnum())
        {
            parsedValue = Enum.valueOf((Class<Enum>) type, value);
        } else
        {
            if (transformerMap.containsKey(type))
            {
                parsedValue = transformerMap.get(type).transform(value);
            } else if (type.isPrimitive())
            {
                LOG.error("no transformer for type " + type);
            }
        }
        return parsedValue;
    }

    private <T> Map<String, Field> createFieldMap(Class<T> clazz)
    {
        Map<String, Field> fieldMap = new HashMap<String, Field>();

        Field[] fields = clazz.getDeclaredFields();
        int seq = 0;

        for (Field field : fields)
        {
            field.setAccessible(true);

            if (field.isAnnotationPresent(Column.class))
            {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();

                fieldMap.put(columnName, field);
            } else if (field.isAnnotationPresent(JoinColumn.class))
            {
                JoinColumn column = field.getAnnotation(JoinColumn.class);
                String columnName = column.name();

                fieldMap.put(columnName, field);
            } else
            {
                Class<?> fieldType = field.getType();

                // do we need to go a level deeper with composite primary keys marked as Embeddable?
                if (fieldType.isAnnotationPresent(Embeddable.class))
                {
                    fieldMap.put(Integer.toString(seq++), field);

                    Map<String, Field> embeddableFieldMap = createFieldMap(fieldType);

                    fieldMap.putAll(embeddableFieldMap);

                }
            }
        }

        return fieldMap;
    }

    PrimaryKeyCache getKeyCache()
    {
        return keyCache;
    }

    private interface TypeTransformer<T>
    {
        T transform(String value);
    }

    private static class IntegerTransformer implements TypeTransformer<Integer>
    {
        @Override
        public Integer transform(String value)
        {
            return StringUtils.isNotBlank(value) ? Integer.parseInt(value) : null;
        }
    }

    private static class BooleanTransformer implements TypeTransformer<Boolean>
    {
        @Override
        public Boolean transform(String value)
        {
            return "y".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "1".equals(value);
        }
    }

    private static class FloatTransformer implements TypeTransformer<Float>
    {
        @Override
        public Float transform(String value)
        {
            return StringUtils.isNotBlank(value) ? Float.parseFloat(value) : null;
        }
    }

    private static class DateTransformer implements TypeTransformer<Date>
    {
        private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Date transform(String value)
        {
            try
            {
                return FORMATTER.parse(value);
            } catch (ParseException e)
            {
                LOG.error("Failed to parse date: " + value);
                return null;
            }
        }
    }

}
