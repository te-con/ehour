package net.rrm.ehour.persistence.timesheet.dao;

import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetCommentId;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 7:36:32 PM
 */
public class TimesheetCommentDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    public TimesheetCommentDaoHibernateImplTest() {
        super("dataset-timesheetcomment.xml");
    }

    @Autowired
    private TimesheetCommentDao timesheetCommentDao;

    @SuppressWarnings("deprecation")
    @Test
    public void shouldGetTimesheetEntriesInRange() {
        TimesheetComment comment = timesheetCommentDao.findById(new TimesheetCommentId(1, new Date(2007 - 1900, Calendar.JANUARY, 7)));

        assertNotNull(comment);
    }

    @Test
    public void shouldDeleteOnUser() {
        int rowCount = timesheetCommentDao.deleteCommentsForUser(1);
        Assert.assertEquals(2, rowCount);
    }
}
