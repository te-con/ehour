package net.rrm.ehour.export.service;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.persistence.export.dao.ImportDao;
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

    @Autowired
    private ImportDao importDao;

    @Override
    public boolean prepareImportDatabase(String xmlData) throws ImportException
    {
        try
        {
            validateXml(xmlData);
        } catch (Exception e)
        {
            System.err.println(e);
            e.printStackTrace();
            throw new ImportException("import.error.failedToParse", e);
        }

        return true;
    }

    private void validateXml(String xmlData) throws XMLStreamException, ImportException, IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

        DomainObjectResolver resolver = new DomainObjectResolver(eventReader, importDao);

        while (eventReader.hasNext())
        {
            XMLEvent event = eventReader.nextTag();

            if (event.isStartElement())
            {
                StartElement startElement = event.asStartElement();

                String startName = startElement.getName().getLocalPart();

                System.out.println(startName);

                ExportElements element = safelyGetExportElements(startName);

                switch (element)
                {
                    case EHOUR:
                        checkDatabaseVersion(startElement);
                        break;
                    case CONFIGURATION:
                        parseConfiguration(startElement, eventReader);
                        break;
                    case USER_TO_USERROLES:
                        eventReader.nextTag();
                        // TODO
                        return;
//                                      break;
                    case OTHER:
                    default:
                        parseElement(startElement, eventReader, resolver);
                        break;
                }
            } else if (event.isEndDocument())
            {
                break;
            }
        }
    }

    private ExportElements safelyGetExportElements(String name)
    {
        ExportElements element;

        try
        {
            element = ExportElements.valueOf(name);
        } catch (IllegalArgumentException iae)
        {
            element = ExportElements.OTHER;
        }

        return element;
    }

    private void parseElement(StartElement element, XMLEventReader reader, DomainObjectResolver resolver) throws XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        String aClass = element.getAttributeByName(new QName("CLASS")).getValue();

        Class<? extends DomainObject> doClass = (Class<? extends DomainObject>)Class.forName(aClass);

        resolver.parse(doClass);
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

    public void setImportDao(ImportDao importDao)
    {
        this.importDao = importDao;
    }
}
