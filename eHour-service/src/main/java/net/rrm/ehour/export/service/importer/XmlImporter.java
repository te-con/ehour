package net.rrm.ehour.export.service.importer;

import net.rrm.ehour.config.ConfigurationItem;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.export.service.ExportElements;
import net.rrm.ehour.export.service.ImportException;
import net.rrm.ehour.export.service.ParseSession;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/6/10 - 3:40 PM
 */
public class XmlImporter
{
    private ConfigurationDao configurationDao;
    private DomainObjectParser domainObjectParser;
    private ConfigurationParser configurationParser;
    private UserRoleParser userRoleParser;

    public XmlImporter(ConfigurationDao configurationDao, DomainObjectParser domainObjectParser, ConfigurationParser configurationParser, UserRoleParser userRoleParser)
    {
        this.configurationDao = configurationDao;
        this.domainObjectParser = domainObjectParser;
        this.configurationParser = configurationParser;
        this.userRoleParser = userRoleParser;
    }

    public void importXml(ParseSession status, XMLEventReader eventReader)
            throws XMLStreamException, ImportException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
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
                        parseElement(startElement, domainObjectParser);
                        break;
                    default:
                        break;
                }
            } else if (event.isEndDocument() || event.isEndElement())
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
}
