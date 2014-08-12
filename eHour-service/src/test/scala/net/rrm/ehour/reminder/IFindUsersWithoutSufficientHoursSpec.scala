package net.rrm.ehour.reminder

import java.util.Date

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.domain.{TimesheetEntryObjectMother, UserObjectMother, UserRole}
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.user.service.UserService
import org.mockito.Matchers._
import org.mockito.Mockito._

import scala.collection.JavaConversions._
class IFindUsersWithoutSufficientHoursSpec extends AbstractSpec {
  val userService = mock[UserService]
  val timesheetDao = mock[TimesheetDao]
  private val assignmentService = mock[ProjectAssignmentService]

  val subject = new IFindUsersWithoutSufficientHours(userService, timesheetDao, assignmentService)

  val userA = UserObjectMother.createUser("a")
  val userB = UserObjectMother.createUser("b")

  override protected def beforeEach() = reset(userService, timesheetDao)

  "I find users without sufficient hours" should {
    "find 1 user with 30 hours" in {
      when(userService.getUsers(UserRole.USER)).thenReturn(List(userA, userB))

      val timesheetEntryInvalid = TimesheetEntryObjectMother.createTimesheetEntry(userA, new Date(), 30)
      val timesheetEntryValid = TimesheetEntryObjectMother.createTimesheetEntry(userB, new Date(), 34)
      when(timesheetDao.getTimesheetEntriesInRange(any())).thenReturn(List(timesheetEntryInvalid, timesheetEntryValid))

      val foundUsers = subject.findUsersWithoutSufficientHours(32)

      foundUsers should have size 1
      foundUsers should contain (userA)
    }

    "find no user as the user booked sufficient hours" in {
      when(userService.getUsers(UserRole.USER)).thenReturn(List(userA))

      val timesheetEntryValidA = TimesheetEntryObjectMother.createTimesheetEntry(userA, new Date(), 10)
      val timesheetEntryValidB = TimesheetEntryObjectMother.createTimesheetEntry(userA, new Date(), 10)
      val timesheetEntryValidC = TimesheetEntryObjectMother.createTimesheetEntry(userA, new Date(), 10)
      val timesheetEntryValidD = TimesheetEntryObjectMother.createTimesheetEntry(userA, new Date(), 10)
      when(timesheetDao.getTimesheetEntriesInRange(any())).thenReturn(List(timesheetEntryValidA, timesheetEntryValidB, timesheetEntryValidC, timesheetEntryValidD))

      val foundUsers = subject.findUsersWithoutSufficientHours(32)

      foundUsers should be ('empty)
    }

    "find 1 user without any hours booked at all" in {
      when(userService.getUsers(UserRole.USER)).thenReturn(List(userA, userB))

      val timesheetEntryInvalid = TimesheetEntryObjectMother.createTimesheetEntry(userA, new Date(), 34)
      when(timesheetDao.getTimesheetEntriesInRange(any())).thenReturn(List(timesheetEntryInvalid))

      val foundUsers = subject.findUsersWithoutSufficientHours(32)

      foundUsers should have size 1
      foundUsers should contain (userB)
    }

    "find 1 user with 30 hours and should ignore the deactivated user who booked hours" in {
      val userC = UserObjectMother.createUser("c")

      when(userService.getUsers(UserRole.USER)).thenReturn(List(userA))

      val timesheetEntryInvalid = TimesheetEntryObjectMother.createTimesheetEntry(userC, new Date(), 30)
      when(timesheetDao.getTimesheetEntriesInRange(any())).thenReturn(List(timesheetEntryInvalid))

      val foundUsers = subject.findUsersWithoutSufficientHours(32)

      foundUsers should have size 1
      foundUsers should contain (userA)
    }

    "ignore user that doesn't have project assignments covering the whole range" in {
      fail()
    }

    "subtract 8 hours per day that is locked" in {
      fail()
    }
  }
}
