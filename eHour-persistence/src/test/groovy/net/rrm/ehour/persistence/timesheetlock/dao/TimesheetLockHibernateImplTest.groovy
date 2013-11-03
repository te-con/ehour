package net.rrm.ehour.persistence.timesheetlock.dao

import net.rrm.ehour.domain.TimesheetLock
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.joda.time.DateTime
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.*

class TimesheetLockHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private TimesheetLockDao timesheetLockDao;

    TimesheetLockHibernateImplTest() {
        super("dataset-timesheetlock.xml")
    }

    @Test
    void shouldPersist() {
        def startDate = new Date()
        def timesheetLock = new TimesheetLock(startDate, new Date())
        def id = timesheetLockDao.persist(timesheetLock)
        assertNotNull(id)

        def persistedLock = timesheetLockDao.findById(id.lockId)
        assertEquals(startDate, persistedLock.dateStart)
    }

    @Test
    void shouldFindMatching() {
        def start = new DateTime(2013, 11, 3, 0, 0, 0, 0)
        def locks = timesheetLockDao.findMatchingLock(start.plusDays(1).toDate(), start.plusDays(3).toDate())

        assertEquals(1, locks[0].lockId)
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



}
