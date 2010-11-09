package net.rrm.ehour.export.service;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import net.rrm.ehour.persistence.export.dao.ExportDao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ExportDao exportDao;

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

		writeTimesheetEntries(writer);

		writer.writeEndDocument();
	}

	private void writeTimesheetEntries(XMLStreamWriter writer) throws XMLStreamException
	{
		writer.writeStartElement("TIMESHEET_ENTRIES");

		List<Map<String, Object>> rows = exportDao.findAllTimesheetEntries();

		for (Map<String, Object> rowMap : rows)
		{
			writer.writeStartElement("TIMESHEET_ENTRY");

			for (Entry<String, Object> columns : rowMap.entrySet())
			{
				writer.writeStartElement(columns.getKey());
				writer.writeCharacters(columns.getValue().toString());
				writer.writeEndElement();
			}

			writer.writeEndElement();
		}
	}

	public void setExportDao(ExportDao exportDao)
	{
		this.exportDao = exportDao;
	}
}
