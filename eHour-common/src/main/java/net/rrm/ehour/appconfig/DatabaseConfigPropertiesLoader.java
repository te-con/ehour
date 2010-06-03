package net.rrm.ehour.appconfig;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class DatabaseConfigPropertiesLoader
{
	public static Properties loadProperties(String databaseName) {
		String filename = "hibernate_" + databaseName.toLowerCase() + ".properties";
		
		ClassPathResource hibernatePropertiesResource = new ClassPathResource(filename);
		
		try
		{
			Properties properties = PropertiesLoaderUtils.loadProperties(hibernatePropertiesResource);
			return properties;
		} catch (IOException e)
		{
			throw new IllegalArgumentException("Failed to load database config from: " + filename + 
					". Did you spell the database name in ehour.properties right?", e);
		}
	}
	
}
