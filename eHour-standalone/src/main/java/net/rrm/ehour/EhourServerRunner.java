package net.rrm.ehour;

import net.rrm.ehour.appconfig.EhourHomeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * @author thies
 */
public class EhourServerRunner {
    private final static Logger LOGGER = Logger.getLogger(EhourServerRunner.class);

    public static void main(String[] args) {
        validateEhourHome();
        fixForwardSlashesInEhourHome();

        String filename = args != null && args.length >= 1 ? args[0] : "${EHOUR_HOME}/conf/ehour.properties";

        ServerPropertiesConfigurator configuration = new ServerPropertiesConfigurator();

        try {
            ServerConfig config = configuration.configureFromProperties(replaceSystemEnv(filename));
            new EhourServer().startServer(config);
        } catch (Exception e) {
            LOGGER.error("Failed to start server", e);
        }
    }

    private static void fixForwardSlashesInEhourHome() {
        if (EhourHomeUtil.isEhourHomeDefined()) {
            String home = EhourHomeUtil.getEhourHome();
            String forwardSlashesOnly = home.replace('\\', '/');

            System.getProperties().put(EhourHomeUtil.EHOUR_HOME, forwardSlashesOnly);
        }
    }


    private static void validateEhourHome() {
        if (!EhourHomeUtil.isEhourHomeDefined()) {
            try {
                String path = new File(".").getCanonicalPath();

                LOGGER.info(String.format("EHOUR_HOME not set, setting it to %s", path));

                System.getProperties().put(EhourHomeUtil.EHOUR_HOME, path);
            } catch (IOException e) {
                LOGGER.error("Failed to get current path", e);
            }
        }
    }


    private static String replaceSystemEnv(String filename) {
        String ehourHome = EhourHomeUtil.getEhourHome();

        if (StringUtils.isBlank(ehourHome)) {
            throw new IllegalArgumentException("EHOUR_HOME is not defined as a environment variable");
        }

        return filename.replace("${EHOUR_HOME}", ehourHome);
    }
}
