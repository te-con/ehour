package net.rrm.ehour.export.service;

import net.rrm.ehour.persistence.export.dao.ExportType;
import net.rrm.ehour.persistence.export.dao.ImportDao;
import org.apache.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
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
    }


    public DomainObjectResolver(XMLEventReader reader, ImportDao importDao)
    {
        this.importDao = importDao;
        this.reader = reader;

        keyCache = new PrimaryKeyCache();
    }

    public <T> List<T> parse(ExportType type, Class<T> clazz) throws IllegalAccessException, InstantiationException, XMLStreamException
    {
        Map<String, Field> fieldMap = createFieldMap(clazz);

        // skip the collection tag
        reader.nextTag();

        return parseDomainObject(clazz, fieldMap);
    }

    /**
     * Parse domain object with reader pointing on the table name tag
     */
    private <T> List<T> parseDomainObject(Class<T> clazz, Map<String, Field> fieldMap) throws XMLStreamException, IllegalAccessException, InstantiationException
    {
        List<T> domainObjects = new ArrayList<T>();

        while (reader.hasNext())
        {
            //  domain object tag
            XMLEvent event = reader.nextTag();

            if (event.isStartElement())
            {
                T domainObject = parseDomainObjectAttributes(clazz, fieldMap);

                domainObjects.add(domainObject);
            } else if (event.isEndElement())
            {
                break;
            }
        }

        return domainObjects;
    }

    private <T> T parseDomainObjectAttributes(Class<T> clazz, Map<String, Field> fieldMap) throws XMLStreamException, IllegalAccessException, InstantiationException
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

            XMLEvent charEvent = reader.nextEvent();

            String data;

            if (charEvent.isCharacters())
            {
                data = charEvent.asCharacters().getData();

                Class<?> type = field.getType();

                /*            if (type.isAnnotationPresent(Entity.class))
               {
                   // we're dealing with a ManyToOne, resolve the thing
                   Object o = importDao.find(1, type);
                   field.set(domainObject, o);
               }
               else */
                if (type == String.class)
                {
                    field.set(domainObject, data);
                } else
                {
                    Object transformedValue = null;

                    if (transformerMap.containsKey(type))
                    {
                        transformedValue = transformerMap.get(type).transform(data);
                    } else
                    {
                        LOG.error("no transformer for type " + type);
                    }


                    if (transformedValue != null)
                    {

                        if (field.getDeclaringClass() != domainObject.getClass()) {
                            Object embeddable;

                            if (!embeddables.containsKey(field.getDeclaringClass()))
                            {
                                embeddable = field.getDeclaringClass().newInstance();
                                embeddables.put(field.getDeclaringClass(), embeddable);
                            } else {
                                embeddable = embeddables.get(field.getDeclaringClass());
                            }

                            field.set(embeddable, transformedValue);
                        }
                        else
                        {
                            field.set(domainObject, transformedValue);
                        }
                    }
                }
            }

            reader.nextEvent();
        }

        for (Field field : fieldMap.values())
        {
            Class<?> fieldType = field.getType();

            if (fieldType.isAnnotationPresent(Embeddable.class))
            {
                field.set(domainObject, embeddables.get(fieldType));
            }
        }



        return domainObject;
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
                fieldMap.put(Integer.toString(seq++), field);

                // do we need to go a level deeper with composite primary keys marked as Embeddable?
                if (fieldType.isAnnotationPresent(Embeddable.class))
                {
                    Map<String, Field> embeddableFieldMap = createFieldMap(fieldType);

                    fieldMap.putAll(embeddableFieldMap);

                }
            }
        }

        return fieldMap;
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
            return Integer.parseInt(value);
        }
    }

    private static class FloatTransformer implements TypeTransformer<Float>
    {
        @Override
        public Float transform(String value)
        {
            return Float.parseFloat(value);
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
