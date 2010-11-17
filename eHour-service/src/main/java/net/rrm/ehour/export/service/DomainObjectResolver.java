package net.rrm.ehour.export.service;

import net.rrm.ehour.persistence.export.dao.ExportType;

import javax.persistence.Column;
import java.lang.reflect.Field;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 11:18:59 PM
 */
public class DomainObjectResolver
{
    public <T> T parse(ExportType type, Class<T> clazz) throws IllegalAccessException, InstantiationException
    {
        T domainObject = clazz.newInstance();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields)
        {
            if (field.isAnnotationPresent(Column.class))
            {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();

                System.out.println(columnName + " on " + field.getName());
            }
        }

        return domainObject;
    }
}
