package net.rrm.ehour;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

class ServerPropertiesConfigurator {
    private static final Logger LOGGER = Logger.getLogger(ServerPropertiesConfigurator.class);
    private static final String EHOUR_STANDALONE_PORT = "ehour.standalone.port";

    ServerConfig configureFromProperties(String filename) throws IOException {
        Properties props = loadProperties(filename);

        return new ServerConfig()
                .setPort(parseServerPort(props))
                .setDefaultConfigFileName(props.getProperty("jetty.config.location"));
    }

    private Integer parseServerPort(Properties props) {

        String serverPort = System.getenv(EHOUR_STANDALONE_PORT);

        if (serverPort == null) {
            serverPort = System.getProperty(EHOUR_STANDALONE_PORT);
        }

        if (serverPort == null) {
            serverPort = props.getProperty(EHOUR_STANDALONE_PORT);
        }

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

            try (FileInputStream stream = new FileInputStream(file)) {
                props.load(stream);
                return props;
            }
        } else {
            throw new FileNotFoundException(String.format("Configuration file %s not found.", filename));
        }
    }
}
