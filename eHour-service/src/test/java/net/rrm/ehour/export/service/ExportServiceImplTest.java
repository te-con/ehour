package net.rrm.ehour.export.service;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class ExportServiceImplTest
{
	private ExportServiceImpl service;

	@Before
	public void setUp()
	{
		service = new ExportServiceImpl();
	}

	@Test
	public void shouldProduceXml() {
		String xml = service.exportDatabase();
		System.out.println(xml);
		assertTrue(xml.startsWith("<?xml version="));

	}
}
