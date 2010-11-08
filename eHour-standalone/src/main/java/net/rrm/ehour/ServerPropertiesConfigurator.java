package net.rrm.ehour;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ServerPropertiesConfigurator
{
	private final static Logger LOGGER = Logger.getLogger(ServerPropertiesConfigurator.class);
	
	public ServerConfig configureFromProperties(String filename) throws IOException
	{
		Properties props = loadProperties(filename);
		
		ServerConfig builder = new ServerConfig()
						.setDataBase(props.getProperty("ehour.database"))
						.setDataBaseDriver(props.getProperty("ehour.database.driver"))
						.setDataBaseURL(props.getProperty("ehour.database.url"))
						.setDataBaseUsername(props.getProperty("ehour.database.username"))
						.setDataBasePassword(props.getProperty("ehour.database.password"))
						.setPort(parseServerPort(props))
						.setDefaultConfigFileName(props.getProperty("jetty.config.location"))
						;
		
		return builder;
		
	}

	private Integer parseServerPort(Properties props)
	{
		String serverPort = props.getProperty("ehour.standalone.port");
		Integer port = null;
		
		if (StringUtils.isNotBlank(serverPort)) {
			
			try
			{
				port = Integer.valueOf(serverPort);
			}
			catch (NumberFormatException nfe) {
				LOGGER.warn("Invalid port " + serverPort + ", using default port");
			}
		}
		return port;
	}

	private Properties loadProperties(String filename) throws FileNotFoundException, IOException
	{
		Properties props = new Properties();
		
		File file = new File(filename);
		
		if (file.exists())
		{
			props.load(new FileInputStream(file));
		} else
		{
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);

			if (inputStream == null)
			{
				throw new FileNotFoundException("property file '" + filename + "' not found in the classpath");
			}

			props.load(inputStream);

		}
		return props;
	}
}
