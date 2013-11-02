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

@RunWith(classOf[JUnitRunner])
class TimesheetLockServiceImplTest extends WordSpec with Matchers with MockitoSugar with BeforeAndAfterEach  {
  val repository = mock[TimesheetLockDao]
  val service = new TimesheetLockServiceImpl(repository)

  val endDate = new LocalDate()
  val startDate = new LocalDate()

  val lock = new TimesheetLock(startDate.toDateMidnight.toDate, endDate.toDateMidnight.toDate)

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
      val lock2 = new TimesheetLock(startDate.plusDays(1).toDateMidnight.toDate, endDate.plusDays(2).toDateMidnight.toDate)

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

      lockedTimesheet should not be ('defined)
    }

  }
}
