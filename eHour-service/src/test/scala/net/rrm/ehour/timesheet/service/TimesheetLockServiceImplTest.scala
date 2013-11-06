package net.rrm.ehour.timesheet.service


import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest._
import net.rrm.ehour.persistence.timesheetlock.dao.TimesheetLockDao
import org.joda.time.LocalDate
import net.rrm.ehour.domain.TimesheetLock
import java.util
import java.{util => ju}

@RunWith(classOf[JUnitRunner])
class TimesheetLockServiceImplTest extends WordSpec with Matchers with MockitoSugar with BeforeAndAfterEach  {
  val repository = mock[TimesheetLockDao]
  val service = new TimesheetLockServiceImpl(repository)

  val endDate = new LocalDate()
  val startDate = new LocalDate()

  val lock = new TimesheetLock(startDate.toDate, endDate.toDate)

  override def beforeEach() {
    reset(repository)
  }

  "Timesheet Lock Service" should {
    "create a new lock" in {
      when(repository.persist(any(classOf[TimesheetLock]))).thenReturn(lock)

      val timesheet = service.createNew(startDate, endDate)

      timesheet.dateStart should be (startDate)
    }

    "find all" in {
      val lock2 = new TimesheetLock(startDate.plusDays(1).toDate, endDate.plusDays(2).toDate)

      when(repository.findAll()).thenReturn(util.Arrays.asList(lock, lock2))

      val lockedTimesheets: List[LockedTimesheet] = service.findAll()

      lockedTimesheets.length should be (2)
      lockedTimesheets(0).dateStart should be (startDate)
      lockedTimesheets(1).dateStart should be (startDate.plusDays(1))
    }

    "find on id and return some timesheet" in {
      when(repository.findById(2)).thenReturn(lock)

      val lockedTimesheet = service.find(2)

      lockedTimesheet should be ('defined)
    }

    "find on id and return none when nothing is found" in {
      when(repository.findById(2)).thenReturn(null)

      val lockedTimesheet = service.find(2)

      lockedTimesheet should not be 'defined
    }

    "find no locked days when there are none" in {
      val locked = service.findLockedDatesInRange(LocalDate.parse("20130101").toDate, LocalDate.parse("20130108").toDate)

      locked should not be 'defined
    }

    "find first 2 days as locked" in {
      val startDate = LocalDate.parse("2013-01-01")
      val endDate = LocalDate.parse("2013-01-08")

      val response = ju.Arrays.asList(new TimesheetLock(startDate.toDate, startDate.plusDays(2).toDate))
      when(repository.findMatchingLock(startDate.toDate, endDate.toDate)).thenReturn(response)

      val locked = service.findLockedDatesInRange(startDate.toDate, endDate.toDate)

      locked.get.getStart.toLocalDate should be (startDate)
      locked.get.getEnd.toLocalDate should be (startDate.plusDays(2))
    }

    "find 3 days as locked" in {
      val startDate = LocalDate.parse("2013-01-01")
      val endDate = LocalDate.parse("2013-01-08")

      val response = ju.Arrays.asList(new TimesheetLock(startDate.toDate, startDate.plusDays(2).toDate), new TimesheetLock(endDate.minusDays(1).toDate, endDate.toDate))
      when(repository.findMatchingLock(startDate.toDate, endDate.toDate)).thenReturn(response)

      val locked = service.findLockedDatesInRange(startDate.toDate, endDate.toDate)

      locked.get.getStart.toLocalDate should be (startDate)
      locked.get.getEnd.toLocalDate should be (startDate.plusDays(2))

      // TODO broken
    }
  }
}
