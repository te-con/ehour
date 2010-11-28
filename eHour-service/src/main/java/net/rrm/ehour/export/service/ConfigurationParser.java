package net.rrm.ehour.export.service;

import net.rrm.ehour.config.EhourConfigStub;
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
    private ConfigurationDaoWrapper daoWrapper;

    public ConfigurationParser(ConfigurationDaoWrapper daoWrapper)
    {
        this.daoWrapper = daoWrapper;
    }

    void parseConfiguration(StartElement element, XMLEventReader eventReader) throws XMLStreamException
    {
        EhourConfigStub config = new EhourConfigStub();

        while (eventReader.hasNext())
        {
            XMLEvent event = eventReader.nextTag();

            if (event.isStartElement())
            {
                Configuration configuration = parseConfigElement(eventReader, event, config);
                daoWrapper.persist(configuration);
            } else if (event.isEndElement())
            {
                return;
            }
        }
    }

    private Configuration parseConfigElement(XMLEventReader eventReader, XMLEvent event, EhourConfigStub config)
            throws XMLStreamException
    {
        StartElement startElement = event.asStartElement();
        String key = startElement.getAttributeByName(new QName(ExportElements.KEY.name())).getValue();

        String value = ParserUtil.parseNextEventAsCharacters(eventReader);

        return new Configuration(key, value);
    }
}
