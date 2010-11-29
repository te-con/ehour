package net.rrm.ehour.export.service.element;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.export.service.ParserUtil;
import net.rrm.ehour.export.service.PrimaryKeyCache;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.persistence.*;
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
public class DomainObjectParser
{
    private DomainObjectParserDao parserDao;

    private XMLEventReader reader;
    private PrimaryKeyCache keyCache;

    private static final Logger LOG = Logger.getLogger(DomainObjectParser.class);

    private static final Map<Class<?>, TypeTransformer<?>> transformerMap = new HashMap<Class<?>, TypeTransformer<?>>();

    static
    {
        transformerMap.put(Integer.class, new IntegerTransformer());
        transformerMap.put(Float.class, new FloatTransformer());
        transformerMap.put(Date.class, new DateTransformer());
        transformerMap.put(Boolean.class, new BooleanTransformer());
    }


    public DomainObjectParser(XMLEventReader reader, DomainObjectParserDao parserDao)
    {
        this.parserDao = parserDao;
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

            String data = ParserUtil.parseNextEventAsCharacters(reader);

            Class<? extends Serializable> type = (Class<? extends Serializable>) field.getType();
            Object parsedValue = parseValue(type, data);

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

        Serializable primaryKey = parserDao.persist(domainObject);

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

    private Serializable parseValue(Class<? extends Serializable> type, String value)
            throws IllegalAccessException, InstantiationException
    {
        Serializable parsedValue = null;

        if (type.isAnnotationPresent(Entity.class))
        {
            Serializable castToFk = castToFkType(type, value);
            // we're dealing with a ManyToOne, resolve the thing
            parsedValue = parserDao.find(castToFk, type);
        } else if (type == String.class)
        {
            parsedValue = value;
        } else if (type.isEnum())
        {
            parsedValue = Enum.valueOf((Class<Enum>) type, value);
        } else
        {
            if (transformerMap.containsKey(type))
            {
                parsedValue = transformerMap.get(type).transform(value);
            } else
            {
                LOG.error("no transformer for type " + type);
            }
        }
        return parsedValue;
    }

    private Serializable castToFkType(Class<?> fkObjectType, String value) throws InstantiationException, IllegalAccessException
    {
        Field[] fields = fkObjectType.getDeclaredFields();

        for (Field field : fields)
        {
            if (field.isAnnotationPresent(Id.class))
            {
                return parseValue((Class<? extends Serializable>) field.getType(), value);
            }
        }

        return value;
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

    private interface TypeTransformer<T extends Serializable>
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
