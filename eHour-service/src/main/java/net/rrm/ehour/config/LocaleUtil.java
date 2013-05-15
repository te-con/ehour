package net.rrm.ehour.config;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;

public class LocaleUtil {
    public static Locale forLanguageTag(String tag) {
        if (StringUtils.isBlank(tag)) {
            return new Locale("nl", "NL");
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
