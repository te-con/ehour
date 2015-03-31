package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.domain.ExportElements;
import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.config.ConfigurationItem;
import net.rrm.ehour.domain.Configuration;
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
    private final ParseContext ctx;

    public XmlParser(ParseContext ctx) {
        this.ctx = ctx;
    }

    public void parseXml(final ParseSession session, final XMLEventReader eventReader) throws Exception {
        JoinTables joinTables = new JoinTables();

        session.start();

        while (eventReader.hasNext()) {
            session.eventProgressed();
            final XMLEvent event = eventReader.nextTag();

            if (event.isStartElement()) {
                parseEvent(session, eventReader, event, joinTables);
            } else if (event.isEndDocument() || event.isEndElement()) {
                break;
            }
        }

        session.finish();
    }

    private void parseEvent(ParseSession session, XMLEventReader eventReader, XMLEvent event, JoinTables joinTables)
            throws ImportException, XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        StartElement startElement = event.asStartElement();

        String startName = startElement.getName().getLocalPart();

        ExportElements element = safelyGetExportElements(startName);

        LOG.info("Element found in backup file: " + element.name() + " = " + startName);

        switch (element) {
            case EHOUR:
                if (!ctx.skipValidation) {
                    checkDatabaseVersion(startElement);
                }
                break;
            case CONFIGURATION:
                ctx.configurationParser.parseConfiguration(eventReader);
                break;
            case JOIN_TABLES:
                parseJoinTables(joinTables);
                break;
            case ENTITY_TABLES:
                parseEntityTables(joinTables, session);
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

    private JoinTables parseJoinTables(JoinTables joinTables) throws XMLStreamException {
        return ctx.joinTableParser.parseJoinTables(joinTables);
    }

    private void parseEntityTables(JoinTables joinTables, ParseSession session) throws XMLStreamException, ClassNotFoundException, ImportException, InstantiationException, IllegalAccessException {
        ctx.entityTableParser.parseEntityTables(joinTables, session);
    }

    private void checkDatabaseVersion(StartElement element) throws ImportException {
        Attribute attribute = element.getAttributeByName(new QName(ExportElements.DB_VERSION.name()));
        String dbVersion = attribute.getValue();

        Configuration version = ctx.configurationDao.findById(ConfigurationItem.VERSION.getDbField());

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
