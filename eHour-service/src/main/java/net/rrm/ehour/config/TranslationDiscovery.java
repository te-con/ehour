package net.rrm.ehour.config;

import net.rrm.ehour.appconfig.ConfigUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

/**
 * User: x082062 (Thies Edeling - thies@te-con.nl)
 * Date: 1/12/11 1:05 PM
 */
@Component
public class TranslationDiscovery {
    private static final String FILE_PREFIX = "EhourWebApplication";
    private static final Logger LOG = Logger.getLogger(TranslationDiscovery.class);
    private static final Map<String, Locale> LOCALE_MAP = createLocaleMap();
    @Value("${ehour.home}")
    private String eHourHome;

    @Value("${ehour.translations}")
    private String translationsDir;

    private List<String> translations;

    @PostConstruct
    public void scanTranslations() {
        String absoluteTranslationsPath = ConfigUtil.getTranslationsDir(eHourHome, translationsDir);

        File transDir = new File(absoluteTranslationsPath);

        if (transDir.exists()) {
            translations = scanTranslations(transDir);
        } else {
            LOG.fatal("Translations dir " + transDir + " does not exist");
            translations = null;
        }
    }

    public List<String> getTranslations() {
        return translations;
    }

    private List<String> scanTranslations(File dir) {
        File[] files = dir.listFiles();

        List<String> translations = new ArrayList<String>();

        for (File file : files) {
            if (file.getName().startsWith(FILE_PREFIX)) {
                translations.add(parseTranslationFile(file));
            } else {
                LOG.warn("Invalid filename for translations, ignoring: " + file.getName());
            }
        }

        return translations;
    }

    private String parseTranslationFile(File file) {
        String fileName = file.getName();

        String localeShort;

        if (fileName.contains("_")) {
            localeShort = fileName.substring(fileName.indexOf('_') + 1, fileName.indexOf('.'));

            if (LOCALE_MAP.containsKey(localeShort)) {
                String localeDisplayName = LOCALE_MAP.get(localeShort).getDisplayName();

                String name;

                if (localeDisplayName.contains("(")) {
                    name = localeDisplayName.substring(0, localeDisplayName.indexOf('(') - 1);
                } else {
                    name = localeDisplayName;
                }

                LOG.info("Found translation file for " + name + " (" + fileName + ")");
            } else {
                LOG.info("Found translation file for unknown language: " + fileName);
            }
        } else {
            LOG.info("Found translation file for English (" + fileName + ")");
            localeShort = "en";
        }

        return localeShort;
    }

    private static Map<String, Locale> createLocaleMap() {
        Locale[] availableLocales = Locale.getAvailableLocales();

        Map<String, Locale> map = new HashMap<String, Locale>();

        for (Locale availableLocale : availableLocales) {
            map.put(availableLocale.getLanguage(), availableLocale);
        }

        return map;
    }
}
