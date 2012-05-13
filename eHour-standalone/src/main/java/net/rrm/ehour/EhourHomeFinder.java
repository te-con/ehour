package net.rrm.ehour;

import net.rrm.ehour.appconfig.EhourHomeUtil;

import java.io.File;
import java.io.IOException;

class EhourHomeFinder {

    private EhourHomeFinder() {
    }

    static void fixEhourHome() throws IOException {
        convertBackwardSlashesInEhourHome();
        setDefaultEhourHome();
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
        if (!EhourHomeUtil.isEhourHomeDefined()) {
            String path = new File(".").getCanonicalPath();

            EhourHomeUtil.setEhourHome(path);
        }

        String home = EhourHomeUtil.getEhourHome();

        File ehourPropertiesFile = EhourHomeUtil.getEhourPropertiesFile(home);

        if (!ehourPropertiesFile.exists()) {
            String newHome = String.format("%s/home", home);

            File newHomeFile = EhourHomeUtil.getEhourPropertiesFile(newHome);

            if (newHomeFile.exists()) {
                EhourHomeUtil.setEhourHome(newHome);
            } else {
                throw new IllegalArgumentException(String.format("EHOUR_HOME value of %s is not properly defined, %s/home not found", home, home));
            }
        }
    }
}
