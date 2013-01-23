package net.rrm.ehour.ui.common.i18n;

import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import java.io.File;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/9/11 - 4:46 PM
 */
public class EhourHomeResourceLoader implements IResourceFinder {
    private String translationsDir;

    public EhourHomeResourceLoader(String translationsDir) {
        this.translationsDir = translationsDir;
    }

    @Override
    public IResourceStream find(Class<?> clazz, String path) {
        String propertyName = path.substring(path.lastIndexOf("/") + 1);

        File file = new File(translationsDir + propertyName);

        if (file.exists()) {
            return new FileResourceStream(file);
        } else {
            return null;
        }
    }
}
