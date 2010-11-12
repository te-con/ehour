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

class ExportServiceImplTest {
	@Mock
	private ExportDao exportDao;

	private ExportServiceImpl service;

	@Before
	void setUp()
	{
		MockitoAnnotations.initMocks(this);

		service = new ExportServiceImpl();
		service.setExportDao(exportDao);
	}

	@Test
	void shouldProduceXml() {
		def map = ["ASSIGNMENT_ID":1, "ENTRY_DATE":new Date()]
		def rows = [map]

		when(exportDao.findAllTimesheetEntries()).thenReturn(rows);

		String xml = service.exportDatabase();
		System.out.println(xml);
		assertTrue(xml.startsWith("<?xml version="));

	}
}
