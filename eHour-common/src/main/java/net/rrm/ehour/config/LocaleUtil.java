package net.rrm.ehour.config;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;

public class LocaleUtil {

    public static final Locale DEFAULT_LOCALE = new Locale("nl", "NL");

    private LocaleUtil() {
    }

    public static Locale currencyForLanguageTag(String tag) {
        if (StringUtils.isBlank(tag)) {
            return DEFAULT_LOCALE;
        }

        String[] localeTags = tag.split("-");

        if (localeTags.length >= 2) {
            return new Locale(localeTags[0], localeTags[1]);
        } else if  (localeTags[0].length() > 2) {
            return DEFAULT_LOCALE;
        } else {
            return new Locale(localeTags[0], localeTags[0]);
        }
    }

    public static Locale forLanguageTag(String tag) {
        if (StringUtils.isBlank(tag)) {
            return DEFAULT_LOCALE;
        }

        String[] localeTags = tag.split("-");

        if (localeTags.length >= 2) {
            return new Locale(localeTags[0], localeTags[1]);
        } else {
            return new Locale(localeTags[0]);
        }
    }

    public static String toLanguageTag(Locale locale) {
        return locale.getLanguage() + (StringUtils.isNotBlank(locale.getCountry()) ? "-" + locale.getCountry().toUpperCase() : "");
    }
}
