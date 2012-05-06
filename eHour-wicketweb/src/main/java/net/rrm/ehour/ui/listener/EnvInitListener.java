package net.rrm.ehour.ui.listener;

import net.rrm.ehour.appconfig.EhourHomeUtil;
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
        String home = getEhourHomePath(sce);

        if (StringUtils.isBlank(home))
        {
            throw new IllegalArgumentException("EHOUR_HOME environment variable or context parameter not defined - exiting");
        }

        System.getProperties().put(EhourHomeUtil.EHOUR_HOME, home);

        configureLog4j(home);

        LOG.info("EHOUR_HOME set to " + home);
    }

    private String getEhourHomePath(ServletContextEvent sce)
    {
        String home = EhourHomeUtil.getEhourHome();

        if (StringUtils.isBlank(home))
        {
            home = sce.getServletContext().getInitParameter(EhourHomeUtil.EHOUR_HOME);
        }

        return home;
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
