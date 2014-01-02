package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.project.service.ProjectAssignmentService
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.domain.{ProjectAssignmentObjectMother, ProjectObjectMother}
import org.mockito.Mockito._
import net.rrm.ehour.util._
import org.apache.wicket.model.Model

class ManageAssignmentsPanelSpec extends AbstractSpringWebAppSpec {
  "Mange Assignments Panel" should {
    val assignmentService = mockService[ProjectAssignmentService]
    mockService[UserService]

    "render" in {
      val project = ProjectObjectMother.createProject(1)
      when(assignmentService.getProjectAssignmentsAndCheckDeletability(project)).thenReturn(toJava(List(ProjectAssignmentObjectMother.createProjectAssignment(1))))

      tester.startComponentInPage(new ManageAssignmentsPanel("id", new Model(new ProjectAdminBackingBean(project))))
      tester.assertNoErrorMessage()
    }
  }
}
