package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.backup.common.BackupJoinTable;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class JoinTableParser {
    private static final Logger LOG = Logger.getLogger(JoinTableParser.class);

    private final XMLEventReader xmlReader;
    private final BackupConfig backupConfig;

    public JoinTableParser(XMLEventReader xmlReader, BackupConfig backupConfig) {
        this.xmlReader = xmlReader;
        this.backupConfig = backupConfig;
    }

    public JoinTables parseJoinTables(JoinTables joinTables) throws XMLStreamException {
        LOG.info("Join tables found, parsing");

        while (xmlReader.hasNext()) {
            XMLEvent containerEvent = xmlReader.nextTag();

            if (containerEvent.isEndElement()) {
                break;
            }

            parseJoinTable(joinTables);
        }

        LOG.info("Join tables parsed");

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

                BackupJoinTable backupJoinTable = backupConfig.joinTableForName(joinTableName);

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
