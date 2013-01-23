package net.rrm.ehour.ui.common.i18n;

import org.apache.wicket.resource.PropertiesFactory;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/9/11 - 4:46 PM
 */
public class EhourHomeResourceLoader implements IResourceFinder

{
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesFactory.class);

    private String translationsDir;

    public EhourHomeResourceLoader(String translationsDir) {
        this.translationsDir = translationsDir;
    }

    @Override
    public IResourceStream find(Class<?> clazz, String path) {
        String propertyName = path.substring(path.lastIndexOf("/") + 1) + "properties";

        final File file = new File(translationsDir + propertyName);

        if (file.exists()) {
            LOG.debug("Loading properties from " + file.getAbsolutePath());

            return new FileResourceStream(file);
        } else {
            return null;
        }
    }
}
