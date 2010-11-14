package net.rrm.ehour.export.service;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.StringReader;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 5:34:24 PM
 */
@Service("importService")
public class ImportServiceImpl implements ImportService
{
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public boolean prepareImportDatabase(String xmlData) throws ImportException
    {
        try
        {
            parseXml(xmlData);
        } catch (XMLStreamException e)
        {
            throw new ImportException("import.error.failedToParse", e);
        }

        return true;
    }

    private void parseXml(String xmlData) throws XMLStreamException, ImportException
    {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

        while (eventReader.hasNext())
        {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement())
            {
                StartElement startElement = event.asStartElement();

                String startName = startElement.getName().getLocalPart();

                if (startName.equalsIgnoreCase(ExportElements.EHOUR.name()))
                {
                    checkDatabaseVersion(startElement);
                }
            }
        }
    }

    private void checkDatabaseVersion(StartElement startElement) throws ImportException
    {
        Attribute attribute = startElement.getAttributeByName(new QName(ExportElements.DB_VERSION.name()));
        String dbVersion = attribute.getValue();

        EhourConfigStub configuration = configurationService.getConfiguration();

        if (!configuration.getVersion().equalsIgnoreCase(dbVersion))
        {
             throw new ImportException("import.error.invalidDatabaseVersion");
        }


    }

    private void startDocument(XMLEventReader eventReader, XMLEvent event)
    {
        
    }

    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
