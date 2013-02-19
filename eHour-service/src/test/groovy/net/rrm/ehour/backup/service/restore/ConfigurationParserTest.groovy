package net.rrm.ehour.backup.service.restore

import org.junit.Before
import org.junit.Test

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory

import static org.junit.Assert.assertEquals

/**
 * @author thies (thies@te-con.nl)
 * Date: 11/30/10 12:11 AM
 */
class ConfigurationParserTest
{
  private ConfigurationParserDaoValidatorImpl daoValidator;

  ConfigurationParser parser
  XMLEventReader eventReader

  @Before
  void setUp()
  {
    def xmlData = """<CONFIGURATION>
  <CONFIG KEY="completeDayHours">8</CONFIG>
  <CONFIG KEY="showTurnOver">true</CONFIG>
  <CONFIG KEY="localeLanguage">en</CONFIG>
  <CONFIG KEY="currency">Euro</CONFIG>
  <CONFIG KEY="localeCountry"></CONFIG>
  <CONFIG KEY="availableTranslations">en,nl,fr,it</CONFIG>
  <CONFIG KEY="mailFrom">noreply@localhost.net</CONFIG>
  <CONFIG KEY="mailSmtp">127.0.0.1</CONFIG>
  <CONFIG KEY="demoMode">false</CONFIG>
  <CONFIG KEY="version">0.8.3</CONFIG>
  <CONFIG KEY="smtpPort">25</CONFIG>
 </CONFIGURATION>"""

    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

    daoValidator = new ConfigurationParserDaoValidatorImpl()

    parser = new ConfigurationParser(daoValidator);
  }

  @Test
  void parseConfig()
  {
    eventReader.nextTag()
    parser.parseConfiguration eventReader

    assertEquals 11, daoValidator.count
  }
}
