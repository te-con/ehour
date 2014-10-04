package net.rrm.ehour.backup.service.restore;

import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 11/30/10 12:11 AM
 */
public class ConfigurationParserTest {

    private ConfigurationParserDaoValidatorImpl daoValidator;
    private ConfigurationParser parser;
    private XMLEventReader eventReader;

    @Before
    public void setUp() throws XMLStreamException {
        String xmlData = "<CONFIGURATION>\n  <CONFIG KEY=\"completeDayHours\">8</CONFIG>\n  <CONFIG KEY=\"showTurnOver\">true</CONFIG>\n" +
                "  <CONFIG KEY=\"localeLanguage\">en</CONFIG>\n  <CONFIG KEY=\"currency\">Euro</CONFIG>\n  " +
                "<CONFIG KEY=\"localeCountry \"></CONFIG>\n  <CONFIG KEY=\"availableTranslations\">en,nl,fr,it</CONFIG>\n" +
                "<CONFIG KEY=\"mailFrom \">noreply@localhost.net</CONFIG>\n" +
                "<CONFIG KEY=\"mailSmtp\">127.0.0.1</CONFIG>\n  <CONFIG KEY=\"demoMode \">false</CONFIG>\n" +
                "<CONFIG KEY=\"version\">0.8.3</CONFIG>\n  <CONFIG KEY=\"smtpPort \">25</CONFIG>\n </CONFIGURATION>";

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

        daoValidator = new ConfigurationParserDaoValidatorImpl();

        parser = new ConfigurationParser(daoValidator);
    }

    @Test
    public void parseConfig() throws XMLStreamException {
        eventReader.nextTag();
        parser.parseConfiguration(eventReader);

        assertEquals(11, daoValidator.getCount());
    }

}
