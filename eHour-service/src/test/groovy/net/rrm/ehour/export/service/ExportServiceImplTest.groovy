package net.rrm.ehour.export.service;
import static org.mockito.Mockito.when;

import java.util.Date;


import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.config.service.ConfigurationService

import net.rrm.ehour.persistence.export.dao.ExportDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

class ExportServiceImplTest {
	@Mock
	private ExportDao exportDao;

	@Mock
	private ConfigurationService configurationService

	private ExportServiceImpl service;

	@Before
	void setUp()
	{
		MockitoAnnotations.initMocks(this);

		service = new ExportServiceImpl(exportDao: exportDao, configurationService: configurationService);
	}

	@Test
	void shouldProduceXml() {
		def map = ["ASSIGNMENT_ID":1, "ENTRY_DATE":new Date()]
		def rows = [map]

		when(exportDao.findAllTimesheetEntries()).thenReturn(rows);

		def configuration = new EhourConfigStub(version:0.9)

		when(configurationService.getConfiguration()).thenReturn(configuration);

		String xml = service.exportDatabase()

		assertTrue(xml.startsWith("<?xml version="));
	}
}
