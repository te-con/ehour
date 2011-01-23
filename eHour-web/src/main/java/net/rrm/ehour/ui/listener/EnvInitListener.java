package net.rrm.ehour.ui.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/23/11 - 2:17 AM
 */
public class EnvInitListener implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        String home = System.getenv("EHOUR_HOME");
        System.getProperties().put("EHOUR_HOME", home);
        System.getProperties().put("log4j.configuration", home + "/conf/log4j.properties");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
    }
}
