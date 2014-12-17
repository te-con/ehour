package net.rrm.ehour.persistence.timesheet.dao;

import com.google.common.collect.Lists;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetCommentId;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

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
    public void should_get_timesheet_comments_for_date() {
        TimesheetComment comment = timesheetCommentDao.findById(new TimesheetCommentId(1, new Date(2007 - 1900, Calendar.JANUARY, 7)));
        assertNotNull(comment);
    }

    @Test
    public void should_delete_comments_for_user() {
        int rowCount = timesheetCommentDao.deleteCommentsForUser(1);
        assertEquals(2, rowCount);
    }

    @Test
    public void should_find_entries_in_range() {
        Interval i = new Interval(new LocalDate(2007, DateTimeConstants.JANUARY, 13).toDateTimeAtCurrentTime(),
                new LocalDate(2007, DateTimeConstants.JANUARY, 22).toDateTimeAtCurrentTime());

        List<TimesheetComment> comments = timesheetCommentDao.findCommentBetween(new DateRange(i));

        assertEquals(2, comments.size());
        assertThat(comments.get(0).getCommentId().getCommentDate().toString(), startsWith("2007-01-14"));
        assertThat(comments.get(1).getCommentId().getCommentDate().toString(), startsWith("2007-01-21"));
    }

    @Test
    public void should_find_entries_in_range_for_users() {
        Interval i = new Interval(new LocalDate(2007, DateTimeConstants.JANUARY, 1).toDateTimeAtCurrentTime(),
                new LocalDate(2007, DateTimeConstants.JANUARY, 30).toDateTimeAtCurrentTime());

        List<TimesheetComment> comments = timesheetCommentDao.findCommentBetweenForUsers(Lists.newArrayList(2, 3), new DateRange(i));
        assertEquals(2, comments.get(0).getCommentId().getUserId().intValue());
        assertEquals(3, comments.get(1).getCommentId().getUserId().intValue());
    }
}
