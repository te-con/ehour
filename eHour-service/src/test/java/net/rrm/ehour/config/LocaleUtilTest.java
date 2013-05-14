package net.rrm.ehour.config;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class LocaleUtilTest {
    @Test
    public void should_serialize_valid_predefined_locale() {
        String languageTag = LocaleUtil.toLanguageTag(Locale.US);

        assertEquals("en-US", languageTag);
    }

    @Test
    public void should_serialize_valid_locale() {
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            String languageTag = LocaleUtil.toLanguageTag(locale);
            assertEquals(locale.getLanguage() + (StringUtils.isNotBlank(locale.getCountry()) ? ("-" + locale.getCountry()) : ""), languageTag);
        }
    }

    @Test
    public void should_serialize_locale_with_just_languageTag() {
        assertEquals("en", LocaleUtil.toLanguageTag(new Locale("en")));
    }

    @Test
    public void should_deserialize_locale_with_just_languageTag() {
        assertEquals(new Locale("en"), LocaleUtil.forLanguageTag("en"));
    }

    @Test
    public void should_deserialize_locale() {
        assertEquals(Locale.US, LocaleUtil.forLanguageTag("en-us"));
    }

    @Test
    public void should_deserialize_locale2() {
        assertEquals(new Locale("nl", "be"), LocaleUtil.forLanguageTag("nl-be"));
    }


}
