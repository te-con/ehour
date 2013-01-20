package net.rrm.ehour.config

import org.junit.Before
import org.junit.Test

class TranslationDiscoveryTest {

  def discovery;

  @Before
  void setUp() {
    discovery = new TranslationDiscovery()
    discovery.eHourHome = "src"
    discovery.translationsDir = "%ehour.home%/test/resources/i18n"
  }

  @Test
  void shouldDiscoverLanguages() {
    discovery.scanTranslations()
    def translations =  discovery.translations.sort()

    assert translations == ["cs", "de", "en", "el", "es", "fr", "it", "ja", "nl", "pl", "pt", "sv", "unknown"].sort()
  }
}
