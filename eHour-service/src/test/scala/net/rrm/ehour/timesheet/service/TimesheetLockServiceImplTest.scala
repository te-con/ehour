package net.rrm.ehour.timesheet.service


import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.matchers.ClassicMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import net.rrm.ehour.persistence.timesheetlock.dao.TimesheetLockDao
import org.joda.time.LocalDate
import net.rrm.ehour.domain.TimesheetLock
import java.util

@RunWith(classOf[JUnitRunner])
class TimesheetLockServiceImplTest extends  ClassicMatchers with MockitoSugar with WordSpec with BeforeAndAfterEach with BeforeAndAfterAll {
  val repository = mock[TimesheetLockDao]
  val service = new TimesheetLockServiceImpl(repository)

  override def beforeEach() {
    reset(repository)
  }

  "Timesheet Lock Service" should {
    "create a new lock" in {
      val endDate = new LocalDate()
      val startDate = new LocalDate()

      val lock = new TimesheetLock(startDate.toDateMidnight.toDate, endDate.toDateMidnight.toDate)

      when(repository.persist(any(classOf[TimesheetLock]))).thenReturn(lock)

      val timesheet = service.createNew(startDate, endDate)

      assert(timesheet.startDate.equals(startDate))
    }

    "find all" in {
      val endDate = new LocalDate()
      val startDate = new LocalDate()

      val lock = new TimesheetLock(startDate.toDateMidnight.toDate, endDate.toDateMidnight.toDate)
      val lock2 = new TimesheetLock(startDate.plusDays(1).toDateMidnight.toDate, endDate.plusDays(2).toDateMidnight.toDate)

      when(repository.findAll()).thenReturn(util.Arrays.asList(lock, lock2))

      val lockedTimesheets: List[LockedTimesheet] = service.findAll()

      assert(lockedTimesheets.length == 2)
      assert(lockedTimesheets(0).startDate == startDate)
      assert(lockedTimesheets(1).startDate == startDate.plusDays(1))

    }
  }
}
