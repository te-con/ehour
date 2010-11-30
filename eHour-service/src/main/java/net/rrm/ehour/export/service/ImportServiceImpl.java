package net.rrm.ehour.export.service;

import net.rrm.ehour.config.ConfigurationItem;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.export.service.element.ConfigurationDaoWrapperValidatorImpl;
import net.rrm.ehour.export.service.element.ConfigurationParser;
import net.rrm.ehour.export.service.element.DomainObjectParser;
import net.rrm.ehour.export.service.element.DomainObjectParserDaoValidatorImpl;
import net.rrm.ehour.export.service.element.UserRoleParser;
import net.rrm.ehour.export.service.element.UserRoleParserDaoValidatorImpl;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.apache.log4j.Logger;
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
    private static final Logger LOG = Logger.getLogger(ImportServiceImpl.class);

    @Autowired
    private ConfigurationDao configurationDao;

    @Override
    public ParseStatus prepareImportDatabase(String xmlData) throws ImportException
    {
        try
        {
            return validateXml(xmlData);
        } catch (Exception e)
        {
            LOG.error(e);
            throw new ImportException("import.error.failedToParse", e);
        }
    }

    private ParseStatus validateXml(String xmlData) throws XMLStreamException, ImportException, IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        ParseStatus status = new ParseStatus();

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

        DomainObjectParserDaoValidatorImpl daoValidator = new DomainObjectParserDaoValidatorImpl();
        DomainObjectParser parser = new DomainObjectParser(eventReader, daoValidator, status);

        ConfigurationDaoWrapperValidatorImpl configValidator = new ConfigurationDaoWrapperValidatorImpl();
        ConfigurationParser configurationParser = new ConfigurationParser(configValidator);

        UserRoleParserDaoValidatorImpl userRoleValidator = new UserRoleParserDaoValidatorImpl();
        UserRoleParser userRoleParser = new UserRoleParser(userRoleValidator);

        while (eventReader.hasNext())
        {
            XMLEvent event = eventReader.nextTag();

            if (event.isStartElement())
            {
                StartElement startElement = event.asStartElement();

                String startName = startElement.getName().getLocalPart();

                ExportElements element = safelyGetExportElements(startName);

                switch (element)
                {
                    case EHOUR:
                        checkDatabaseVersion(startElement);
                        break;
                    case CONFIGURATION:
                        configurationParser.parseConfiguration(eventReader);
                        break;
                    case USER_TO_USERROLES:
                        userRoleParser.parseUserRoles(eventReader, status);
                        break;
                    case OTHER:
                        parseElement(startElement, parser);
                        break;
                    default:
                        break;
                }
            } else if (event.isEndDocument() || event.isEndElement())
            {
                break;
            }
        }

        return status;
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

    @SuppressWarnings("unchecked")
    private void parseElement(StartElement element, DomainObjectParser parser) throws XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        String aClass = element.getAttributeByName(new QName("CLASS")).getValue();

        Class<? extends DomainObject> doClass = (Class<? extends DomainObject>) Class.forName(aClass);

        parser.parse(doClass);
    }

    private void checkDatabaseVersion(StartElement element) throws ImportException
    {
        Attribute attribute = element.getAttributeByName(new QName(ExportElements.DB_VERSION.name()));
        String dbVersion = attribute.getValue();

        Configuration version = configurationDao.findById(ConfigurationItem.VERSION.getDbField());

        if (version == null || !version.getConfigValue().equalsIgnoreCase(dbVersion))
        {
            throw new ImportException("import.error.invalidDatabaseVersion");
        }
    }

    public void setConfigurationDao(ConfigurationDao configurationDao)
    {
        this.configurationDao = configurationDao;
    }
}
