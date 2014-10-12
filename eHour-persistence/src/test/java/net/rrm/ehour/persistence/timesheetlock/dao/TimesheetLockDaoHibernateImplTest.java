package net.rrm.ehour.persistence.timesheetlock.dao;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.TimesheetLock;
import net.rrm.ehour.domain.UserObjectMother;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TimesheetLockDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private TimesheetLockDao timesheetLockDao;

    public TimesheetLockDaoHibernateImplTest() {
        super("dataset-timesheetlock.xml");
    }

    @Test
    public void shouldPersistWithoutExclusions() {
        Date startDate = new Date();
        TimesheetLock timesheetLock = new TimesheetLock(startDate, new Date());
        TimesheetLock id = timesheetLockDao.persist(timesheetLock);
        Assert.assertNotNull(id);

        TimesheetLock persistedLock = timesheetLockDao.findById(id.getLockId());
        assertEquals(startDate, persistedLock.getDateStart());
    }

    @Test
    public void shouldPersistWithExclusions() {
        Date startDate = new Date();
        TimesheetLock timesheetLock = new TimesheetLock(startDate, new Date());

        timesheetLock.setExcludedUsers(Lists.newArrayList(UserObjectMother.createUser()));
        TimesheetLock id = timesheetLockDao.persist(timesheetLock);
        Assert.assertNotNull(id);

        TimesheetLock persistedLock = timesheetLockDao.findById(id.getLockId());
        assertEquals(startDate, persistedLock.getDateStart());
    }

    @Test
    public void shouldFindMatching() {
        DateTime start = new DateTime(2013, DateTimeConstants.NOVEMBER, 3, 0, 0, 0, 0);
        List<TimesheetLock> locks = timesheetLockDao.findMatchingLock(start.plusDays(1).toDate(), start.plusDays(3).toDate());

        assertEquals(10, locks.get(0).getLockId().intValue());
    }

    @Test
    public void shouldNotFindMatchingBefore() {
        DateTime start = new DateTime(2013, DateTimeConstants.OCTOBER, 3, 0, 0, 0, 0);
        List<TimesheetLock> locks = timesheetLockDao.findMatchingLock(start.plusDays(1).toDate(), start.plusDays(3).toDate());

        Assert.assertTrue(locks.isEmpty());
    }

    @Test
    public void shouldNotFindMatchingAfter() {
        DateTime start = new DateTime(2013, DateTimeConstants.DECEMBER, 3, 0, 0, 0, 0);
        List<TimesheetLock> locks = timesheetLockDao.findMatchingLock(start.plusDays(1).toDate(), start.plusDays(3).toDate());

        Assert.assertTrue(locks.isEmpty());
    }

    @Test
    public void shouldFindMatchingOverlap() {
        DateTime start = new DateTime(2013, DateTimeConstants.DECEMBER, 31, 0, 0, 0, 0);
        DateTime end = new DateTime(2014, DateTimeConstants.JANUARY, 31, 23, 59, 59, 0);
        List<TimesheetLock> locks = timesheetLockDao.findMatchingLock(start.toDate(), end.toDate());

        assertEquals(20, locks.get(0).getLockId().intValue());
    }

}
