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
import java.util.HashMap;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 11:18:59 PM
 */
public class DomainObjectResolver
{
    private ImportDao importDao;

    public <T> T parse(ExportType type, Class<T> clazz, XMLEvent event, XMLEventReader reader) throws IllegalAccessException, InstantiationException, XMLStreamException
    {
        T domainObject = clazz.newInstance();

        Map<String, Field> fieldMap = createFieldMap(clazz);

        for (Map.Entry<String, Field> entry : fieldMap.entrySet())
        {
            System.out.println(entry.getValue());
        }

        recursiveFillObject(domainObject, fieldMap, reader.nextEvent(), reader);

        return domainObject;
    }

    private <T> void recursiveFillObject(T domainObject, Map<String, Field> fieldMap, XMLEvent event, XMLEventReader reader) throws XMLStreamException, IllegalAccessException
    {
        if (event.isStartElement())
        {
            StartElement startElement = event.asStartElement();
            String dbField = startElement.getName().getLocalPart();

            System.out.println(dbField);
            Field field = fieldMap.get(dbField);

            XMLEvent charEvent = reader.nextEvent();

            String data;

            if (charEvent.isCharacters())
            {
                data = charEvent.asCharacters().getData();
            } else {
                return;
            }

            Class<?> type = field.getType();

/*            if (type.isAnnotationPresent(Entity.class))
            {
                // we're dealing with a ManyToOne, resolve the thing
                Object o = importDao.find(1, type);
                field.set(domainObject, o);
            }
            else */if (type == String.class)
            {
                field.set(domainObject, data);
            }

        } else if (event.isEndElement())
        {
            return;
        } else if (event.isCharacters())
        {
            recursiveFillObject(domainObject, fieldMap, reader.nextEvent(), reader);
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
            }
            else if (field.isAnnotationPresent(JoinColumn.class))
            {
                JoinColumn column = field.getAnnotation(JoinColumn.class);
                String columnName = column.name();

                fieldMap.put(columnName, field);
            }
            else
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
