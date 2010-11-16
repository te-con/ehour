package net.rrm.ehour.persistence.export.dao

import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import static junit.framework.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 4:47:00 PM
 */
class ExportDaoJbcImplTest  extends AbstractAnnotationDaoTest
{
	@Autowired
	private	ExportDao exportDao;

	ExportDaoJbcImplTest()
	{
		super("dataset-timesheet.xml")
	}

	@Test
	void shouldFindAllTimesheetEntries()
	{
		def list = exportDao.findForType(ExportType.TIMESHEET_ENTRY)

		assertEquals(12, list.size())
	}
}
