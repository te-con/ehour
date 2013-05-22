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
        assertEquals(Locale.US, LocaleUtil.forLanguageTag("en-US"));
    }

    @Test
    public void should_deserialize_locale2() {
        assertEquals(new Locale("nl", "BE"), LocaleUtil.forLanguageTag("nl-BE"));
    }

    @Test
    public void should_default_to_nl_when_locale_not_set() {
        assertEquals(new Locale("nl", "nl"), LocaleUtil.forLanguageTag(""));
    }

    @Test
    public void should_ignore_euro_and_return_default() {
        assertEquals(new Locale("nl", "nl"), LocaleUtil.currencyForLanguageTag("euro"));
    }
}
