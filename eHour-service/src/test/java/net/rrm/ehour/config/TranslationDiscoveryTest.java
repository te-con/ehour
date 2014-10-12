package net.rrm.ehour.config;

import net.rrm.ehour.appconfig.EhourSystemConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TranslationDiscoveryTest {
    private TranslationDiscovery discovery;

    @Before
    public void setUp() {
        EhourSystemConfig config1 = new EhourSystemConfig("src", "%ehour.home%/test/resources/i18n");
        discovery = new TranslationDiscovery(config1);
    }

    @Test
    public void shouldDiscoverLanguages() {
        discovery.scanTranslations();
        List<String> translations = discovery.getTranslations();
        Collections.sort(translations);

        List<String> expected = Arrays.asList("cs", "de", "en", "el", "es", "fr", "it", "ja", "nl", "pl", "pt", "sv", "unknown");
        Collections.sort(expected);

        assertThat(translations, is(expected));
    }
}
