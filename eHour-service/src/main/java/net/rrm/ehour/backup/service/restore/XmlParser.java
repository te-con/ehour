package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.domain.ExportElements;
import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.config.ConfigurationItem;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.apache.log4j.Logger;

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
public class XmlParser {
    private static final Logger LOG = Logger.getLogger(XmlParser.class);

    private ConfigurationDao configurationDao;
    private DomainObjectParser domainObjectParser;
    private ConfigurationParser configurationParser;
    private JoinTableParser joinTableParser;
    private UserRoleParser userRoleParser;

    private boolean skipValidation;

    public XmlParser(ConfigurationDao configurationDao, DomainObjectParser domainObjectParser, ConfigurationParser configurationParser, JoinTableParser joinTableParser, UserRoleParser userRoleParser, boolean skipValidation) {
        this.configurationDao = configurationDao;
        this.domainObjectParser = domainObjectParser;
        this.configurationParser = configurationParser;
        this.joinTableParser = joinTableParser;
        this.userRoleParser = userRoleParser;

        this.skipValidation = skipValidation;
    }

    public void parseXml(final ParseSession status, final XMLEventReader eventReader) throws Exception {
        while (eventReader.hasNext()) {
            final XMLEvent event = eventReader.nextTag();

            if (event.isStartElement()) {
                parseEvent(status, eventReader, event);

            } else if (event.isEndDocument() || event.isEndElement()) {
                break;
            }
        }
    }

    private void parseEvent(ParseSession status, XMLEventReader eventReader, XMLEvent event)
            throws ImportException, XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        StartElement startElement = event.asStartElement();

        String startName = startElement.getName().getLocalPart();

        ExportElements element = safelyGetExportElements(startName);

        LOG.info("Element found in backup file: " + element.name() + " = " + startName);

        JoinTables joinTables = null;

        switch (element) {
            case EHOUR:
                if (!skipValidation) {
                    checkDatabaseVersion(startElement);
                }
                break;
            case CONFIGURATION:
                configurationParser.parseConfiguration(eventReader);
                break;
//            case USER_TO_USERROLES:
//                userRoleParser.parseUserRoles(eventReader, status);
//                break;
            case JOIN_TABLES:
                joinTables = parseJoinTables();
                break;
            case ENTITY_TABLES:
                // yes order is important, joinTables should have been parsed first - basically first in the XML
                parseEntity(startElement, domainObjectParser, joinTables, status);
                break;
            default:
                break;
        }
    }

    private ExportElements safelyGetExportElements(String name) {
        try {
            return ExportElements.valueOf(name);
        } catch (IllegalArgumentException iae) {
            return ExportElements.OTHER;
        }
    }

    private JoinTables parseJoinTables() throws XMLStreamException {
        return joinTableParser.parseJoinTables();
    }

    @SuppressWarnings("unchecked")
    private void parseEntity(StartElement element, DomainObjectParser entityParser, JoinTables joinTables, ParseSession status) throws XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException, ImportException {
        Attribute attribute = element.getAttributeByName(new QName("CLASS"));

        if (attribute != null) {
            String aClass = attribute.getValue();

            Class<? extends DomainObject> doClass = (Class<? extends DomainObject>) Class.forName(aClass);

            entityParser.parse(doClass, joinTables, status);
        } else {
            throw new ImportException("Invalid XML, no attribute found for element: " + element.getName().getLocalPart());
        }
    }

    private void checkDatabaseVersion(StartElement element) throws ImportException {
        Attribute attribute = element.getAttributeByName(new QName(ExportElements.DB_VERSION.name()));
        String dbVersion = attribute.getValue();

        Configuration version = configurationDao.findById(ConfigurationItem.VERSION.getDbField());

        isDatabaseCompatible(version.getConfigValue(), dbVersion);
    }

    private void isDatabaseCompatible(String version, String dbVersion) throws ImportException {
        dbVersion = dbVersion != null && dbVersion.equalsIgnoreCase("0.8.3") ? "0.8.4" : dbVersion;
        version = version != null && version.equalsIgnoreCase("0.8.3") ? "0.8.4" : version;

        if (version == null || !version.equalsIgnoreCase(dbVersion)) {
            String foundVersion = version != null ? version : "n/a";

            throw new ImportException("Invalid database version (" + dbVersion + ") specified in file, target database should match backup database version (" + foundVersion + ")");
        }
    }
}
