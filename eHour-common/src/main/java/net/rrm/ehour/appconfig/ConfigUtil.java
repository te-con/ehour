package net.rrm.ehour.appconfig;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public final class ConfigUtil
{

    private ConfigUtil()
    {
    }

    public static String getConfDir(String eHourHome)
    {
        String separator = System.getProperty("file.separator");

        StringBuffer buffer = new StringBuffer(eHourHome);
        buffer.append(separator);
        buffer.append("conf");
        buffer.append(separator);

        return buffer.toString();
    }

    public static Properties loadDatabaseProperties(String databaseName)
    {
        String filename = "hibernate_" + databaseName.toLowerCase() + ".properties";

        try
        {
            return loadProperties(filename);
        } catch (IOException e)
        {
            throw new IllegalArgumentException("Failed to load database config from: " + filename +
                    ". Did you spell the database name in ehour.properties right?", e);
        }
    }

    public static Properties loadProperties(String filename) throws IOException
    {
        ClassPathResource hibernatePropertiesResource = new ClassPathResource(filename);

        return PropertiesLoaderUtils.loadProperties(hibernatePropertiesResource);
    }

}
