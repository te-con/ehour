package net.rrm.ehour;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

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
                .setDefaultConfigFileName(props.getProperty("jetty.config.location"));

        return builder;

    }

    private Integer parseServerPort(Properties props)
    {
        String serverPort = props.getProperty("ehour.standalone.port");
        Integer port = null;

        if (StringUtils.isNotBlank(serverPort))
        {

            try
            {
                port = Integer.valueOf(serverPort);
            }
            catch (NumberFormatException nfe)
            {
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
                LOGGER.debug("property file '" + filename + "' not found in the classpath, using it as absolute path");


                try
                {
                    inputStream = new FileInputStream(filename);
                } catch (FileNotFoundException e)
                {
                    throw new FileNotFoundException("property field " + filename + " not found in classpath or as absolute path");
                }
            }

            props.load(inputStream);

        }
        return props;
    }
}
