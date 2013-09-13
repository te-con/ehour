package net.rrm.ehour.persistence.timesheet.dao

import net.rrm.ehour.domain.TimesheetCommentId
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 7:36:32 PM
 */
class TimesheetCommentDaoHibernateImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private TimesheetCommentDao	timesheetCommentDao;

	TimesheetCommentDaoHibernateImplTest()
	{
		super("dataset-timesheetcomment.xml")
	}

	@Test
	void shouldGetTimesheetEntriesInRange()
	{
		def comment = timesheetCommentDao.findById(new TimesheetCommentId(1, new Date(2007 - 1900, 1 - 1, 7)));

		assertNotNull(comment);
	}

	@Test
	 void shouldDeleteOnUser()
	{
		int rowCount = timesheetCommentDao.deleteCommentsForUser(1);
		assertEquals(2, rowCount);
	}
}