package net.rrm.ehour.timesheet.service


import java.util.Date
import java.{util => ju}

import com.github.nscala_time.time.Imports._
import com.google.common.collect.Lists
import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{TimesheetEntryObjectMother, TimesheetLock, UserObjectMother}
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao
import net.rrm.ehour.persistence.timesheetlock.dao.TimesheetLockDao
import org.joda.time.LocalDate
import org.mockito.Mockito._

class TimesheetLockServiceSpringImplSpec extends AbstractSpec {
  val lockDao = mock[TimesheetLockDao]
  val timesheetDao = mock[TimesheetDao]
  
  val service = new TimesheetLockServiceSpringImpl(lockDao, timesheetDao)

  val endDate = new LocalDate()
  val startDate = new LocalDate()

  val lock = new TimesheetLock(startDate.toDate, endDate.toDate)

  override def beforeEach() {
    reset(lockDao)
  }

  "Timesheet Lock Service" should {
    "create a new lock" in {
      val timesheet = service.createNew(None, startDate.toDate, endDate.toDate)

      timesheet.getDateStart should be(startDate.toDate)
    }

    "find all" in {
      val lock2 = new TimesheetLock(startDate.plusDays(1).toDate, endDate.plusDays(2).toDate)

      when(lockDao.findAll()).thenReturn(Lists.newArrayList(lock, lock2))

      val lockedTimesheets: List[TimesheetLock] = service.findAll()

      lockedTimesheets.length should be(2)
      lockedTimesheets(0).getDateStart should be(startDate.toDate)
      lockedTimesheets(1).getDateStart should be(startDate.plusDays(1).toDate)
    }

    "find on id and return some timesheet" in {
      when(lockDao.findById(2)).thenReturn(lock)

      val lockedTimesheet = service.find(2)

      lockedTimesheet should be('defined)
    }

    "find on id and return none when nothing is found" in {
      when(lockDao.findById(2)).thenReturn(null)

      val lockedTimesheet = service.find(2)

      lockedTimesheet should not be 'defined
    }

    "find no locked days when there are none" in {
      val locked = service.findLockedDatesInRange(LocalDate.parse("20130101").toDate, LocalDate.parse("20130108").toDate)

      locked should be('empty)
    }

    "aggregate the booked hours per affected user in a date range" in {
      val startDate = LocalDate.parse("20130101").toDate
      val endDate = LocalDate.parse("20130108").toDate

      val entryA = TimesheetEntryObjectMother.createTimesheetEntry(1, new Date(), 5)
      val entryB = TimesheetEntryObjectMother.createTimesheetEntry(1, new DateTime().plusDays(1).toDate, 8)
      val entryC = TimesheetEntryObjectMother.createTimesheetEntry(2, new Date(), 4)
      entryA.getEntryId.getProjectAssignment.getUser.setUsername("aplin")
      entryB.getEntryId.getProjectAssignment.getUser.setUsername("aplin")
      entryC.getEntryId.getProjectAssignment.getUser.setUsername("boplin")

      when(timesheetDao.getTimesheetEntriesInRange(new DateRange(startDate, endDate))).thenReturn(Lists.newArrayList(entryA, entryB, entryC))

      val affectedUsers = service.findAffectedUsers(startDate, endDate, Nil)

      affectedUsers should have size 2
      affectedUsers.head.hoursBooked should be (13)
      affectedUsers(1).hoursBooked should be (4)
    }

    "aggregate the booked hours per affected user in a date range excluding users" in {
      val startDate = LocalDate.parse("20130101").toDate
      val endDate = LocalDate.parse("20130108").toDate

      val entryA = TimesheetEntryObjectMother.createTimesheetEntry(1, new Date(), 5)
      val entryB = TimesheetEntryObjectMother.createTimesheetEntry(1, new DateTime().plusDays(1).toDate, 8)
      val entryC = TimesheetEntryObjectMother.createTimesheetEntry(2, new Date(), 4)
      entryA.getEntryId.getProjectAssignment.getUser.setUsername("aplin")
      entryB.getEntryId.getProjectAssignment.getUser.setUsername("aplin")
      entryC.getEntryId.getProjectAssignment.getUser.setUsername("boplin")

      when(timesheetDao.getTimesheetEntriesInRange(new DateRange(startDate, endDate))).thenReturn(Lists.newArrayList(entryA, entryB, entryC))

      val affectedUsers = service.findAffectedUsers(startDate, endDate, List(entryC.getEntryId.getProjectAssignment.getUser))

      affectedUsers should have size 1
      affectedUsers.head.hoursBooked should be (13)
    }

    {
      val startDate = LocalDate.parse("2013-01-01")
      val endDate = LocalDate.parse("2013-01-08")

      def findLockedDatesInRange(response: ju.List[TimesheetLock]) = {
        when(lockDao.findMatchingLock(startDate.toDate, endDate.toDate)).thenReturn(response)
        val l = service.findLockedDatesInRange(startDate.toDate, endDate.toDate)
        verify(lockDao).findMatchingLock(startDate.toDate, endDate.toDate)

        l
      }

      "find first 2 days as locked with lock matching requested range" in {
        val response = Lists.newArrayList(new TimesheetLock(startDate.toDate, startDate.plusDays(2).toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate + 2.days)
      }


      "find first 2 days as locked with lock partially overlapping the requested range" in {
        val response = Lists.newArrayList(new TimesheetLock(startDate.minusDays(2).toDate, startDate.plusDays(2).toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate + 2.days)
      }

      "find first day as locked when locked abuts the requested range at the start" in {
        val response = Lists.newArrayList(new TimesheetLock(startDate.minusDays(2).toDate, startDate.toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate)
      }

      "find last 2 days as locked with lock matching the requested range" in {
        val response = Lists.newArrayList(new TimesheetLock(endDate.minusDays(2).toDate, endDate.toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(endDate - 2.days)
        locked(0).end.toLocalDate should be(endDate)
      }

      "find last 2 days as locked with lock partially overlapping the requested range" in {
        val response = Lists.newArrayList(new TimesheetLock(endDate.minusDays(2).toDate, endDate.plusDays(2).toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(endDate - 2.days)
        locked(0).end.toLocalDate should be(endDate)
      }

      "find last day locked as end lock is inclusive" in {
        val response = Lists.newArrayList(new TimesheetLock(endDate.toDate, endDate.plusDays(1).toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(endDate)
        locked(0).end.toLocalDate should be(endDate)
      }

      "find first 2 days and last day as locked" in {
        val response = Lists.newArrayList(new TimesheetLock(startDate.toDate, startDate.plusDays(2).toDate), new TimesheetLock(endDate.minusDays(1).toDate, endDate.toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 2

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate + 2.days)

        locked(1).start.toLocalDate should be(endDate - 1.days)
        locked(1).end.toLocalDate should be(endDate)
      }

      "find first start day and last day as locked" in {
        val response = Lists.newArrayList(new TimesheetLock(startDate.minusDays(2).toDate, startDate.toDate), new TimesheetLock(endDate.toDate, endDate.toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 2

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate)

        locked(1).start.toLocalDate should be(endDate)
        locked(1).end.toLocalDate should be(endDate)
      }

      "find first 4 days as locked with locks overlapping eachother" in {
        val response = Lists.newArrayList(new TimesheetLock(startDate.minusDays(2).toDate, startDate.plusDays(2).toDate), new TimesheetLock(startDate.minusDays(2).toDate, startDate.plusDays(4).toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate + 4.days)
      }

      "find first 4 days as locked with locks abuts eachother" in {
        val response = Lists.newArrayList(new TimesheetLock(startDate.minusDays(2).toDate, startDate.plusDays(2).toDate), new TimesheetLock(startDate.plusDays(2).toDate, startDate.plusDays(4).toDate))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start.toLocalDate should be(startDate)
        locked(0).end.toLocalDate should be(startDate + 4.days)
      }

      "exclude locks where user is.. excluded !" in {
        val userA = UserObjectMother.createUser()
        val userB = UserObjectMother.createUser()
        userB.setUsername("imanotheruser")

        val start = startDate
        val end = startDate.plusDays(2)
        val lockA = new TimesheetLock(start.toDate, end.toDate, Lists.newArrayList(userA))
        val lockB = new TimesheetLock(startDate.toDate, startDate.plusDays(3).toDate, Lists.newArrayList(userB))
        val response = Lists.newArrayList(lockA, lockB)

        when(lockDao.findMatchingLock(startDate.toDate, endDate.toDate)).thenReturn(response)

        val locked = service.findLockedDatesInRange(startDate.toDate, endDate.toDate, userB)

        locked should have size 1

        locked(0).start.toLocalDate should be(start)
        locked(0).end.toLocalDate should be(end)
      }

      "don't fail when there are no locks" in {
        val response = Lists.newArrayList[TimesheetLock]()

        when(lockDao.findMatchingLock(startDate.toDate, endDate.toDate)).thenReturn(response)

        val locked = service.findLockedDatesInRange(startDate.toDate, endDate.toDate, UserObjectMother.createUser())

        locked should have size 0
      }
    }
  }
}
