package net.rrm.ehour.export.service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:06 AM
 */
public class ParserUtil
{
    public static String parseNextEventAsCharacters(XMLEventReader eventReader) throws XMLStreamException
    {
        // no chars can only mean an end importer
        StringBuffer data = new StringBuffer();
        XMLEvent charEvent;

        while ((charEvent = eventReader.nextEvent()).isCharacters())
        {
            data.append(charEvent.asCharacters().getData());
        }

        return data.toString();
    }
}
