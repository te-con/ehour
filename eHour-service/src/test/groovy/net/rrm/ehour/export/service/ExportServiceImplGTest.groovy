package net.rrm.ehour.export.service;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.rrm.ehour.persistence.export.dao.ExportDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.Assert.*;

class ExportServiceImplGTest {
	@Mock
	private ExportDao exportDao;

	private ExportServiceImpl service;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		service = new ExportServiceImpl();
		service.setExportDao(exportDao);
	}

	@Test
	public void shouldProduceXml() {
		Map<String, Object> row = new HashMap<String, Object>();

		row.put("ASSIGNMENT_ID", new Integer(1));
		row.put("ENTRY_DATE", new Date());

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		rows.add(row);

		when(exportDao.findAllTimesheetEntries()).thenReturn(rows);

		String xml = service.exportDatabase();
		System.out.println(xml);
		assertTrue(xml.startsWith("<?xml version="));

	}
}
