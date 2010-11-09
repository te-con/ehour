package net.rrm.ehour.export.service;

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author thies
 *
 */
@Service("exportService")
public class ExportServiceImpl implements ExportService
{
	private static final Logger LOGGER = Logger.getLogger(ExportServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.export.service.ExportService#exportDatabase()
	 */
	@Override
	public String exportDatabase()
	{
		String xmlDocument = null;

		StringWriter stringWriter = new StringWriter();
		XMLOutputFactory factory = XMLOutputFactory.newInstance();

		try
		{
			XMLStreamWriter writer = factory.createXMLStreamWriter(stringWriter);

			exportDatabase(writer);

			xmlDocument = stringWriter.toString();

		} catch (XMLStreamException e)
		{

			LOGGER.error(e);
		}

		return xmlDocument;
	}

	private void exportDatabase(XMLStreamWriter writer) throws XMLStreamException
	{
		writer.writeStartDocument();
		writer.writeEndDocument();
	}

}
