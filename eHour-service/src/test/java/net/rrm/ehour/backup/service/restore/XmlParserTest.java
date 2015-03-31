package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.config.ConfigurationItem;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.StringReader;

import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: thies
 * Date: 1/16/11
 * Time: 2:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class XmlParserTest {

    private XMLEventReader eventReader;

    @Mock
    private ConfigurationDao configurationDao;

    @Test
    public void shouldMatch084On083() throws Exception {
        MockitoAnnotations.initMocks(this);

        Configuration config = new Configuration("version", "0.8.4");

        when(configurationDao.findById(ConfigurationItem.VERSION.getDbField())).thenReturn(config);

        String xmlData = "<?xml version=\"1.0\" ?>\n<EHOUR DB_VERSION=\"0.8.3\"></EHOUR>";

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

        ParseContext ctx = new ParseContext(configurationDao, null, null, null, null, false);
        XmlParser importer = new XmlParser(ctx);
        importer.parseXml(new ParseSession(), eventReader);
    }

    @Test(expected = ImportException.class)
    public void shouldNotMatch084On081() throws Exception {
        MockitoAnnotations.initMocks(this);

        Configuration config = new Configuration("version", "0.8.4");

        when(configurationDao.findById(ConfigurationItem.VERSION.getDbField())).thenReturn(config);

        String xmlData = "<?xml version=\"1.0\" ?>\n<EHOUR DB_VERSION=\"0.8.1\"></EHOUR>";

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

        ParseContext ctx = new ParseContext(configurationDao, null, null, null, null, false);
        XmlParser importer = new XmlParser(ctx);
        importer.parseXml(new ParseSession(), eventReader);
    }
}
