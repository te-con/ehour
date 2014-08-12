package net.rrm.ehour.reminder

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain._
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.user.service.UserService
import org.joda.time.LocalDate
import org.mockito.Matchers.{eq => mockitoEq, _}
import org.mockito.Mockito._

import scala.collection.JavaConversions._
class IFindUsersWithoutSufficientHoursSpec extends AbstractSpec {
  val userService = mock[UserService]
  val timesheetDao = mock[TimesheetDao]
  val assignmentService = mock[ProjectAssignmentService]

  val subject = new IFindUsersWithoutSufficientHours(userService, timesheetDao, assignmentService)

  val userA = UserObjectMother.createUser("a")
  userA.setUserId(1)
  val userB = UserObjectMother.createUser("b")
  userB.setUserId(2)

  val currentDate = new LocalDate()

  val project = ProjectObjectMother.createProject(1)

  val assignmentA = ProjectAssignmentObjectMother.createProjectAssignment(userA, project)
  assignmentA.setDateStart(currentDate.minusDays(15).toDate)
  assignmentA.setDateEnd(currentDate.plusDays(15).toDate)

  val assignmentB = ProjectAssignmentObjectMother.createProjectAssignment(userB, project)
  assignmentB.setDateStart(currentDate.minusDays(15).toDate)
  assignmentB.setDateEnd(currentDate.plusDays(15).toDate)

  override protected def beforeEach() = reset(userService, timesheetDao)

  "I find users without sufficient hours" should {
    "find 1 user with 30 hours" in {
      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userA.getUserId), any(classOf[DateRange]))).thenReturn(List(assignmentA))
      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userB.getUserId), any(classOf[DateRange]))).thenReturn(List(assignmentB))

      when(userService.getUsers(UserRole.USER)).thenReturn(List(userA, userB))

      val timesheetEntryInvalid = TimesheetEntryObjectMother.createTimesheetEntry(userA, currentDate.toDate, 30)
      val timesheetEntryValid = TimesheetEntryObjectMother.createTimesheetEntry(userB, currentDate.toDate, 34)
      when(timesheetDao.getTimesheetEntriesInRange(any())).thenReturn(List(timesheetEntryInvalid, timesheetEntryValid))

      val foundUsers = subject.findUsersWithoutSufficientHours(32)

      foundUsers should have size 1
      foundUsers should contain(userA)
    }

    "find no user as the user booked sufficient hours" in {
      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userA.getUserId), any(classOf[DateRange]))).thenReturn(List(assignmentA))
      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userB.getUserId), any(classOf[DateRange]))).thenReturn(List(assignmentB))

      when(userService.getUsers(UserRole.USER)).thenReturn(List(userA))

      val timesheetEntryValidA = TimesheetEntryObjectMother.createTimesheetEntry(userA, currentDate.toDate, 10)
      val timesheetEntryValidB = TimesheetEntryObjectMother.createTimesheetEntry(userA, currentDate.toDate, 10)
      val timesheetEntryValidC = TimesheetEntryObjectMother.createTimesheetEntry(userA, currentDate.toDate, 10)
      val timesheetEntryValidD = TimesheetEntryObjectMother.createTimesheetEntry(userA, currentDate.toDate, 10)
      when(timesheetDao.getTimesheetEntriesInRange(any())).thenReturn(List(timesheetEntryValidA, timesheetEntryValidB, timesheetEntryValidC, timesheetEntryValidD))

      val foundUsers = subject.findUsersWithoutSufficientHours(32)

      foundUsers should be ('empty)
    }

    "find 1 user without any hours booked at all" in {
      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userA.getUserId), any(classOf[DateRange]))).thenReturn(List(assignmentA))
      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userB.getUserId), any(classOf[DateRange]))).thenReturn(List(assignmentB))

      when(userService.getUsers(UserRole.USER)).thenReturn(List(userA, userB))

      val timesheetEntryInvalid = TimesheetEntryObjectMother.createTimesheetEntry(userA, currentDate.toDate, 34)
      when(timesheetDao.getTimesheetEntriesInRange(any())).thenReturn(List(timesheetEntryInvalid))

      val foundUsers = subject.findUsersWithoutSufficientHours(32)

      foundUsers should have size 1
      foundUsers should contain (userB)
    }

    "find 1 user with 30 hours and should ignore the deactivated user who booked hours" in {
      val userC = UserObjectMother.createUser("c")

      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userA.getUserId), any(classOf[DateRange]))).thenReturn(List(assignmentA))
      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userB.getUserId), any(classOf[DateRange]))).thenReturn(List(assignmentB))
      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userC.getUserId), any(classOf[DateRange]))).thenReturn(List(assignmentB))

      when(userService.getUsers(UserRole.USER)).thenReturn(List(userA))

      val timesheetEntryInvalid = TimesheetEntryObjectMother.createTimesheetEntry(userC, currentDate.toDate, 30)
      when(timesheetDao.getTimesheetEntriesInRange(any())).thenReturn(List(timesheetEntryInvalid))

      val foundUsers = subject.findUsersWithoutSufficientHours(32)

      foundUsers should have size 1
      foundUsers should contain (userA)
    }

    "ignore user that doesn't have project assignments covering the whole range" in {
      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userA.getUserId), any(classOf[DateRange]))).thenReturn(List())
      when(assignmentService.getProjectAssignmentsForUser(mockitoEq(userB.getUserId), any(classOf[DateRange]))).thenReturn(List(assignmentB))

      when(userService.getUsers(UserRole.USER)).thenReturn(List(userA, userB))

      val timesheetEntryInvalid = TimesheetEntryObjectMother.createTimesheetEntry(userA, currentDate.toDate, 30)
      val timesheetEntryValid = TimesheetEntryObjectMother.createTimesheetEntry(userB, currentDate.toDate, 31)
      when(timesheetDao.getTimesheetEntriesInRange(any())).thenReturn(List(timesheetEntryInvalid, timesheetEntryValid))

      val foundUsers = subject.findUsersWithoutSufficientHours(32)

      foundUsers should have size 1
      foundUsers should contain(userB)
    }

    "subtract 8 hours per day that is locked" ignore {
      fail()
    }
  }
}
