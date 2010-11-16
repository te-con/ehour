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
import java.lang.reflect.Method;

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
            validateXml(xmlData);
        } catch (XMLStreamException e)
        {
            throw new ImportException("import.error.failedToParse", e);
        }

        return true;
    }

    private void validateXml(String xmlData) throws XMLStreamException, ImportException
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

                ExportElements element = ExportElements.valueOf(startName);

                switch (element)
                {
                    case EHOUR:
                        checkDatabaseVersion(startElement);
                        break;
                    case CONFIGURATION:
                        parseConfiguration(startElement, eventReader);
                        break;
                    default:
                        parseElement(startElement, eventReader);
                        break;
                }
            }
        }
    }

    private void parseElement(StartElement startElement, XMLEventReader reader)
    {
        String startName = startElement.getName().getLocalPart();

//        Class<? extends DomainObject<?, ?>> domainObjectClass = CLASS_MAP.get(startName);

//        if (ExportType.valueOf()

    }

    private void parseConfiguration(StartElement element, XMLEventReader eventReader) throws XMLStreamException
    {
        EhourConfigStub config = new EhourConfigStub();

        while (eventReader.hasNext())
        {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement())
            {
                parseConfigElement(eventReader, event);
            } else if (event.isEndElement())
            {
                return;
            }
        }
    }

    private void parseConfigElement(XMLEventReader eventReader, XMLEvent event)
            throws XMLStreamException
    {
        String key = null;
        String value = null;

        StartElement startElement = event.asStartElement();
        key = startElement.getAttributeByName(new QName(ExportElements.KEY.name())).getValue();

        while (eventReader.hasNext())
        {
            event = eventReader.nextEvent();

            if (event.isCharacters())
            {
                value = event.asCharacters().getData();
            } else if (event.isEndElement())
            {
                break;
            }
        }
    }

    private void checkDatabaseVersion(StartElement element) throws ImportException
    {
        Attribute attribute = element.getAttributeByName(new QName(ExportElements.DB_VERSION.name()));
        String dbVersion = attribute.getValue();

        EhourConfigStub configuration = configurationService.getConfiguration();

        if (!configuration.getVersion().equalsIgnoreCase(dbVersion))
        {
            throw new ImportException("import.error.invalidDatabaseVersion");
        }
    }

    private void fillConfiguration(EhourConfigStub targetObject, StartElement element)
    {
        Class<? extends Object> targetClass = targetObject.getClass();
        Method[] methods = targetClass.getDeclaredMethods();

        for (Method method : methods)
        {
            Class<?>[] parameters = method.getParameterTypes();

        }

    }

    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
