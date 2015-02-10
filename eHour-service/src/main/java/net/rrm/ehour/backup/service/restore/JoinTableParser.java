package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.service.backup.BackupEntityLocator;
import net.rrm.ehour.backup.service.backup.BackupJoinTable;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class JoinTableParser {
    private final XMLEventReader xmlReader;
    private final BackupEntityLocator locator;

    public JoinTableParser(XMLEventReader xmlReader, BackupEntityLocator locator) {
        this.xmlReader = xmlReader;
        this.locator = locator;
    }

    public JoinTables parseJoinTables() throws XMLStreamException {
        JoinTables joinTables = new JoinTables();

        while (xmlReader.hasNext()) {
            XMLEvent containerEvent = xmlReader.nextTag();

            if (containerEvent.isEndElement()) {
                break;
            }

            parseJoinTable(joinTables);
        }

        return joinTables;
    }

    private void parseJoinTable(JoinTables joinTables) throws XMLStreamException {
        while (xmlReader.hasNext()) {
            XMLEvent event = xmlReader.nextTag();

            if (event.isEndElement()) {
                break;
            }

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String joinTableName = startElement.getName().getLocalPart();

                BackupJoinTable backupJoinTable = locator.joinTableForName(joinTableName);

                if (backupJoinTable == null) {
                    throw new IllegalArgumentException("Unknown join table name: " + joinTableName);
                } else {
                    String source = startElement.getAttributeByName(new QName(backupJoinTable.getAttributeSource())).getValue();
                    String target = startElement.getAttributeByName(new QName(backupJoinTable.getAttributeTarget())).getValue();

                    joinTables.put(joinTableName, source, target);
                }

                xmlReader.nextTag();
            }
        }
    }
}
