package net.rrm.ehour.appconfig;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;

public class EhourHomeFinder {

    private EhourHomeFinder() {
    }

    public static void fixEhourHome() throws IOException {
        convertBackwardSlashesInEhourHome();
        setDefaultEhourHome();
        defineDerbyHome();
    }

    private static void defineDerbyHome() {
        String derbyHome = System.getProperty("derby.system.home");

        if (StringUtils.isBlank(derbyHome)) {
            System.getProperties().put("derby.system.home", String.format("%s/db", EhourHomeUtil.getEhourHome()));
        }
    }

    // on Windows install4j provides the current path with backward slashes, convert to forward slashes
    private static void convertBackwardSlashesInEhourHome() {
        if (EhourHomeUtil.isEhourHomeDefined()) {
            String home = EhourHomeUtil.getEhourHome();
            String forwardSlashesOnly = home.replace('\\', '/');

            EhourHomeUtil.setEhourHome(forwardSlashesOnly);
        }
    }

    // check whether an ehour home is defined, use the current path when not defined
    private static void setDefaultEhourHome() throws IOException {
        useCurrentDirectoryWhenNotDefined();

        if (!EhourHomeUtil.getEhourPropertiesFile().exists()) {
            whenHomeDefinedButPropertyNotFoundTryTheHomeSubdir();
        }
    }

    private static void whenHomeDefinedButPropertyNotFoundTryTheHomeSubdir() {
        String newHome = String.format("%s/home", EhourHomeUtil.getEhourHome());

        String configurationDir = EhourHomeUtil.getConfDir(newHome);

        File newHomeFile = new File(configurationDir, EhourHomeUtil.EHOUR_PROPERTIES_FILENAME);

        if (newHomeFile.exists()) {
            EhourHomeUtil.setEhourHome(newHome);
        } else {
            throw new IllegalArgumentException(String.format("EHOUR_HOME is not properly defined - ehour.properties not found in %s", newHome));
        }
    }

    private static void useCurrentDirectoryWhenNotDefined() throws IOException {
        if (!EhourHomeUtil.isEhourHomeDefined()) {
            String path = new File(".").getCanonicalPath();

            EhourHomeUtil.setEhourHome(path);
        }
    }
}
