package net.rrm.ehour.persistence.mail.dao

import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 4:48:38 PM
 */
class MailLogDaoAssignmentHibernateImplTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private MailLogAssignmentDao mailLogAssignmentDao

    MailLogDaoAssignmentHibernateImplTest()
	{
		super("dataset-maillog.xml")
	}

	@Test
	final void shouldFindMailLogOnAssignmentId()
	{
		def mla = mailLogAssignmentDao.findMailLogOnAssignmentIds([2] as Integer[])

		assertEquals(1, mla.size())
		assertEquals(9, mla[0].mailLogId)
	}
}
