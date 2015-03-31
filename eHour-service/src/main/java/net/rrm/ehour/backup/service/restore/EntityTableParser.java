package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.domain.DomainObject;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class EntityTableParser {
    private static final Logger LOG = Logger.getLogger(EntityTableParser.class);

    private final XMLEventReader xmlReader;
    private final EntityParser entityParser;

    public EntityTableParser(XMLEventReader xmlReader, EntityParser entityParser) {
        this.xmlReader = xmlReader;
        this.entityParser = entityParser;
    }

    public void parseEntityTables(JoinTables joinTables, ParseSession session) throws XMLStreamException, ClassNotFoundException, ImportException, InstantiationException, IllegalAccessException {
        LOG.info("Entity tables found, parsing");

        while (xmlReader.hasNext()) {
            XMLEvent containerEvent = xmlReader.nextTag();

            if (containerEvent.isEndElement()) {
                break;
            }

            session.eventProgressed();

            parseEntity(joinTables, session, containerEvent);
        }

        LOG.info("Entity tables parsed");
    }

    @SuppressWarnings("unchecked")
    private void parseEntity(JoinTables joinTables, ParseSession status, XMLEvent event) throws XMLStreamException, InstantiationException, IllegalAccessException, ClassNotFoundException, ImportException {
        StartElement element = event.asStartElement();
        Attribute attribute = element.getAttributeByName(new QName("CLASS"));

        if (attribute != null) {
            try {
                String aClass = attribute.getValue();

                Class<? extends DomainObject> doClass = (Class<? extends DomainObject>) Class.forName(aClass);

                entityParser.parse(doClass, joinTables, status);
            } catch (Exception e) {
                LOG.warn("element " + attribute.getValue() + " threw " + e.getMessage(), e);
                throw new ImportException("element " + attribute.getValue() + " threw " + e.getMessage(), e);
            }
        } else {
            throw new ImportException("Invalid XML, no attribute found for element: " + element.getName().getLocalPart());
        }
    }
}
