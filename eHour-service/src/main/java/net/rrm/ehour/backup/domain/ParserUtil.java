package net.rrm.ehour.backup.domain;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:06 AM
 */
public class ParserUtil {
    private ParserUtil() {
    }

    public static String parseNextEventAsCharacters(XMLEventReader eventReader) throws XMLStreamException {
        // no chars can only mean an end importer
        StringBuilder data = new StringBuilder();
        XMLEvent charEvent;

        while ((charEvent = eventReader.nextEvent()).isCharacters()) {
            data.append(charEvent.asCharacters().getData());
        }

        return data.toString();
    }
}
