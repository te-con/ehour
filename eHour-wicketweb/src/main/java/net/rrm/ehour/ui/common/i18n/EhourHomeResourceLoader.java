package net.rrm.ehour.ui.common.i18n;

import net.rrm.ehour.util.IoUtil;
import org.apache.wicket.Application;
import org.apache.wicket.resource.Properties;
import org.apache.wicket.resource.PropertiesFactory;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.listener.IChangeListener;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.util.watch.IModificationWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/9/11 - 4:46 PM
 */
public class EhourHomeResourceLoader implements PropertiesFactory.IPropertiesLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesFactory.class);

    private Application application;
    private String translationsDir;

    public EhourHomeResourceLoader(Application application, String translationsDir)
    {
        this.application = application;
        this.translationsDir = translationsDir;
    }

    @Override
    public Properties load(Class<?> clazz, final String path)
    {
        String propertyName = path.substring(path.lastIndexOf("/") + 1) + "properties";

        final File file = new File(translationsDir + propertyName);

        if (file.exists())
        {
            LOG.debug("Loading properties from " + file.getAbsolutePath());
            FileInputStream stream = null;

            try
            {
                stream = new FileInputStream(file);
                java.util.Properties properties = new java.util.Properties();
                properties.load(stream);

                // Watch file modifications
                final IModificationWatcher watcher = application.getResourceSettings().getResourceWatcher(true);

                if (watcher != null)
                {
                    watcher.add(file, new IChangeListener()
                    {
                        public void onChange()
                        {
                            LOG.info("A properties files has changed. Removing all entries from the cache. Resource: " + file);

                            application.getResourceSettings().getPropertiesFactory().clearCache();
                        }
                    });
                }

                ValueMap strings = new ValueMap();
                Enumeration<?> enumeration = properties.propertyNames();

                while (enumeration.hasMoreElements())
                {
                    String property = (String) enumeration.nextElement();
                    strings.put(property, properties.getProperty(property));
                }

                return new Properties(path, strings);
            } catch (IOException e)
            {
            } finally
            {
                IoUtil.close(stream);
            }
        }

        return null;
    }
}
