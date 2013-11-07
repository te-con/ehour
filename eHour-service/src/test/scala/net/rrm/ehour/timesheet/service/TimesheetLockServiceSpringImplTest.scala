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
import java.{util => ju}
import com.github.nscala_time.time.Imports._

@RunWith(classOf[JUnitRunner])
class TimesheetLockServiceSpringImplTest extends WordSpec with Matchers with MockitoSugar with BeforeAndAfterEach {
  val repository = mock[TimesheetLockDao]
  val service = new TimesheetLockServiceSpringImpl(repository)

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

      timesheet.dateStart should be(startDate)
    }

    "find all" in {
      val lock2 = new TimesheetLock(startDate.plusDays(1).toDate, endDate.plusDays(2).toDate)

      when(repository.findAll()).thenReturn(ju.Arrays.asList(lock, lock2))

      val lockedTimesheets: List[LockedTimesheet] = service.findAll()

      lockedTimesheets.length should be(2)
      lockedTimesheets(0).dateStart should be(startDate)
      lockedTimesheets(1).dateStart should be(startDate.plusDays(1))
    }

    "find on id and return some timesheet" in {
      when(repository.findById(2)).thenReturn(lock)

      val lockedTimesheet = service.find(2)

      lockedTimesheet should be('defined)
    }

    "find on id and return none when nothing is found" in {
      when(repository.findById(2)).thenReturn(null)

      val lockedTimesheet = service.find(2)

      lockedTimesheet should not be 'defined
    }

    "find no locked days when there are none" in {
      val locked = service.findLockedDatesInRange(LocalDate.parse("20130101").toDate, LocalDate.parse("20130108").toDate)

      locked should be('empty)
    }

    {
      val startDate = LocalDate.parse("2013-01-01")
      val endDate = LocalDate.parse("2013-01-08")

      def findLockedDatesInRange(response: ju.List[TimesheetLock]) = {
        when(repository.findMatchingLock(startDate.toDate, endDate.toDate)).thenReturn(response)
        val l = service.findLockedDatesInRange(startDate.toDate, endDate.toDate)
        verify(repository).findMatchingLock(startDate.toDate, endDate.toDate)

        l
      }

      "find first 2 days as locked with lock matching requested range" in {
        val response = ju.Arrays.asList(new TimesheetLock(startDate.toDate, startDate.plusDays(2).toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate + 2.days)
      }


      "find first 2 days as locked with lock partially overlapping the requested range" in {
        val response = ju.Arrays.asList(new TimesheetLock(startDate.minusDays(2).toDate, startDate.plusDays(2).toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate + 2.days)
      }

      "find first day as locked when locked abuts the requested range at the start" in {
        val response = ju.Arrays.asList(new TimesheetLock(startDate.minusDays(2).toDate, startDate.toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate)
      }

      "find last 2 days as locked with lock matching the requested range" in {
        val response = ju.Arrays.asList(new TimesheetLock(endDate.minusDays(2).toDate, endDate.toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(endDate - 2.days)
        locked(0).end.toLocalDate should be(endDate)
      }

      "find last 2 days as locked with lock partially overlapping the requested range" in {
        val response = ju.Arrays.asList(new TimesheetLock(endDate.minusDays(2).toDate, endDate.plusDays(2).toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(endDate - 2.days)
        locked(0).end.toLocalDate should be(endDate)
      }

      "find last day locked as end lock is inclusive" in {
        val response = ju.Arrays.asList(new TimesheetLock(endDate.toDate, endDate.plusDays(1).toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(endDate)
        locked(0).end.toLocalDate should be(endDate)
      }

      "find first 2 days and last day as locked" in {
        val response = ju.Arrays.asList(new TimesheetLock(startDate.toDate, startDate.plusDays(2).toDate), new TimesheetLock(endDate.minusDays(1).toDate, endDate.toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 2

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate + 2.days)

        locked(1).start.toLocalDate should be(endDate - 1.days)
        locked(1).end.toLocalDate should be(endDate)
      }

      "find first start day and last day as locked" in {
        val response = ju.Arrays.asList(new TimesheetLock(startDate.minusDays(2).toDate, startDate.toDate), new TimesheetLock(endDate.toDate, endDate.toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 2

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate)

        locked(1).start.toLocalDate should be(endDate)
        locked(1).end.toLocalDate should be(endDate)
      }
    }
  }
}
