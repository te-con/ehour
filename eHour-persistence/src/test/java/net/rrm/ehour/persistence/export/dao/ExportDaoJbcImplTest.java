package net.rrm.ehour.persistence.export.dao;

import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


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
		List<Map<String, Object>> list = exportDao.findForType(ExportType.TIMESHEET_ENTRY);

		assertEquals(12, list.size());
	}
}
