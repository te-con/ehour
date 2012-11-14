package net.rrm.ehour.config

import org.junit.Before
import org.junit.Test

/**
 * User: x082062 (Thies Edeling - thies@te-con.nl)
 * Date: 1/12/11 1:05 PM
 */
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
