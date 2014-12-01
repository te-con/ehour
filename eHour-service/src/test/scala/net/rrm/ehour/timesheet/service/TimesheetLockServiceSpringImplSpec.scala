package net.rrm.ehour.timesheet.service


import java.util.Date

import com.github.nscala_time.time.Imports._
import com.google.common.collect.Lists
import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{TimesheetEntryObjectMother, UserObjectMother}
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao
import net.rrm.ehour.timesheet.dto.TimesheetLock
import org.joda.time.LocalDate
import org.mockito.ArgumentCaptor
import org.mockito.Mockito._

class TimesheetLockServiceSpringImplSpec extends AbstractSpec {
  private val lockPort = mock[TimesheetLockPort]
  private val timesheetDao = mock[TimesheetDao]

  private val service = new TimesheetLockServiceSpringImpl(lockPort, timesheetDao)

  private val s = LocalDate.parse("20130101").toDateTimeAtStartOfDay
  private val e = LocalDate.parse("20130108").toDateTimeAtStartOfDay
  private val week = new Interval(s, e)

  private val lock = TimesheetLock(s, e)

  override def beforeEach() {
    reset(lockPort)
  }

  "Timesheet Lock Service" should {
    "create a new lock" in {
      service.createNew(None, s, e)
      val captor = ArgumentCaptor.forClass(classOf[TimesheetLock])
      verify(lockPort).persist(captor.capture())
      captor.getValue.startDate should be(s)
    }

    "find all" in {
      val lock2 = TimesheetLock(s.plusDays(1), e.plusDays(2))

      when(lockPort.findAll()).thenReturn(List(lock, lock2))

      val lockedTimesheets: List[TimesheetLock] = service.findAll()

      lockedTimesheets.length should be(2)
      lockedTimesheets(0).startDate should be(s)
      lockedTimesheets(1).startDate should be(s.plusDays(1))
    }

    "find on id and return some timesheet" in {
      when(lockPort.findById(2)).thenReturn(Some(lock))

      val lockedTimesheet = service.find(2)

      lockedTimesheet should be('defined)
    }

    "find on id and return none when nothing is found" in {
      when(lockPort.findById(2)).thenReturn(None)

      val lockedTimesheet = service.find(2)

      lockedTimesheet should not be 'defined
    }

    "find no locked days when there are none" in {
      val i = new Interval(LocalDate.parse("20130101").toDateTimeAtCurrentTime, LocalDate.parse("20130108").toDateTimeAtCurrentTime)
      when(lockPort.findMatchingLock(i)).thenReturn(List())
      val locked = service.findLockedDates(i)

      locked should be('empty)
    }

    "aggregate the booked hours per affected user in a date range" in {
      val entryA = TimesheetEntryObjectMother.createTimesheetEntry(1, new Date(), 5)
      val entryB = TimesheetEntryObjectMother.createTimesheetEntry(1, new DateTime().plusDays(1).toDate, 8)
      val entryC = TimesheetEntryObjectMother.createTimesheetEntry(2, new Date(), 4)
      entryA.getEntryId.getProjectAssignment.getUser.setUsername("aplin")
      entryB.getEntryId.getProjectAssignment.getUser.setUsername("aplin")
      entryC.getEntryId.getProjectAssignment.getUser.setUsername("boplin")

      when(timesheetDao.getTimesheetEntriesInRange(new DateRange(s, e))).thenReturn(Lists.newArrayList(entryA, entryB, entryC))

      val affectedUsers = service.findAffectedUsers(s, e, Nil)

      affectedUsers should have size 2
      affectedUsers.head.hoursBooked should be (13)
      affectedUsers(1).hoursBooked should be (4)
    }

    "aggregate the booked hours per affected user in a date range excluding users" in {
      val entryA = TimesheetEntryObjectMother.createTimesheetEntry(1, new Date(), 5)
      val entryB = TimesheetEntryObjectMother.createTimesheetEntry(1, new DateTime().plusDays(1).toDate, 8)
      val entryC = TimesheetEntryObjectMother.createTimesheetEntry(2, new Date(), 4)
      entryA.getEntryId.getProjectAssignment.getUser.setUsername("aplin")
      entryB.getEntryId.getProjectAssignment.getUser.setUsername("aplin")
      entryC.getEntryId.getProjectAssignment.getUser.setUsername("boplin")

      when(timesheetDao.getTimesheetEntriesInRange(new DateRange(s, e))).thenReturn(Lists.newArrayList(entryA, entryB, entryC))

      val affectedUsers = service.findAffectedUsers(s, e, List(entryC.getEntryId.getProjectAssignment.getUser))

      affectedUsers should have size 1
      affectedUsers.head.hoursBooked should be (13)
    }

    {
      def findLockedDatesInRange(response: List[TimesheetLock]) = {
        when(lockPort.findMatchingLock(week)).thenReturn(response)
        val l = service.findLockedDates(week)
        verify(lockPort).findMatchingLock(week)

        l
      }

      "find first 2 days as locked with lock matching requested range" in {
        val response = List(TimesheetLock(s, s.plusDays(2)))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start should be(s)
        locked(0).end should be(s + 2.days)
      }


      "find first 2 days as locked with lock partially overlapping the requested range" in {
        val response = List(TimesheetLock(s.minusDays(2), s.plusDays(2)))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start should be(s)
        locked(0).end should be(s + 2.days)
      }

      "find first day as locked when locked abuts the requested range at the start" in {
        val response = List(TimesheetLock(s.minusDays(2), s))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start should be(s)
        locked(0).end should be(s)
      }

      "find last 2 days as locked with lock matching the requested range" in {
        val response = List(TimesheetLock(e.minusDays(2), e))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start should be(e - 2.days)
        locked(0).end should be(e)
      }

      "find last 2 days as locked with lock partially overlapping the requested range" in {
        val response = List(TimesheetLock(e.minusDays(2), e.plusDays(2)))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start should be(e - 2.days)
        locked(0).end should be(e)
      }

      "find last day locked as end lock is inclusive" in {
        val response = List(TimesheetLock(e, e.plusDays(1)))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start should be(e)
        locked(0).end should be(e)
      }

      "find first 2 days and last day as locked" in {
        val response = List(TimesheetLock(s, s.plusDays(2)), TimesheetLock(e.minusDays(1), e))
        val locked = findLockedDatesInRange(response)

        locked should have size 2

        locked(0).start should be(s)
        locked(0).end should be(s + 2.days)

        locked(1).start should be(e - 1.days)
        locked(1).end should be(e)
      }

      "find first start day and last day as locked" in {
        val response = List(TimesheetLock(s.minusDays(2), s), TimesheetLock(e, e))
        val locked = findLockedDatesInRange(response)

        locked should have size 2

        locked(0).start should be(s)
        locked(0).end should be(s)

        locked(1).start should be(e)
        locked(1).end should be(e)
      }

      "find first 4 days as locked with locks overlapping eachother" in {
        val response = List(TimesheetLock(s.minusDays(2), s.plusDays(2)), TimesheetLock(s.minusDays(2), s.plusDays(4)))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start should be(s)
        locked(0).end should be(s + 4.days)
      }

      "find first 4 days as locked with locks abuts eachother" in {
        val response = List(TimesheetLock(s.minusDays(2), s.plusDays(2)), TimesheetLock(s.plusDays(2), s.plusDays(4)))
        val locked = findLockedDatesInRange(response)

        locked should have size 1

        locked(0).start should be(s)
        locked(0).end should be(s + 4.days)
      }

      "exclude locks where user is.. excluded !" in {
        val userA = UserObjectMother.createUser()
        val userB = UserObjectMother.createUser()
        userB.setUsername("imanotheruser")

        val start = s
        val end = s.plusDays(2)
        val lockA = TimesheetLock(start, end, List(userA))
        val lockB = TimesheetLock(s, s.plusDays(3), List(userB))
        val response = List(lockA, lockB)

        val i = new Interval(s, e)
        when(lockPort.findMatchingLock(i)).thenReturn(response)

        val locked = service.findLockedDates(i, userB)

        locked should have size 1

        locked(0).start should be(start)
        locked(0).end should be(end)
      }

      "don't fail when there are no locks" in {
        val response = List[TimesheetLock]()

        val i = new Interval(s, e)
        when(lockPort.findMatchingLock(i)).thenReturn(response)

        val locked = service.findLockedDates(i, UserObjectMother.createUser())

        locked should have size 0
      }
    }
  }
}
