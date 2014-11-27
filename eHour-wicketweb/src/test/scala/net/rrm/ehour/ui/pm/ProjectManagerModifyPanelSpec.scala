package net.rrm.ehour.ui.pm

import net.rrm.ehour.AbstractSpringWebAppSpec
import org.scalatest.BeforeAndAfterAll
import net.rrm.ehour.domain.{ProjectAssignmentObjectMother, ProjectObjectMother}
import net.rrm.ehour.project.service.{ProjectAssignmentManagementService, ProjectAssignmentService}
import org.mockito.Mockito._
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.util._
import net.rrm.ehour.ui.DummyUIDataGenerator

class ProjectManagerModifyPanelSpec extends AbstractSpringWebAppSpec with BeforeAndAfterAll {
  val assignmentService = mock[ProjectAssignmentService]
  val assignmentMgmtService = mock[ProjectAssignmentManagementService]
  val userService = mock[UserService]

  override def beforeAll() {
    springTester.getMockContext.putBean(assignmentService)
    springTester.getMockContext.putBean(userService)
    springTester.getMockContext.putBean(assignmentMgmtService)
    springTester.setUp()
  }

  override def beforeEach() {
    reset(assignmentService, userService, assignmentMgmtService)
  }

  "Project Management Project Info Panel" should {
    "render" in {
      tester.startComponentInPage(new ProjectManagerModifyPanel("id", ProjectObjectMother.createProject(1)))
      tester.assertNoErrorMessage()
    }

    "update a modified assignment" ignore {
      val project = ProjectObjectMother.createProject(1)
      val assignment = ProjectAssignmentObjectMother.createProjectAssignment(1)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(assignment)))

      when(assignmentService.getProjectAssignmentTypes).thenReturn(DummyUIDataGenerator.getProjectAssignmentTypes)

      tester.startComponentInPage(new ProjectManagerModifyPanel("id", project))

      tester.executeAjaxEvent("id:border:border_body:assignments:border:border_body:assignedUserPanel:border:border_body:assignments:0", "click")

      val formTester =tester.newFormTester("id:border:border_body:assignments:border:border_body:assignmentFormPanel:border:greySquaredFrame:border_body:assignmentForm")
      formTester.setValue("formComponents:projectAssignment.active", false)
      formTester.select("formComponents:assignmentType:projectAssignment.assignmentType", 0)

      tester.executeAjaxEvent("id:border:border_body:assignments:border:border_body:assignmentFormPanel:border:greySquaredFrame:border_body:assignmentForm:submitButton", "onclick")

      tester.assertNoErrorMessage();
      verify(assignmentMgmtService).persistNewProjectAssignment(assignment)
    }

    "delete an assignment" ignore {
      val project = ProjectObjectMother.createProject(1)
      val assignment = ProjectAssignmentObjectMother.createProjectAssignment(1)
      assignment.setDeletable(true)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(assignment)))

      tester.startComponentInPage(new ProjectManagerModifyPanel("id", project))

      tester.executeAjaxEvent("id:border:border_body:assignments:assignmentContainer:assignments:0:container", "click")
      tester.executeAjaxEvent("id:border:border_body:assignments:assignmentContainer:assignments:0:container:editForm:delete", "click")

      tester.executeAjaxEvent("id:border:border_body:submitButton", "click")

      verify(assignmentMgmtService).deleteProjectAssignment(assignment)
    }

  }
}
