package net.rrm.ehour.export.service;

import net.rrm.ehour.persistence.export.dao.ExportType;

import javax.persistence.Column;
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
    public <T> T parse(ExportType type, Class<T> clazz, XMLEvent event, XMLEventReader reader) throws IllegalAccessException, InstantiationException, XMLStreamException
    {
        T domainObject = clazz.newInstance();

        Map<String, Field> fieldMap = createFieldMap(clazz);

        recursiveFillObject(domainObject, fieldMap, event, reader);

        return domainObject;
    }

    private <T> void recursiveFillObject(T domainObject, Map<String, Field> fieldMap, XMLEvent event, XMLEventReader reader) throws XMLStreamException
    {
        if (event.isStartElement())
        {
            XMLEvent domainEvent = reader.nextEvent();

            StartElement startElement = event.asStartElement();
            String dbField = startElement.getName().getLocalPart();
            Field field = fieldMap.get(dbField);

            System.out.println(startElement);

            XMLEvent characterEvent = reader.nextEvent();

//            System.out.println(characterEvent);

            if (characterEvent.isCharacters())
            {
                String data = characterEvent.asCharacters().getData();

                Class<?> type = field.getType();

                System.out.println(type);
                if (type == Integer.class) {

                }

            }


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
        }

        return fieldMap;
    }
}
