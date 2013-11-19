package net.rrm.ehour.report.service

import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import net.rrm.ehour.report.service.ReportFilterFixture._
import net.rrm.ehour.report.criteria.UserSelectedCriteria
import net.rrm.ehour.persistence.user.dao.UserDao
import net.rrm.ehour.report.criteria.UserSelectedCriteria.ReportType
import net.rrm.ehour.domain.{UserObjectMother, ProjectObjectMother, ProjectAssignmentObjectMother}

class UserCriteriaFilterSpec extends WordSpec with MockitoSugar with Matchers with BeforeAndAfterEach {
  val dao = mock[UserDao]
  val subject = new UserCriteriaFilter(dao)

  override protected def beforeEach() = reset(dao)

  "User Criteria Filter" must {
    "find all users when no departments are provided" in {
      when(dao.findUsers(false)).thenReturn(List(pm))

      val criteria = new UserSelectedCriteria
      criteria.setOnlyActiveUsers(false)
      val users = subject.getAvailableUsers(criteria)

      users should have size 1

      verify(dao).findUsers(false)
    }

    "find all active users when no departments are provided" in {
      when(dao.findUsers(true)).thenReturn(List(pm))

      val criteria = new UserSelectedCriteria
      val users = subject.getAvailableUsers(criteria)

      users should have size 1

      verify(dao).findUsers(true)
    }

    "find all users for given departments" in {
      when(dao.findUsersForDepartments(List(department), false)).thenReturn(List(pm))

      val criteria = new UserSelectedCriteria
      criteria.setDepartments(List(department))
      criteria.setOnlyActiveUsers(false)
      val users = subject.getAvailableUsers(criteria)

      users should have size 1

      verify(dao).findUsersForDepartments(List(department), false)
    }

    "find all active users for given departments" in {
      when(dao.findUsersForDepartments(List(department), true)).thenReturn(List(pm))

      val criteria = new UserSelectedCriteria
      criteria.setDepartments(List(department))
      criteria.setOnlyActiveUsers(true)
      val users = subject.getAvailableUsers(criteria)

      users should have size 1

      verify(dao).findUsersForDepartments(List(department), true)
    }

    "find all users who are assigned to a PM project" in {
      val userNotAssigned = UserObjectMother.createUser()
      userNotAssigned.setFirstName("not assigned")
      val projectNoPm = ProjectObjectMother.createProject(1)
      val assignment = ProjectAssignmentObjectMother.createProjectAssignment(userNotAssigned, projectNoPm)
      userNotAssigned.addProjectAssignment(assignment)

      val userAssigned = UserObjectMother.createUser()
      userAssigned.setFirstName("assigned")
      val projectPm = ProjectObjectMother.createProject(2)
      projectPm.setProjectManager(pm)
      val pmAssignment = ProjectAssignmentObjectMother.createProjectAssignment(userAssigned, projectPm)
      userAssigned.addProjectAssignment(pmAssignment)

      when(dao.findUsers(true)).thenReturn(List(userNotAssigned, userAssigned))

      val criteria = new UserSelectedCriteria
      criteria.addReportType(ReportType.PM)
      criteria.setPm(pm)

      val users = subject.getAvailableUsers(criteria)

      users should have size 1
      users(0) should be (userAssigned)
    }
  }
}
