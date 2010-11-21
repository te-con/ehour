package net.rrm.ehour.export.service;

import net.rrm.ehour.persistence.export.dao.ExportType;
import net.rrm.ehour.persistence.export.dao.ImportDao;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 11:18:59 PM
 */
public class DomainObjectResolver
{
    private ImportDao importDao;
    private XMLEventReader reader;
    private PrimaryKeyCache keyCache;

    public DomainObjectResolver(XMLEventReader reader, ImportDao importDao)
    {
        this.importDao = importDao;
        this.reader = reader;

        keyCache = new PrimaryKeyCache();
    }

    /**
     * @param type
     * @param clazz
     * @param event
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws XMLStreamException
     */
    public <T> List<T> parse(ExportType type, Class<T> clazz, XMLEvent event) throws IllegalAccessException, InstantiationException, XMLStreamException
    {
        Map<String, Field> fieldMap = createFieldMap(clazz);

        return parseDomainObject(clazz, fieldMap);
    }

    private <T> List<T> parseDomainObject(Class<T> clazz, Map<String, Field> fieldMap) throws XMLStreamException, IllegalAccessException, InstantiationException
    {
        List<T> domainObjects = new ArrayList<T>();

        while (reader.hasNext())
        {
            XMLEvent event = reader.nextEvent();

            if (event.isStartElement())
            {
                // skip the characters
                reader.nextEvent();

                T domainObject = clazz.newInstance();

                parseDomainObjectAttributes(domainObject, fieldMap);

                domainObjects.add(domainObject);
            } else if (event.isEndElement())
            {
                break;
            }
        }

        return domainObjects;
    }

    private <T> void parseDomainObjectAttributes(T domainObject, Map<String, Field> fieldMap) throws XMLStreamException, IllegalAccessException
    {
        while (reader.hasNext())
        {
            XMLEvent event = reader.nextEvent();

            if (event.isEndElement())
            {
                return;
            }

            StartElement startElement = event.asStartElement();
            String dbField = startElement.getName().getLocalPart();

            System.out.println(dbField);
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
                }
            }

            reader.nextEvent();
        }
    }

    private <T> Map<String, Field> createFieldMap(Class<T> clazz)
    {
        Map<String, Field> fieldMap = new HashMap<String, Field>();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields)
        {
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
                    Map<String, Field> embeddableFieldMap = createFieldMap(fieldType);

                    fieldMap.putAll(embeddableFieldMap);

                }
            }
        }

        return fieldMap;
    }
}
