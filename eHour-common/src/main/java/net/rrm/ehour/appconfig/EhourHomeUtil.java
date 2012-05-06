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
     * @return
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
     * @param eHourHome
     * @return
     */
    public static String getConfDir(String eHourHome)
    {
        String separator = System.getProperty("file.separator");

        StringBuilder builder = new StringBuilder(eHourHome);
        builder.append(separator);
        builder.append("conf");
        builder.append(separator);

        return builder.toString();
    }

    /**
     * Get the ehour.properties location as a file
     * @param eHourHome
     * @return
     */
    public static File getEhourPropertiesFile(String eHourHome)
    {
        String eHourPropsFilename = getConfDir(eHourHome) + EHOUR_PROPERTIES_FILENAME;
        return new File(eHourPropsFilename);
    }



    /**
     * Get the dir with translations, relative to to ehour home
     */
    public static String getTranslationsDir(String eHourHome, String translationsDir)
    {
        String absoluteTranslationsPath = translationsDir.replace("%ehour.home%", (eHourHome != null) ? eHourHome : "");
        return absoluteTranslationsPath  + System.getProperty("file.separator");
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

    private static Properties loadProperties(String filename) throws IOException
    {
        ClassPathResource hibernatePropertiesResource = new ClassPathResource(filename);

        return PropertiesLoaderUtils.loadProperties(hibernatePropertiesResource);
    }
}
