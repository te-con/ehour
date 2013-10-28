package net.rrm.ehour.persistence.timesheetlock.dao

import net.rrm.ehour.domain.TimesheetLock
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class TimesheetLockHibernateImplTest extends AbstractAnnotationDaoTest{
    @Autowired
    private TimesheetLockDao timesheetLockDao;

    @Test
    void shouldPersist()
    {
        def startDate = new Date()
        def timesheetLock = new TimesheetLock(startDate, new Date())
        def id = timesheetLockDao.persist(timesheetLock)
        assertNotNull(id)

        def persistedLock = timesheetLockDao.findById(id.lockId)
        assertEquals(startDate, persistedLock.dateStart)
    }
}
