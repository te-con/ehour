package net.rrm.ehour;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

class ServerPropertiesConfigurator {
    private final static Logger LOGGER = Logger.getLogger(ServerPropertiesConfigurator.class);

    ServerConfig configureFromProperties(String filename) throws IOException {
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

    private Integer parseServerPort(Properties props) {
        String serverPort = props.getProperty("ehour.standalone.port");
        Integer port = null;

        if (StringUtils.isNotBlank(serverPort)) {
            try {
                port = Integer.valueOf(serverPort);
            } catch (NumberFormatException nfe) {
                LOGGER.warn("Invalid port " + serverPort + ", using default port");
            }
        }
        return port;
    }

    private Properties loadProperties(String filename) throws IOException {
        File file = new File(filename);

        if (file.exists()) {
            Properties props = new Properties();
            props.load(new FileInputStream(file));
            return props;
        } else {
            throw new FileNotFoundException(String.format("Configuration file %s not found.", filename));
        }
    }
}
