package net.rrm.ehour.ui.listener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/23/11 - 2:17 AM
 */
public class EnvInitListener implements ServletContextListener
{
    private static final Logger LOG = Logger.getLogger(EnvInitListener.class);


    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        String home = System.getenv("EHOUR_HOME");

        if (StringUtils.isBlank(home))
        {
            throw new IllegalArgumentException("EHOUR_HOME environment variable not defined - exiting");
        }

        System.getProperties().put("EHOUR_HOME", home);

        configureLog4j(home);

        LOG.warn("EHOUR_HOME set to " + home);
    }

    private void configureLog4j(String eHourHome)
    {
        String separator = System.getProperty("file.separator");

        StringBuilder log4jConfigPath = new StringBuilder(eHourHome);
        log4jConfigPath.append(separator);
        log4jConfigPath.append("conf");
        log4jConfigPath.append(separator);
        log4jConfigPath.append("log4j.properties");

        PropertyConfigurator.configure(log4jConfigPath.toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
    }
}
