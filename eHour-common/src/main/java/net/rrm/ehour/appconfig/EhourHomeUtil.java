package net.rrm.ehour.appconfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public final class EhourHomeUtil
{
    public static final String EHOUR_HOME = "EHOUR_HOME";
    public static final String EHOUR_PROPERTIES_FILENAME = "ehour.properties";

    private EhourHomeUtil()
    {
    }

    /**
     * Get EHOUR_HOME property, either as a system property or an environment variable
     */
    public static String getEhourHome()
    {
        String eHourHome = System.getProperty(EHOUR_HOME);

        if (StringUtils.isBlank(eHourHome))
        {
            eHourHome = System.getenv(EHOUR_HOME);
        }

        return eHourHome;
    }

    public static boolean isEhourHomeDefined() {
        return StringUtils.isNotBlank(System.getProperty(EHOUR_HOME)) || StringUtils.isNotBlank(System.getenv(EHOUR_HOME));
    }

    /**
     * Get the conf dir, relative to ehour home dir
     */
    public static String getConfDir(String eHourHome)
    {
        return String.format("%s/conf/", eHourHome);
    }

    public static File getFileInConfDir(String filename) {
        String configurationDir = getConfDir(getEhourHome());

        return new File(configurationDir, filename);
    }

    /**
     * Get the ehour.properties location as a file
     */
    public static File getEhourPropertiesFile()
    {
        return getFileInConfDir(EHOUR_PROPERTIES_FILENAME);
    }

    /**
     * Get the dir with translations, relative to to ehour home
     */
    public static String getTranslationsDir(String eHourHome, String translationsDir)
    {
        String absoluteTranslationsPath = translationsDir.replace("%ehour.home%", (eHourHome != null) ? eHourHome : "");
        return absoluteTranslationsPath  + "/";
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

    public static void setEhourHome(String homeDir) {
        System.getProperties().put(EhourHomeUtil.EHOUR_HOME, homeDir);
    }

    private static Properties loadProperties(String filename) throws IOException
    {
        ClassPathResource resource = new ClassPathResource(filename);

        return PropertiesLoaderUtils.loadProperties(resource);
    }
}
