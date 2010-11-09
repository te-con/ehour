package net.rrm.ehour.persistence.export.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class ExportDaoJbcImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private	ExportDao exportDao;

	public ExportDaoJbcImplTest()
	{
		super("dataset-timesheet.xml");
	}

	@Test
	public void shouldFindAllTimesheetEntries()
	{
		List<Map<String, Object>> list = exportDao.findAllTimesheetEntries();

		assertEquals(12, list.size());
	}
}
