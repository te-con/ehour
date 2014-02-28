package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.domain.ExportElements;
import net.rrm.ehour.backup.domain.ParserUtil;
import net.rrm.ehour.domain.Configuration;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:19 AM
 */
public class ConfigurationParser
{
    private ConfigurationParserDao parserDao;

    public ConfigurationParser(ConfigurationParserDao parserDao)
    {
        this.parserDao = parserDao;
    }

    public void parseConfiguration(XMLEventReader eventReader) throws XMLStreamException
    {
        while (eventReader.hasNext())
        {
            XMLEvent event = eventReader.nextTag();

            if (event.isStartElement())
            {
                Configuration configuration = parseConfigElement(eventReader, event);
                parserDao.persist(configuration);
            } else if (event.isEndElement())
            {
                return;
            }
        }
    }

    private Configuration parseConfigElement(XMLEventReader eventReader, XMLEvent event)
            throws XMLStreamException
    {
        StartElement startElement = event.asStartElement();
        String key = startElement.getAttributeByName(new QName(ExportElements.KEY.name())).getValue();

        String value = ParserUtil.parseNextEventAsCharacters(eventReader);

        return new Configuration(key, value);
    }
}
