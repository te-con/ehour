package net.rrm.ehour.persistence.timesheetlock.dao

import com.google.common.collect.Lists
import net.rrm.ehour.domain.TimesheetLock
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import net.rrm.ehour.persistence.user.dao.UserDao
import org.joda.time.DateTime
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.*

class TimesheetLockDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private TimesheetLockDao timesheetLockDao;

    @Autowired
    private UserDao userDao;

    TimesheetLockDaoHibernateImplTest() {
        super("dataset-timesheetlock.xml")
    }

    @Test
    void shouldPersistWithoutExclusions() {
        def startDate = new Date()
        def timesheetLock = new TimesheetLock(startDate, new Date())
        def id = timesheetLockDao.persist(timesheetLock)
        assertNotNull(id)

        def persistedLock = timesheetLockDao.findById(id.lockId)
        assertEquals(startDate, persistedLock.dateStart)
    }

    @Test
    void shouldPersistWithExclusions() {
        def startDate = new Date()
        def timesheetLock = new TimesheetLock(startDate, new Date())

        timesheetLock.excludedUsers = Lists.newArrayList(UserObjectMother.createUser())
        def id = timesheetLockDao.persist(timesheetLock)
        assertNotNull(id)

        def persistedLock = timesheetLockDao.findById(id.lockId)
        assertEquals(startDate, persistedLock.dateStart)
    }

    @Test
    void shouldFindMatching() {
        def start = new DateTime(2013, 11, 3, 0, 0, 0, 0)
        def locks = timesheetLockDao.findMatchingLock(start.plusDays(1).toDate(), start.plusDays(3).toDate())

        assertEquals(10, locks[0].lockId)
    }

    @Test
    void shouldNotFindMatchingBefore() {
        def start = new DateTime(2013, 10, 3, 0, 0, 0, 0)
        def locks = timesheetLockDao.findMatchingLock(start.plusDays(1).toDate(), start.plusDays(3).toDate())

        assertTrue(locks.isEmpty())
    }

    @Test
    void shouldNotFindMatchingAfter() {
        def start = new DateTime(2013, 12, 3, 0, 0, 0, 0)
        def locks = timesheetLockDao.findMatchingLock(start.plusDays(1).toDate(), start.plusDays(3).toDate())

        assertTrue(locks.isEmpty())
    }

    @Test
    void shouldFindMatchingOverlap() {
        def start = new DateTime(2013, 12, 31, 0, 0, 0, 0)
        def end = new DateTime(2014, 1, 31, 23, 59, 59, 0)
        def locks = timesheetLockDao.findMatchingLock(start.toDate(), end.toDate())

        assertEquals(20, locks[0].lockId)
    }
}
