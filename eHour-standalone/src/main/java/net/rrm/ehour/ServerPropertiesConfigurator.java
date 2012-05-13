package net.rrm.ehour;

import net.rrm.ehour.util.IoUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class ServerPropertiesConfigurator
{
    private final static Logger LOGGER = Logger.getLogger(ServerPropertiesConfigurator.class);

    public ServerConfig configureFromProperties(String filename) throws IOException
    {
        System.out.println("file: "+ filename);
        Properties props = loadProperties(filename);

        return new ServerConfig()
                .setDataBase(props.getProperty("ehour.database"))
                .setDataBaseDriver(props.getProperty("ehour.database.driver"))
                .setDataBaseURL(props.getProperty("ehour.database.url"))
                .setDataBaseUsername(props.getProperty("ehour.database.username"))
                .setDataBasePassword(props.getProperty("ehour.database.password"))
                .setPort(parseServerPort(props))
                .setDefaultConfigFileName(props.getProperty("jetty.config.location"));
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
            } catch (NumberFormatException nfe)
            {
                LOGGER.warn("Invalid port " + serverPort + ", using default port");
            }
        }
        return port;
    }

    private Properties loadProperties(String filename) throws IOException
    {
        Properties props = new Properties();

        File file = new File(filename);
        InputStream inputStream = null;
        System.out.println(filename +":" + file.exists());

        try
        {
            if (file.exists())
            {
                inputStream = new FileInputStream(file);

                props.load(inputStream);
            } else
            {
                inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);

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
        } finally
        {
            IoUtil.close(inputStream);
        }
        return props;
    }
}
