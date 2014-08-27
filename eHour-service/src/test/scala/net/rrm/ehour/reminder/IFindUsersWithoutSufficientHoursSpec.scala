package net.rrm.ehour.reminder

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.activity.service.ActivityService
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain._
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao
import net.rrm.ehour.timesheet.service.TimesheetLockService
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.util.JodaDateUtil
import org.joda.time.{Interval, LocalDate}
import org.mockito.Matchers.{eq => mockitoEq, _}
import org.mockito.Mockito._

import scala.collection.JavaConversions._

class IFindUsersWithoutSufficientHoursSpec extends AbstractSpec {
  val userService = mock[UserService]
  val timesheetDao = mock[TimesheetDao]
  val activityService = mock[ActivityService]
  val lockService = mock[TimesheetLockService]

  val subject = new IFindUsersWithoutSufficientHours(userService, timesheetDao, activityService, lockService)

  val userA = UserObjectMother.createUser("a")
  userA.setUserId(1)
  val userB = UserObjectMother.createUser("b")
  userB.setUserId(2)

  val currentDate = new LocalDate()

  val project = ProjectObjectMother.createProject(1)

  val activityA = ActivityMother.createActivity(userA, project)
  activityA.setDateStart(currentDate.minusDays(15).toDate)
  activityA.setDateEnd(currentDate.plusDays(15).toDate)

  val activityB = ActivityMother.createActivity(userB, project)
  activityB.setDateStart(currentDate.minusDays(15).toDate)
  activityB.setDateEnd(currentDate.plusDays(15).toDate)

  override protected def beforeEach() = reset(userService, timesheetDao, lockService)

  "I find users without sufficient hours" should {
    "find the user with less than the minimum hours, ignore the user with more than the minimum" in {
      `user A has activity A`
      `user B has activity B`

      `user A and B are active`
      `no locked days are in the range`

      val timesheetEntryInvalid = `create a timesheet entry for user`(userA, 30)
      val timesheetEntryValid = `create a timesheet entry for user`(userB, 34)
      `with timesheet entries`(timesheetEntryInvalid, timesheetEntryValid)

      val foundUsers = subject.findUsersWithoutSufficientHours(32, 8)

      foundUsers should have size 1
      foundUsers should contain(userA)
    }

    "find no user as the user booked sufficient hours over multiple days within that week" in {
      `user A has activity A`
      `user B has activity B`

      `user A is active`
      `no locked days are in the range`

      val timesheetEntryValidA = `create a timesheet entry for user`(userA, 10)
      val timesheetEntryValidB = `create a timesheet entry for user`(userA, 10)
      val timesheetEntryValidC = `create a timesheet entry for user`(userA, 10)
      val timesheetEntryValidD = `create a timesheet entry for user`(userA, 10)
      `with timesheet entries`(timesheetEntryValidA, timesheetEntryValidB, timesheetEntryValidC, timesheetEntryValidD)

      val foundUsers = subject.findUsersWithoutSufficientHours(32, 8)

      foundUsers should be('empty)
    }

    "find a user when he has an active activity but didn't book any hours at all" in {
      `user A has activity A`
      `user B has activity B`

      `user A and B are active`
      `no locked days are in the range`

      val timesheetEntryInvalid = `create a timesheet entry for user`(userA, 34)
      `with timesheet entries`(timesheetEntryInvalid)

      val foundUsers = subject.findUsersWithoutSufficientHours(32, 8)

      foundUsers should have size 1
      foundUsers should contain(userB)
    }

    "ignore any users that are inactive, even if they booked hours" in {
      val userC = UserObjectMother.createUser("c")

      `user A has activity A`
      `user B has activity B`
      `user has activity B`(userC)

      `user A is active`
      `no locked days are in the range`

      val timesheetEntryInvalid = `create a timesheet entry for user`(userC, 30)
      `with timesheet entries`(timesheetEntryInvalid)

      val foundUsers = subject.findUsersWithoutSufficientHours(32, 8)

      foundUsers should have size 1
      foundUsers should contain(userA)
    }

    "ignore user that doesn't have project activitys covering the whole week" in {
      val activityA2 = ActivityMother.createActivity(userA, project)
      activityA2.setDateStart(currentDate.minusDays(2).toDate)
      activityA2.setDateEnd(currentDate.plusDays(1).toDate)

      when(activityService.getActivitiesForUser(mockitoEq(userA.getUserId), any(classOf[DateRange]))).thenReturn(List(activityA2))
      `user B has activity B`

      `user A and B are active`
      `no locked days are in the range`

      val timesheetEntryInvalid = `create a timesheet entry for user`(userA, 30)
      val timesheetEntryValid = `create a timesheet entry for user`(userB, 31)
      `with timesheet entries`(timesheetEntryInvalid, timesheetEntryValid)

      val foundUsers = subject.findUsersWithoutSufficientHours(32, 8)

      foundUsers should have size 1
      foundUsers should contain(userB)
    }

    "find a user that has project activitys with infinite start/end date" in {
      val activityA2 = ActivityMother.createActivity(userA, project)
      activityA2.setDateStart(null)
      activityA2.setDateEnd(null)

      when(activityService.getActivitiesForUser(mockitoEq(userA.getUserId), any(classOf[DateRange]))).thenReturn(List(activityA2))

      `user A is active`
      `no locked days are in the range`

      val timesheetEntryInsufficient = `create a timesheet entry for user`(userA, 30)
      `with timesheet entries`(timesheetEntryInsufficient)

      val foundUsers = subject.findUsersWithoutSufficientHours(32, 8)

      foundUsers should have size 1
      foundUsers should contain(userA)
    }

    "from the required minimum hours, subtract 8 hours per work day that is locked" in {
      def findWorkDay(date: LocalDate): LocalDate = if (JodaDateUtil.isWeekend(date)) findWorkDay(date.minusDays(1)) else date

      `user A has activity A`
      `user B has activity B`

      `user A and B are active`

      val nonWeekendDate = findWorkDay(currentDate)
      val lockedInterval = new Interval(nonWeekendDate.toDateTimeAtStartOfDay, nonWeekendDate.toDateTimeAtStartOfDay)
      when(lockService.findLockedDatesInRange(any(), any())).thenReturn(List(lockedInterval))

      val timesheetEntrySufficient = `create a timesheet entry for user`(userA, 30)
      val timesheetEntryInsufficient = `create a timesheet entry for user`(userB, 20)
      `with timesheet entries`(timesheetEntrySufficient, timesheetEntryInsufficient)

      val foundUsers = subject.findUsersWithoutSufficientHours(32, 8)

      foundUsers should have size 1
      foundUsers should contain(userB)
    }

    "from the required minimum hours, do not subtract any hours for locked weekend days" in {
      def findDateInWeekend(date: LocalDate): LocalDate = if (JodaDateUtil.isWeekend(date)) date else findDateInWeekend(date.minusDays(1))

      `user A has activity A`
      `user B has activity B`

      `user A and B are active`

      val weekendDate = findDateInWeekend(currentDate)
      val lockedInterval = new Interval(weekendDate.toDateTimeAtStartOfDay, weekendDate.toDateTimeAtStartOfDay)
      when(lockService.findLockedDatesInRange(any(), any())).thenReturn(List(lockedInterval))

      val timesheetEntrySufficient = `create a timesheet entry for user`(userA, 30)
      val timesheetEntryInsufficient = `create a timesheet entry for user`(userB, 20)
      `with timesheet entries`(timesheetEntrySufficient, timesheetEntryInsufficient)

      val foundUsers = subject.findUsersWithoutSufficientHours(32, 8)

      foundUsers should have size 2
      foundUsers should contain(userA)
      foundUsers should contain(userB)
    }
  }

  def `user has activity B`(user: User) {
    when(activityService.getActivitiesForUser(mockitoEq(user.getUserId), any(classOf[DateRange]))).thenReturn(List(activityB))
  }

  def `user A is active` {
    when(userService.getUsers(UserRole.USER)).thenReturn(List(userA))
  }

  def `with timesheet entries`(entry: TimesheetEntry*) {
    when(timesheetDao.getTimesheetEntriesInRange(any())).thenReturn(entry.toList)
  }

  def `create a timesheet entry for user`(user: User = userA, hours: Int = 30): TimesheetEntry = {
    val timesheetEntryInvalid = TimesheetEntryObjectMother.createTimesheetEntry(user, currentDate.toDate, hours)
    timesheetEntryInvalid
  }

  def `user A and B are active` {
    when(userService.getUsers(UserRole.USER)).thenReturn(List(userA, userB))
  }

  def `user B has activity B` {
    `user has activity B`(userB)
  }

  def `user A has activity A` {
    when(activityService.getActivitiesForUser(mockitoEq(userA.getUserId), any(classOf[DateRange]))).thenReturn(List(activityA))
  }

  def `no locked days are in the range` {
    when(lockService.findLockedDatesInRange(any(), any())).thenReturn(List())
  }
}
